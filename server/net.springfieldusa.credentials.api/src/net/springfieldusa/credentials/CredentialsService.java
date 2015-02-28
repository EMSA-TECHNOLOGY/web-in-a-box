package net.springfieldusa.credentials;

import java.security.Principal;

import net.springfieldusa.password.EncryptionException;

public interface CredentialsService
{
  void addCredential(Credential credential) throws EncryptionException;

  Principal authenticate(String email, String password) throws EncryptionException;
  boolean authorize(Principal principal, String role);
}
