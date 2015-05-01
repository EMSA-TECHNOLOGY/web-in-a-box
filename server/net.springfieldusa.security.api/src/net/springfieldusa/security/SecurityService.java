package net.springfieldusa.security;

import java.security.Principal;

import net.springfieldusa.credentials.Credential;

public interface SecurityService
{
  Principal authenticate(Credential credentials);
  boolean authorize(Principal principal, String role);
}
