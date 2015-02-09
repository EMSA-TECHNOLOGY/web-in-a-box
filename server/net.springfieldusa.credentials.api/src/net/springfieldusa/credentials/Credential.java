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

package net.springfieldusa.credentials;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author bhunt
 * 
 */
@XmlRootElement
public class Credential
{
	private String email;
	private String password;

	/**
	 * Constructs an empty Credential instance.
	 */
	public Credential()
	{
		super();
	}

	/**
	 * Constructs a Credential instance from an email and password.
	 * 
	 * @param email
	 * @param password
	 */
	public Credential(String email, String password)
	{
		this.email = email;
		this.password = password;
	}

	/**
	 * Retrieves the email portion of the credential.
	 * 
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
   * Retrieves the email portion of the credential.
   * 
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Changes the credential email field.
	 * 
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Changes the credential password field.
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
}
