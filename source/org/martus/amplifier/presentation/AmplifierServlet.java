package org.martus.amplifier.presentation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.servlet.VelocityServlet;

public class AmplifierServlet extends VelocityServlet
{
	/**
	 *   Called by the VelocityServlet
	 *   init().  We want to set a set of properties
	 *   so that templates will be found in the webapp
	 *   root.  This makes this easier to work with as 
	 *   an example, so a new user doesn't have to worry
	 *   about config issues when first figuring things
	 *   out
	 */
	protected Properties loadConfiguration(ServletConfig config )
		throws IOException, FileNotFoundException
	{
		Properties p = new Properties();

		/*
		 *  first, we set the template path for the
		 *  FileResourceLoader to the root of the 
		 *  webapp.  This probably won't work under
		 *  in a WAR under WebLogic, but should 
		 *  under tomcat :)
		 */

		String path = config.getServletContext().getRealPath("/");

		if (path == null)
		{
			System.out.println(" SampleServlet.loadConfiguration() : unable to " 
							   + "get the current webapp root.  Using '/'. Please fix.");

			path = "/";
		}

		p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH,  path );

		/**
		 *  and the same for the log file
		 */

		p.setProperty( "runtime.log", path + "velocity.log" );

		return p;
	}

	protected Template loadTemplate(String templateName)
	{
		try
		{
			return getTemplate(templateName);
		}
		catch( ParseErrorException e )
		{
			displayError("parse error for template", e);
		}
		catch( ResourceNotFoundException e )
		{
			displayError("template not found", e);
		}
		catch( Exception e )
		{
			displayError("Unknown error", e);
		}
		return null;
	}

	protected void displayError(String message, Exception e)
	{
		System.out.println(getClass().getName() + ": " + message + " " + e);
	}


}
