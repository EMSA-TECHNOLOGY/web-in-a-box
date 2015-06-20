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

package net.springfieldusa.registration.comp;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.registration.RegistrationException;
import net.springfieldusa.registration.UserRegistrationService;
import net.springfieldusa.storage.StorageService;


/**
 * @author bhunt
 * 
 */
@Component(service = UserRegistrationService.class)
public class UserRegistrationComponent extends AbstractComponent implements UserRegistrationService
{
	private static final String REGISTRATIONS = "registrations";
	private volatile PasswordService passwordService;
	private volatile StorageService storageService;

	@Override
	public void registerUser(Credential userRegistration) throws RegistrationException
	{
		// TODO : See if the user is already registered or has a registration pending

		try
    {
		  byte[] salt = passwordService.createSalt();
      JSONObject data = new JSONObject();
      data.put("userId", userRegistration.getUserId());
      data.put("salt", salt);
      data.put("password", passwordService.encryptPassword(userRegistration.getPassword(), salt));
      data.put("registeredOn", new Date());

      log(LogService.LOG_DEBUG, "Registering user: '" + userRegistration.getUserId() + "'");
      storageService.create(REGISTRATIONS, data);
    }
    catch (JSONException | EncryptionException e)
    {
      throw new RegistrationException(e);
    }
	}

	@Reference(unbind = "-")
	public void bindPasswordService(PasswordService passwordService)
	{
		this.passwordService = passwordService;
	}
	
	public void bindStorageService(StorageService storageService)
	{
	  this.storageService = storageService;
	}
}
