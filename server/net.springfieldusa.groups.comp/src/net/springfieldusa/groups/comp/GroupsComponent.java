package net.springfieldusa.groups.comp;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.groups.GroupsService;
import net.springfieldusa.storage.StorageService;

@Component
public class GroupsComponent extends AbstractComponent implements GroupsService
{
  private static final String GROUPS = "groups";
  private static final String KEY_GROUP_NAME = "name";
  private static final String KEY_GROUP_MEMBERS = "members";

  private volatile StorageService storageService;
  
  @Activate
  public void activate() throws JSONException
  {
    if(storageService.retrieve(GROUPS, KEY_GROUP_NAME, "admin") == null)
    {
      JSONArray admins = new JSONArray();
      admins.put("admin");
      JSONObject group = new JSONObject();
      group.put(KEY_GROUP_NAME, "admin");
      group.put(KEY_GROUP_MEMBERS, admins);
      storageService.create(GROUPS, group);
    }
  }
  
  @Override
  public JSONObject addGroup(JSONObject group) throws JSONException
  {
    return storageService.create(GROUPS, group);
  }

  @Override
  public JSONObject getGroup(String group) throws JSONException
  {
    return storageService.retrieve(GROUPS, KEY_GROUP_NAME, group);
  }

  @Override
  public void removeGroup(String group)
  {
    storageService.delete(GROUPS, group);
  }

  @Override
  public Set<String> getGroupsFor(String user) throws JSONException
  {
    JSONObject query = new JSONObject().put(KEY_GROUP_MEMBERS, user);
    Collection<JSONObject> results = storageService.find(GROUPS, query.toString());

    HashSet<String> groups = new HashSet<>();

    for (JSONObject item : results)
      groups.add((String) item.get(KEY_GROUP_NAME));

    return groups;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<String> getUsersInGroup(String group) throws JSONException
  {
    JSONObject result = storageService.retrieve(GROUPS, KEY_GROUP_NAME, group);
    List<String> members = (List<String>) result.get(KEY_GROUP_MEMBERS);
    return new HashSet<String>(members);
  }
  
  @Reference(unbind = "-")
  public void bindStorageService(StorageService storageService)
  {
    this.storageService = storageService;
  }
}
