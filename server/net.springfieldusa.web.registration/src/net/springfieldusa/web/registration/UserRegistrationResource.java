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

package net.springfieldusa.web.registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.registration.RegistrationException;
import net.springfieldusa.registration.UserRegistrationService;

/**
 * @author bhunt
 * 
 */
@Path("/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component(service = Object.class)
public class UserRegistrationResource
{
	private volatile UserRegistrationService userRegistrationService;

	@GET
	public Credential getSample()
	{
		return new Credential("mail@domain.com", "password");
	}

	@POST
	public Response registerUser(Credential credential)
	{
		  try
      {
        userRegistrationService.registerUser(credential);
        String result = "ok";
        return Response.status(Status.CREATED).entity(result).build();
      }
      catch (RegistrationException e)
      {
        throw new InternalServerErrorException("Failed to register user");
      }
	}

	@Reference(unbind = "-")
	public void bindUserRegistrationService(UserRegistrationService userRegistrationService)
	{
		this.userRegistrationService = userRegistrationService;
	}
}
