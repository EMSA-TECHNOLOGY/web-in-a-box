package net.springfieldusa.security.comp;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import org.json.JSONException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.CredentialException;
import net.springfieldusa.credentials.CredentialsService;
import net.springfieldusa.groups.GroupsService;
import net.springfieldusa.security.SecurityService;

@Component(service=SecurityService.class)
public class SecurityComponent extends AbstractComponent implements SecurityService
{
  private CredentialsService credentialsService;
  private GroupsService groupsService;
  
  @Override
  public Principal authenticate(Credential credentials)
  {
    log(LogService.LOG_DEBUG, "Authenticating user: '" + credentials.getUserId() + "'");
    
    try
    {
      return credentialsService.authenticate(credentials);
    }
    catch (CredentialException e)
    {
      log(LogService.LOG_ERROR, "Exception occured when attempting to authenticate user: '" + credentials.getUserId() + "'");
      return null;
    }
  }
  
  @Override
  public boolean authorize(Principal principal, String role)
  {
    try
    {
      return credentialsService.authorize(principal, role);
    }
    catch (CredentialException e)
    {
      log(LogService.LOG_ERROR, "Exception occured when attempting to authorize user: '" + principal.getName() + "' for role: '" + role + "'");
      return false;
    }
  }
  
  @Override
  public Set<String> getRoles(Principal principal)
  {
    try
    {
      return groupsService.getGroupsFor(principal.getName());
    }
    catch (JSONException e)
    {
      log(LogService.LOG_ERROR, "Exception occured when attempting to get groups for user: '" + principal.getName() + "'");
      return Collections.emptySet();
    }
  }

  @Reference(unbind="-")
  public void bindCredentialService(CredentialsService credentialsService)
  {
    this.credentialsService = credentialsService;
  }

  @Reference(unbind="-")
  public void bindGroupsService(GroupsService groupsService)
  {
    this.groupsService = groupsService;
  }
}
