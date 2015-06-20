package net.springfieldusa.credentials;

import java.security.Principal;

public interface CredentialsService
{
  void addCredential(Credential credential) throws CredentialException;
  
  Principal authenticate(Credential credential) throws CredentialException;
  boolean authorize(Principal principal, String role) throws CredentialException;
}
