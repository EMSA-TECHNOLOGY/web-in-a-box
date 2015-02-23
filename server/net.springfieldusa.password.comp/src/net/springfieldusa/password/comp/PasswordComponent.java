/*******************************************************************************
 * Copyright (c) 2015 Bryan Hunt.  All Rights Reserved.
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

package net.springfieldusa.password.comp;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.log.LogService;

/**
 * @author bhunt
 * 
 */
@Component(service = PasswordService.class)
public class PasswordComponent extends AbstractComponent implements PasswordService
{
  private volatile int numberEncryptionIterations = 20000;
  private volatile int derivedKeyLength = 160;
  private volatile int saltByteLength = 8;
  private SecureRandom secureRandom;
  private SecretKeyFactory secretKeyFactory;

  // TODO : add activate() and set privates from the service properties

  @Activate
  public void activate() throws NoSuchAlgorithmException
  {
    try
    {
      secureRandom = createSecureRandom();
      secretKeyFactory = createSecretKeyFactory();
    }
    catch (NoSuchAlgorithmException e)
    {
      log(LogService.LOG_ERROR, "Failed to initialize password service", e);
      throw e;
    }
  }

  @Override
  public byte[] createSalt() throws EncryptionException
  {
    byte[] salt = new byte[saltByteLength];
    secureRandom.nextBytes(salt);
    return salt;
  }

  @Override
  public byte[] encryptPassword(String password, byte[] salt) throws EncryptionException
  {
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, numberEncryptionIterations, derivedKeyLength);

    try
    {
      return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }
    catch (InvalidKeySpecException e)
    {
      log(LogService.LOG_ERROR, "Failed to encrypt password", e);
      throw new SecurityException(e);
    }
  }

  @Override
  public boolean validatePassword(String password, byte[] targetPassword, byte[] salt) throws EncryptionException
  {
    byte[] enctyptedPassword = encryptPassword(password, salt);
    return Arrays.equals(enctyptedPassword, targetPassword);
  }

  protected SecretKeyFactory createSecretKeyFactory() throws NoSuchAlgorithmException
  {
    return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  }

  protected SecureRandom createSecureRandom() throws NoSuchAlgorithmException
  {
    return SecureRandom.getInstance("SHA1PRNG");
  }
}
