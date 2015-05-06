package net.springfieldusa.web.security.jwt;

import javax.ws.rs.Consumes;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.jwt.TokenService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component(service = Object.class)
public class TokenResource 
{
  private volatile TokenService tokenService;
  
  @POST
  public Token createToken(Credential credentials)
  {
    String token = tokenService.createToken(credentials);
    
    if(token == null)
      throw new NotAuthorizedException("Token");
    
    return new Token(token);
  }
  
  @Reference(unbind = "-")
  public void bindTokenService(TokenService tokenService)
  {
    this.tokenService = tokenService;
  }
}
