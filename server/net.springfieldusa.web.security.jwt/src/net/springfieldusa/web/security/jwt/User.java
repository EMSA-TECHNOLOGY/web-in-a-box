package net.springfieldusa.web.security.jwt;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User implements Principal
{
  public String name;
  public Set<String> roles;
  
  public User(String name, Collection<String> roles)
  {
    this.name = name;
    roles = roles != null? new HashSet<>(roles) : Collections.emptySet();
  }

  @Override
  public String getName()
  {
    return name;
  }
  
  public boolean isInRole(String role)
  {
    return roles.contains(role);
  }
}
