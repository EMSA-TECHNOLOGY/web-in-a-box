package net.springfieldusa.web.admin;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.log.LogService;

@Path("/admin/logs")
@RolesAllowed("admin")
@Component(service = Object.class)
public class LogResource {
  private volatile LogService logService;
  
  @GET
  public Response deleteUser(@PathParam("id") String userId)
  {
    logService.getLogEntries(userId);
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Reference(unbind = "-")
  public void bindLogService(LogService logService)
  {
    this.logService = logService;
  }
}
