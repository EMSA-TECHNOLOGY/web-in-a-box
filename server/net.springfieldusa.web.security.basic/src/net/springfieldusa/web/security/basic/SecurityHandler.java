package net.springfieldusa.web.security.basic;

import java.security.Principal;
import java.util.Base64;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.security.SecurityService;

@Component(service= {AuthenticationHandler.class, AuthorizationHandler.class})
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler
{
  private volatile SecurityService securityService;
  
  @Override
  public boolean isUserInRole(Principal user, String role)
  {
    return securityService.authorize(user, role);
  }

  @Override
  public Principal authenticate(ContainerRequestContext requestContext)
  {
    String authHeader = requestContext.getHeaderString("Authorization");
    
    if(authHeader == null || !authHeader.startsWith("Basic "))
      throw new NotAuthorizedException("Basic");
    
    String[] credentials = new String(Base64.getDecoder().decode(authHeader.substring(6))).split(":");
    
    if(credentials.length != 2 || credentials[0].isEmpty() || credentials[1].isEmpty())
      throw new NotAuthorizedException("Basic");
    
    Principal principal = securityService.authenticate(new Credential(credentials[0], credentials[1]));
    
    if(principal == null)
      throw new NotAuthorizedException("Basic");
    
    return principal;
  }

  @Override
  public String getAuthenticationScheme()
  {
    return "BASIC";
  }
  
  @Reference(unbind="-")
  public void bindSecurityService(SecurityService securityService)
  {
    this.securityService = securityService;
  }
}
