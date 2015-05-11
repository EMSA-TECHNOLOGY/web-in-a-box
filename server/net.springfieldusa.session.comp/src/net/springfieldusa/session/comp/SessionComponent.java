package net.springfieldusa.session.comp;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.security.SecurityService;
import net.springfieldusa.session.SessionService;

@Component(service = SessionService.class)
public class SessionComponent extends AbstractComponent implements SessionService
{
  // TODO: add a timestamp to the session token so that they can be
  // expired after a configured timeout
  
  private Map<String, Principal> sessionsByToken = new ConcurrentHashMap<>();
  private Map<String, String> sessionsByUser = new ConcurrentHashMap<>();
  private volatile SecurityService securityService;
  
  @Override
  public Principal getPrincipal(String sessionToken)
  {
    return sessionsByToken.get(sessionToken);
  }

  @Override
  public String createSessionToken(Credential credential)
  {
    // TODO: check for a race condition when a request for a new token
    // is received for the same user while a previous request is still pending
    
    if(credential == null)
      return null;
    
    Principal principal = securityService.authenticate(credential);
    
    if(principal == null)
      return null;
      
    String sessionToken = UUID.randomUUID().toString();
    String previousSessionToken = sessionsByUser.put(credential.getUserId(), sessionToken);
    
    if(previousSessionToken != null)
      sessionsByToken.remove(previousSessionToken);

    sessionsByToken.put(sessionToken, principal);
    return sessionToken;
  }

  @Override
  public void expireSessionToken(String sessionToken)
  {
    Principal principal = sessionsByToken.remove(sessionToken);
    
    if(principal != null)
      sessionsByUser.remove(principal.getName());
  }
  
  @Reference(unbind = "-")
  public void bindSecurityService(SecurityService securityService)
  {
    this.securityService = securityService;
  }
}
