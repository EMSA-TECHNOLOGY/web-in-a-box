package net.springfieldusa.log.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.springfieldusa.log.LogService;
import net.springfieldusa.mongodb.comp.MongoDBComponent;

@Component(service = LogService.class)
public class LogComponent extends MongoDBComponent implements LogService
{
  private String logCollection;
  
  @Activate
  public void activate(Map<String, Object> properties)
  {
    logCollection = (String) properties.get("collection");
  }
  
  @Override
  public List<JSONObject> getLogEntries(String query)
  {
    DBObject dbQuery = (DBObject) JSON.parse(query);
    DBCollection collection = getCollection(logCollection);
    DBCursor results = collection.find(dbQuery);
    
    ArrayList<JSONObject> logEntries = new ArrayList<>();
    
    while(results.hasNext())
      logEntries.add(new JSONObject(results.next().toMap()));
    
    return logEntries;
  }
}
