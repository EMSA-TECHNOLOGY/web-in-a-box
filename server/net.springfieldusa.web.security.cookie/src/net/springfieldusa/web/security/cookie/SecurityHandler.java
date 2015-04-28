package net.springfieldusa.web.security.cookie;

import java.security.Principal;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;

import net.springfieldusa.security.SecurityService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

@Component(service= {AuthenticationHandler.class, AuthorizationHandler.class})
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler
{
  private static final String PASSWORD_COOKIE = "password";
  private static final String USER_COOKIE = "user";
  
  private SecurityService securityService;
  
  @Override
  public boolean isUserInRole(Principal user, String role)
  {
    return securityService.authorize(user, role);
  }

  @Override
  public Principal authenticate(ContainerRequestContext requestContext)
  {
    Map<String, Cookie> cookies = requestContext.getCookies();
    Cookie userCookie = cookies.get(USER_COOKIE);
    Cookie passwordCookie = cookies.get(PASSWORD_COOKIE);
    
    if(userCookie != null && passwordCookie != null)
      return securityService.authenticate(userCookie.getValue(), passwordCookie.getValue());
    
    return null;
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
