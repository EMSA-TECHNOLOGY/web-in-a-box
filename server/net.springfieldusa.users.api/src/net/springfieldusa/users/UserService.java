package net.springfieldusa.users;

import org.json.JSONObject;

public interface UserService
{
  JSONObject addUser(JSONObject user);
  void removeUser(String userId);
}
