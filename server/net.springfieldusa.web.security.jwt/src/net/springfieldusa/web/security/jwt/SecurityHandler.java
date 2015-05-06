package net.springfieldusa.web.security.jwt;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import net.springfieldusa.jwt.TokenService;

@Component(service= {AuthenticationHandler.class, AuthorizationHandler.class})
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler
{
  private volatile TokenService tokenService;
  
  @Override
  public boolean isUserInRole(Principal user, String role)
  {
    if(!(user instanceof User))
      return false;
    
    return ((User) user).isInRole(role);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Principal authenticate(ContainerRequestContext requestContext)
  {
    String authHeader = requestContext.getHeaderString("Authorization");
    
    if(authHeader == null || !authHeader.startsWith("Bearer "))
      return null;
    
    Map<String, Object> claims = tokenService.verifyToken(authHeader.substring(7));
    
    if(claims == null || claims.isEmpty())
      return null;
    
    return new User((String) claims.get("userId"), (Collection<String>) claims.get("roles"));
  }

  @Override
  public String getAuthenticationScheme()
  {
    return "TOKEN";
  }
  
  @Reference(unbind = "-")
  public void bindTokenService(TokenService tokenService)
  {
    this.tokenService = tokenService;
  }
}
