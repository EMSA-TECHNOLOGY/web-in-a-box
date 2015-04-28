package net.springfieldusa.web.security.basic;

import java.security.Principal;
import java.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import net.springfieldusa.security.SecurityService;

@Component(service= {AuthenticationHandler.class, AuthorizationHandler.class})
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler
{
  private SecurityService securityService;
  
  @Override
  public boolean isUserInRole(Principal user, String role)
  {
    return securityService.authorize(user, role);
  }

  @Override
  public Principal authenticate(ContainerRequestContext requestContext)
  {
    String authHeader = requestContext.getHeaderString("Authorization");
    
    if(!authHeader.startsWith("Basic "))
      return null;
    
    String[] credentials = new String(Base64.getDecoder().decode(authHeader.substring(6))).split(":");
    
    if(credentials.length != 2 || credentials[0].isEmpty() || credentials[1].isEmpty())
      return null;
    
    return securityService.authenticate(credentials[0], credentials[1]);
  }

  @Override
  public String getAuthenticationScheme()
  {
    return null;
  }
  
  @Reference(unbind="-")
  public void bindSecurityService(SecurityService securityService)
  {
    this.securityService = securityService;
  }
}
