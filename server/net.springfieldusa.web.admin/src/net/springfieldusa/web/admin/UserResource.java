package net.springfieldusa.web.admin;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.*;

import net.springfieldusa.users.UserService;

@Path("/admin/users")
@RolesAllowed("admin")
@Component(service = Object.class)
public class UserResource {
  private volatile UserService userService;
  
  @POST
  public JSONObject addUser(JSONObject user)
  {
    try
    {
      return userService.addUser(user);
    }
    catch (JSONException e)
    {
      throw new InternalServerErrorException("Failed to add user");
    }
  }
  
  @DELETE
  @Path("{id}")
  public Response deleteUser(@PathParam("id") String userId)
  {
    userService.removeUser(userId);
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Reference(unbind = "-")
  public void bindUserService(UserService userService)
  {
    this.userService = userService;
  }
}
