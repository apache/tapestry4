/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.contrib.mckoi;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.util.ServiceMBeanSupport;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import java.util.ResourceBundle;

import com.mckoi.runtime.BootMain;

/**
 *  An MBean used to start and stop and embedded instance of
 *  <a href="http://www.mckoi.com/database">McKoi Database</a>.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 **/

public class McKoiDB 
extends ServiceMBeanSupport 
implements McKoiDBMBean
{
	private String rootPath;
	private String configPath;
	
	public String getRootPath()
	{
		return rootPath;
	}

	public void setRootPath(String path)
	{
		rootPath = path;
	}

	public String getConfigPath()
	{
		return configPath;
	}

	public void setConfigPath(String path)
	{
		configPath = path;
	}

	public ObjectName preRegister(MBeanServer server, ObjectName name)
		throws Exception
	{
		if (name != null)
			return name;
			
		return new ObjectName(":service=McKoiDB");
	}

	public String getName()
	{
		return "McKoiDB";
	}

	public void startService() throws Exception
	{
		if (rootPath == null)
			throw new NullPointerException("McKoiDB: rootPath not specified.");
			
		if (configPath == null)
			throw new NullPointerException("McKoiDB: configPath not specified.");
			
		log.debug("Root path: " + rootPath);
		log.debug("Config path: " + configPath);
		
		File file = new File(rootPath);
		InputStream stream = new FileInputStream(configPath);
		ResourceBundle bundle = new PropertyResourceBundle(stream);
		
		BootMain.boot(file, bundle);
	}

	public void stopService()
	{
		BootMain.shutdown();
	}

}
