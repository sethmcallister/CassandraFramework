## Usage
### Creating data object
```java
class Example extends DataObject {
        public Example(UUID _id) {
            super(_id);
        }
}
```
### Creating a new DataObjectManager
```java
DataObjectManager<Example> exampleManager = new DataObjectManager("keyspace", "tablename");
```

### Inserting a DataObject
```java
exampleManager.insert(new Example());
```

### Getting a DataObject
```java
exampleManager.get(uuid);
```