package net.springfieldusa.web.admin;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.log.LogService;

@Path("/admin/logs")
@RolesAllowed("admin")
@Component(service = Object.class)
public class LogResource {
  private volatile LogService logService;
  
  @GET
  public Collection<JSONObject> getLog(@Context HttpServletRequest request)
  {
    try
    {
      return logService.getLogEntries(request.getQueryString());
    }
    catch (JSONException e)
    {
      throw new InternalServerErrorException("Failed to retrieve log entries");
    }
  }
  
  @Reference(unbind = "-")
  public void bindLogService(LogService logService)
  {
    this.logService = logService;
  }
}
