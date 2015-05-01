package net.springfieldusa.web.session;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.*;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.session.SessionService;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component(service = Object.class)
public class SessionResource {
  private static final String SESSION_COOKIE = "session";

  private volatile SessionService sessionService;
  
  @POST
  public Response createSession(Credential credential)
  {
    String sessionToken = sessionService.createSessionToken(credential);
    
    if(sessionToken == null)
      throw new NotAuthorizedException("Form");
    
    URI location;
    try
    {
      location = new URI(sessionToken);
      return Response.created(location).cookie(new NewCookie(SESSION_COOKIE, sessionToken)).build();
    }
    catch (URISyntaxException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @DELETE
  public Response expireSession(@CookieParam(SESSION_COOKIE) String sessionToken)
  {
    sessionService.expireSessionToken(sessionToken);
    return Response.ok().cookie(new NewCookie(SESSION_COOKIE, "")).build();
  }
  
  @Reference(unbind = "-")
  public void bindSessionService(SessionService sessionService) 
  {
    this.sessionService = sessionService;
  }
}
