package net.springfieldusa.security;

import java.security.Principal;
import java.util.Collection;

import net.springfieldusa.credentials.Credential;

public interface SecurityService
{
  Principal authenticate(Credential credentials);
  boolean authorize(Principal principal, String role);
  Collection<String> getRoles(Principal principal);
}
