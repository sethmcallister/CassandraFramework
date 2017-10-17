package xyz.sethy.profiles.api.data;

import java.util.UUID;

public abstract class DataObject {
    private final UUID _id;

    public DataObject(final UUID _id) {
        this._id = _id;
    }

    public UUID get_id() {
        return _id;
    }
}
