package net.springfieldusa.users;

import org.json.JSONException;
import org.json.JSONObject;

public interface UserService
{
  JSONObject addUser(JSONObject user) throws JSONException;
  void removeUser(String userId);
}
