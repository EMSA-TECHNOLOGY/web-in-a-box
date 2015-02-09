package net.springfieldusa.credentials.comp;

import java.security.Principal;

public class User implements Principal
{
  private String name;
  
  public User(String name)
  {
    if(name == null)
      throw new IllegalArgumentException("name cannot be null");
    
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public int hashCode()
  {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    return name.equals(obj);
  }

  @Override
  public String toString()
  {
    return name;
  }
}
