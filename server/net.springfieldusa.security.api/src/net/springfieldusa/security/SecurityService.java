package net.springfieldusa.security;

import java.security.Principal;
import java.util.Set;

import net.springfieldusa.credentials.Credential;

public interface SecurityService
{
  Principal authenticate(Credential credentials);
  boolean authorize(Principal principal, String role);
  Set<String> getRoles(Principal principal);
}
