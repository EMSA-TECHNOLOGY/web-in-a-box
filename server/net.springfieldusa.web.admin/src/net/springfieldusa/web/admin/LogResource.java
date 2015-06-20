package net.springfieldusa.web.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.log.LogService;

@Path("/admin/logs")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@Component(service = Object.class)
public class LogResource {
  private volatile LogService logService;
  
  @GET
  public JSONObject getLog(@Context HttpServletRequest request)
  {
    try
    {
      JSONArray logEntries = logService.getLogEntries(URLDecoder.decode(request.getQueryString(), "UTF-8"));
      JSONObject result = new JSONObject().put("logEntries", logEntries);
      return result;
    }
    catch (JSONException | UnsupportedEncodingException e)
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
