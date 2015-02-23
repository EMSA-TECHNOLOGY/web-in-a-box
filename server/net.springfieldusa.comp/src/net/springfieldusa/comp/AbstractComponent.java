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

package net.springfieldusa.comp;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

/**
 * @author bhunt
 * 
 */
public abstract class AbstractComponent
{
	private AtomicReference<LogService> logServiceReference = new AtomicReference<>();

	@Reference
	public void bindLogService(LogService logService)
	{
		logServiceReference.set(logService);
	}

	public void unbindLogService(LogService logService)
	{
		logServiceReference.compareAndSet(logService, null);
	}

	protected void log(int level, String message)
	{
		log(level, message, null);
	}

	protected void log(int level, String message, Throwable exception)
	{
		LogService logService = logServiceReference.get();

		if (logService != null)
			logService.log(level, message, exception);
	}
}
