package net.springfieldusa.security.comp;

import java.security.Principal;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.CredentialsService;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.security.SecurityService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

@Component(service=SecurityService.class)
public class SecurityComponent extends AbstractComponent implements SecurityService
{
  private CredentialsService credentialsService;
  
  @Override
  public Principal authenticate(Credential credentials)
  {
    try
    {
      return credentialsService.authenticate(credentials);
    }
    catch (EncryptionException e)
    {
      log(LogService.LOG_ERROR, "Exception occured when attempting to authenticate user: '" + credentials.getUserId() + "'");
      return null;
    }
  }
  
  @Override
  public boolean authorize(Principal principal, String role)
  {
    return credentialsService.authorize(principal, role);
  }
  
  @Reference(unbind="-")
  public void bindCredentialService(CredentialsService credentialsService)
  {
    this.credentialsService = credentialsService;
  }
}
