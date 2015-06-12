package net.springfieldusa.users.comp;

import org.bson.types.ObjectId;
import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

import net.springfieldusa.mongodb.comp.MongoDBComponent;
import net.springfieldusa.users.UserService;

@Component(service = UserService.class)
public class UserComponent extends MongoDBComponent implements UserService 
{
  private static final String USERS = "users";

  @Override
  public JSONObject addUser(JSONObject user)
  {
    DBObject data = (DBObject) JSON.parse(user.toString());
    WriteResult result = getCollection(USERS).insert(data);
    
    try
    {
      user.put("id", result.getUpsertedId().toString());
      log(LogService.LOG_DEBUG, "Adding user: '" + user.getString("id") + "'");
      return user;
    }
    catch (JSONException e)
    {
      log(LogService.LOG_ERROR, "Failed to add id to user object", e);
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void removeUser(String userId)
  {
    getCollection(USERS).remove(new BasicDBObject("_id", new ObjectId(userId)));
  }
  
  @Reference(unbind = "-", target = "(alias=data)")
  public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
  {
    super.bindMongoDatabaseProvider(mongoDatabaseProvider);
  }
}
