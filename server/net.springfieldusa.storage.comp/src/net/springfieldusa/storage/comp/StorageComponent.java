package net.springfieldusa.storage.comp;

import net.springfieldusa.mongodb.comp.MongoDBComponent;
import net.springfieldusa.storage.StorageService;

import org.bson.types.ObjectId;
import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component(service=StorageService.class)
// TODO: this implementation is very hacky and should be revisited
public class StorageComponent extends MongoDBComponent implements StorageService
{
  @Override
  public void create(String collectionName, String json)
  {
    DBCollection collection = getCollection(collectionName);
    DBObject data = (DBObject) JSON.parse(json);
    collection.insert(data);
  }

  @Override
  public String retrieve(String collectionName, String id)
  {
    DBCollection collection = getCollection(collectionName);
    DBObject object = collection.findOne(new ObjectId(id));
    return object.toString();
  }

  @Override
  public void update(String collectionName, String json)
  {
    DBCollection collection = getCollection(collectionName);
    DBObject data = (DBObject) JSON.parse(json);
    collection.save(data);    
  }

  @Override
  public void delete(String collectionName, String id)
  {
    DBCollection collection = getCollection(collectionName);
    collection.remove(new BasicDBObject("_id", new ObjectId(id)));
  }

  @Override
  public String find(String collectionName, String query)
  {
    DBObject jsonQuery = (DBObject) JSON.parse(query);
    DBCollection collection = getCollection(collectionName);
    DBCursor cursor = collection.find(jsonQuery);
    
    boolean firstOne = true;
    StringBuilder results = new StringBuilder("[");
    
    while(cursor.hasNext())
    {
      if(firstOne)
        firstOne = false;
      else
        results.append(',');
      
      results.append(cursor.next().toString());
    }
    
    results.append(']');
    return results.toString();
  }

  @Reference(unbind = "-", target = "(alias=data)")
  public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
  {
    super.bindMongoDatabaseProvider(mongoDatabaseProvider);
  }
}
