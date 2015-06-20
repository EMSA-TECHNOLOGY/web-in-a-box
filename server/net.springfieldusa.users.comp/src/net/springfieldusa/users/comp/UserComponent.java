package net.springfieldusa.users.comp;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.storage.StorageService;
import net.springfieldusa.users.UserService;

@Component(service = UserService.class)
public class UserComponent extends AbstractComponent implements UserService 
{
  private static final String USERS = "users";
  private volatile StorageService storageService;
  
  @Override
  public JSONObject addUser(JSONObject user) throws JSONException
  {
    return storageService.create(USERS, user);
  }

  @Override
  public void removeUser(String userId)
  {
    storageService.delete(USERS, userId);
  }
  
  @Reference(unbind = "-")
  public void bindStorageService(StorageService storageService)
  {
    this.storageService = storageService;
  }
}
