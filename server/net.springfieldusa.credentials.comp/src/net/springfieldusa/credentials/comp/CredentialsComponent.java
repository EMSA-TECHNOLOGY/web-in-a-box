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

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.CredentialsService;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;

import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author bhunt
 * 
 */
@Component(service = CredentialsService.class)
public class CredentialsComponent implements CredentialsService
{
	private static final String KEY_UPDATED_ON = "updatedOn";
  private static final String KEY_PASSWORD = "password";
  private static final String KEY_SALT = "salt";
  private static final String KEY_EMAIL = "email";
  private static final String CREDENTIALS = "credentials";
  
	private volatile MongoDatabaseProvider credentialsDatabaseProvider;
	private volatile PasswordService passwordService;

	@Override
	public void addCredential(Credential credential) throws EncryptionException
	{
		// TODO : See if the user is already registered or has a registration pending

		byte[] salt = passwordService.createSalt();

		DBObject data = new BasicDBObject();
		data.put(KEY_EMAIL, credential.getEmail());
		data.put(KEY_SALT, salt);
		data.put(KEY_PASSWORD, passwordService.encryptPassword(credential.getPassword(), salt));
		data.put(KEY_UPDATED_ON, new Date());

		credentialsDatabaseProvider.getDB().getCollection(CREDENTIALS).insert(data);
	}

  @Override
  public Principal authenticate(String email, String password) throws EncryptionException
  {
    DBObject filter = new BasicDBObject();
    filter.put(KEY_EMAIL, email);
    DBObject credential = credentialsDatabaseProvider.getDB().getCollection(CREDENTIALS).findOne(filter);
    
    if(passwordService.validatePassword(password, (byte[]) credential.get(KEY_PASSWORD), (byte[]) credential.get(KEY_SALT)))
      return new User(email);
    
    return null;
  }

	@Reference(unbind = "-", target = "(alias=creds)")
	public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
	{
		this.credentialsDatabaseProvider = mongoDatabaseProvider;
	}

	@Reference(unbind = "-")
	public void bindPasswordService(PasswordService passswordService)
	{
		this.passwordService = passswordService;
	}
}
