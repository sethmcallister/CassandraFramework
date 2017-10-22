package xyz.sethy.profiles.api.data;

import com.datastax.driver.core.*;
import xyz.sethy.profiles.api.data.codec.JsonCodec;

import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.String.format;

public class DataObjectManager<T extends DataObject> {

    private final String keyspace;
    private final Cluster cluster;
    private final JsonCodec<T> codec = (JsonCodec<T>) new JsonCodec<>(DataObject.class);

    public DataObjectManager(final String table, final String keyspace) {
        this.keyspace = keyspace;
        this.cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
        codecRegistry.register(codec);

        System.out.println("creating keysapce");
        Statement statement = new SimpleStatement(format("CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 } AND DURABLE_WRITES = true", keyspace));
        this.cluster.connect().execute(statement);

        System.out.println("creating table");
        Statement statement1 = new SimpleStatement(format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, json list<int>)", keyspace, table));
        this.cluster.connect().execute(statement1);
    }


    public T get(final UUID _id) {
        Statement statement = new SimpleStatement(format("SELECT * FROM %s.profiles WHERE id = %s", keyspace, _id));
        ResultSet set = cluster.connect().execute(statement);
        List<Integer> list = set.one().getList("json", Integer.class);
        byte[] bytes = toByteArray(list);
        return this.codec.deserialize(ByteBuffer.wrap(bytes), ProtocolVersion.V5);
    }

    public void insert(final T object) {
        ByteBuffer serialized = this.codec.serialize(object, ProtocolVersion.V5);
        String toInsert = Arrays.toString(serialized.array());
        System.out.println(toInsert);
        Statement statement = new SimpleStatement(format("INSERT INTO %s.profiles (id, json) VALUES (%s, %s)", keyspace, object.get_id(), toInsert));
        cluster.connect().execute(statement);
    }

    public boolean delete(final UUID _id) {
        Statement statement = new SimpleStatement(format("DELETE FROM %s.profiles WHERE id = %s", keyspace, _id));
        return cluster.connect().execute(statement).wasApplied();
    }

    private byte[] toByteArray(final List<Integer> list) {
        byte[] bytes = new byte[list.size()];
        Iterator<Integer> integerIterator = list.iterator();
        int index = 0;
        while(integerIterator.hasNext()) {
            bytes[index] = integerIterator.next().byteValue();
        }
        return bytes;
    }
}
