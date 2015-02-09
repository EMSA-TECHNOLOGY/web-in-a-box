package net.springfieldusa.security;

import java.security.Principal;

public interface SecurityService
{
  Principal authenticate(String email, String password);
  boolean authorize(Principal principal, String role);
}
