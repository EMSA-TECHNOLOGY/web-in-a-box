package net.springfieldusa.security.comp;

import java.security.Principal;

import net.springfieldusa.comp.AbstractComponent;
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
  public Principal authenticate(String email, String password)
  {
    try
    {
      return credentialsService.authenticate(email, password);
    }
    catch (EncryptionException e)
    {
      log(LogService.LOG_ERROR, "Exception occured when attempting to authenticate user: '" + email + "'");
      return null;
    }
  }
  
  @Override
  public boolean authorize(Principal principal, String role)
  {
    return true; // FIXME
  }
  
  @Reference(unbind="-")
  public void bindCredentialService(CredentialsService credentialsService)
  {
    this.credentialsService = credentialsService;
  }
}
