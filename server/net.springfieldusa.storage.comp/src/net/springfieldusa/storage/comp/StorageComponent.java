package net.springfieldusa.storage.comp;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.springfieldusa.mongodb.comp.MongoDBComponent;
import net.springfieldusa.storage.StorageService;

@Component(service = StorageService.class)
// TODO: this implementation is very hacky and should be revisited
public class StorageComponent extends MongoDBComponent implements StorageService
{
  @Override
  public JSONObject create(String collection, JSONObject json) throws JSONException
  {
    DBObject data = (DBObject) JSON.parse(json.toString());
    getCollection(collection).insert(data);

    json.put("_id", data.get("_id").toString());
    log(LogService.LOG_DEBUG, "Adding object: '" + json.getString("_id") + "'");
    return json;
  }

  @Override
  public JSONObject retrieve(String collection, String id) throws JSONException
  {
    return retrieve(collection, "_id", id);
  }

  @Override
  public JSONObject retrieve(String collection, String key, String value) throws JSONException
  {
    DBObject result = getCollection(collection).findOne(new BasicDBObject(key, value));
    return result != null ? new JSONObject(result.toString()) : null;
  }

  @Override
  public void update(String collectionName, String json)
  {
    DBCollection collection = getCollection(collectionName);
    DBObject data = (DBObject) JSON.parse(json);
    collection.save(data);
  }

  @Override
  public void delete(String collection, String id)
  {
    getCollection(collection).remove(new BasicDBObject("_id", new ObjectId(id)));
  }

  @Override
  public Collection<JSONObject> find(String collection, String query) throws JSONException
  {
    DBObject jsonQuery = (DBObject) JSON.parse(query);
    DBCursor cursor = getCollection(collection).find(jsonQuery);
    ArrayList<JSONObject> items = new ArrayList<>();
    
    for(DBObject value : cursor)
    {
      JSONObject json = new JSONObject(value.toString());
      items.add(json);
    }
    
    return items;
  }

  @Reference(unbind = "-", target = "(alias=data)")
  public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
  {
    super.bindMongoDatabaseProvider(mongoDatabaseProvider);
  }
}
