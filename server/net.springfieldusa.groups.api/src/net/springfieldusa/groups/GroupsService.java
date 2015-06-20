package net.springfieldusa.groups;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public interface GroupsService
{
  JSONObject addGroup(JSONObject group) throws JSONException;
  JSONObject getGroup(String groupId) throws JSONException;
  void removeGroup(String groupId);
  
  Set<String> getUsersInGroup(String group) throws JSONException;
  Set<String> getGroupsFor(String user) throws JSONException;
}
