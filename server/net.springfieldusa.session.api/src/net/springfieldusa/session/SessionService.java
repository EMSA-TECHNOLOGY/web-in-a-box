package net.springfieldusa.session;

import java.security.Principal;

import net.springfieldusa.credentials.Credential;

public interface SessionService {
  Principal getPrincipal(String sessionToken);
  String createSessionToken(Credential credential);
  void expireSessionToken(String sessionToken);
}
