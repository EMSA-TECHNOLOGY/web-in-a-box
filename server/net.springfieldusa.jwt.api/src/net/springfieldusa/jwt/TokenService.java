package net.springfieldusa.jwt;

import java.util.Map;

import net.springfieldusa.credentials.Credential;

public interface TokenService
{
  String createToken(Credential credentials);
  Map<String, Object> verifyToken(String token);
}
