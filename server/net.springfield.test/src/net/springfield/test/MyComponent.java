package net.springfield.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component(service=MyComponent.class)
@Path("/storage/data/{collection}")
@Produces("application/json")
@Consumes("application/json")
public class MyComponent
{
  private MongoDatabaseProvider mongoDatabaseProvider;
  
  @GET
  public String activate()
  {
    return "Hello REST world";
  }
  
  @POST
  public Response addData(@PathParam("collection") String collectionName, String json)
  {
    DBCollection collection = getCollection(collectionName);
    DBObject data = (DBObject) JSON.parse(json);
    collection.insert(data);
    return Response.ok(data.toString(), MediaType.APPLICATION_JSON).build();
  }
  
  @Reference(unbind = "-")
  public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
  {
    this.mongoDatabaseProvider = mongoDatabaseProvider;
  }

  private DBCollection getCollection(String collectionName)
  {
    return mongoDatabaseProvider.getDB().getCollection(collectionName);
  }
}
