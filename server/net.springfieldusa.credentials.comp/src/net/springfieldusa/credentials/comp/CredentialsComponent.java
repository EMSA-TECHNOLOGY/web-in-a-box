/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt.  All Rights Reserved.
 *
 * Bryan Hunt CONFIDENTIAL
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Bryan Hunt and its suppliers, if any.  The 
 * intellectual and technical concepts contained herein are 
 * proprietary to Bryan Hunt and its suppliers and may be covered 
 * by U.S. and Foreign Patents, patents in process, and are 
 * protected by trade secret or copyright law.  Dissemination of 
 * this information or reproduction of this material is strictly 
 * forbidden unless prior written permission is obtained from 
 * Bryan Hunt.
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package net.springfieldusa.credentials.comp;

import java.security.Principal;
import java.util.Date;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.CredentialException;
import net.springfieldusa.credentials.CredentialsService;
import net.springfieldusa.groups.GroupsService;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.storage.StorageService;

/**
 * @author bhunt
 * 
 */
@Component(service = CredentialsService.class)
public class CredentialsComponent extends AbstractComponent implements CredentialsService
{
	private static final String KEY_UPDATED_ON = "updatedOn";
  private static final String KEY_PASSWORD = "password";
  private static final String KEY_SALT = "salt";
  private static final String KEY_USER_ID = "userId";
  private static final String CREDENTIALS = "credentials";
  
	private volatile PasswordService passwordService;
	private volatile GroupsService groupsService;
	private volatile StorageService storageService;

	@Activate
	public void activate() throws JSONException, CredentialException
	{
	  if(storageService.retrieve(CREDENTIALS, KEY_USER_ID, "admin") == null)
	    addCredential(new Credential("admin", "admin"));
	}
	
	@Override
	public void addCredential(Credential credential) throws CredentialException
	{
		// TODO : See if the user is already registered or has a registration pending

		try
    {
      byte[] salt = passwordService.createSalt();

      JSONObject data = new JSONObject();
      data.put(KEY_USER_ID, credential.getUserId());
      data.put(KEY_SALT, salt);
      data.put(KEY_PASSWORD, passwordService.encryptPassword(credential.getPassword(), salt));
      data.put(KEY_UPDATED_ON, new Date());

      storageService.create(CREDENTIALS, data);
    }
    catch (EncryptionException | JSONException e)
    {
      throw new CredentialException(e);
    }
	}

  @Override
  public Principal authenticate(Credential credential) throws CredentialException
  {
    try
    {
      JSONObject storedCredential = storageService.retrieve(CREDENTIALS, KEY_USER_ID, credential.getUserId());
      
      if(storedCredential == null)
        return null;
      
      JSONArray jsonPassword = storedCredential.getJSONArray(KEY_PASSWORD);
      JSONArray jsonSalt = storedCredential.getJSONArray(KEY_SALT);
      
      byte[] password = new byte[jsonPassword.length()];
      byte[] salt = new byte[jsonSalt.length()];
      
      for(int i = 0; i < password.length; i++)
        password[i] = (byte) jsonPassword.optInt(i);
      
      for(int i = 0; i < salt.length; i++)
        salt[i] = (byte) jsonSalt.optInt(i);
      
      if(passwordService.validatePassword(credential.getPassword(), password, salt))
        return new User(credential.getUserId());
      
      return null;
    }
    catch (EncryptionException | JSONException e)
    {
      throw new CredentialException(e);
    }
  }

	@Override
  public boolean authorize(Principal principal, String role) throws CredentialException
  {
	  Set<String> groups;
    try
    {
      groups = groupsService.getUsersInGroup(role);
      return groups.contains(principal.getName());
    }
    catch (Exception e)
    {
      throw new CredentialException(e);
    }
  }

	@Reference(unbind = "-")
	public void bindPasswordService(PasswordService passwordService)
	{
		this.passwordService = passwordService;
	}
	
	@Reference(unbind = "-")
	public void bindGroupService(GroupsService groupsService)
	{
	  this.groupsService = groupsService;
	}
	
	@Reference(unbind = "-")
	public void bindStorageService(StorageService storageService)
	{
	  this.storageService = storageService;
	}
}
