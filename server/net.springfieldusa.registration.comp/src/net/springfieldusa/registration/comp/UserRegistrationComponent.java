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

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.registration.UserRegistrationService;

import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author bhunt
 * 
 */
@Component(service = UserRegistrationService.class)
public class UserRegistrationComponent implements UserRegistrationService
{
	private static final String REGISTRATIONS = "registrations";
	private volatile MongoDatabaseProvider mongoDatabaseProvider;
	private volatile PasswordService passwordService;

	@Override
	public void registerUser(Credential userRegistration) throws EncryptionException
	{
		// TODO : See if the user is already registered or has a registration pending

		byte[] salt = passwordService.createSalt();

		DBObject data = new BasicDBObject();
		data.put("email", userRegistration.getEmail());
		data.put("salt", salt);
		data.put("password", passwordService.encryptPassword(userRegistration.getPassword(), salt));
		data.put("registeredOn", new Date());

		mongoDatabaseProvider.getDB().getCollection(REGISTRATIONS).insert(data);
	}

	@Reference(unbind = "-", target = "(alias=data)")
	public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
	{
		this.mongoDatabaseProvider = mongoDatabaseProvider;
	}

	@Reference(unbind = "-")
	public void bindSecurityService(PasswordService passwordService)
	{
		this.passwordService = passwordService;
	}
}
