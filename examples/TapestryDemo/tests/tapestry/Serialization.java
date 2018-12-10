package tests.tapestry;

import com.primix.tapestry.app.SimpleApplication;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.io.*;
import com.primix.foundation.*;
import com.primix.foundation.io.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

/*
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Serialization extends BasePage
{
	private String pageName;
	public Serialization(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}
	public String getCompressedApplicationDump()
	{
		StringWriter writer;
		BinaryDumpOutputStream dump;
		GZIPOutputStream gos;
		ObjectOutputStream oos;
		String result = null;

	try
	{
		writer = new StringWriter();
			dump = new BinaryDumpOutputStream(writer);
			gos = new GZIPOutputStream(dump);
			oos = new ObjectOutputStream(gos);

			oos.writeObject(application);

			oos.close();

			result = writer.getBuffer().toString();
			}
	catch (IOException e)
	{
		result = e.toString();
			}

	return result;
	}
	public String getCompressedPageRecorderDump()
	{
		StringWriter writer;
		BinaryDumpOutputStream dump;
		GZIPOutputStream gos;
		ObjectOutputStream oos;
		String result = null;

	try
	{
		writer = new StringWriter();
			dump = new BinaryDumpOutputStream(writer);
			gos = new GZIPOutputStream(dump);
			oos = new ObjectOutputStream(gos);

			oos.writeObject(application.getPageRecorder(pageName));

			oos.close();

			result = writer.getBuffer().toString();
			}
	catch (IOException e)
	{
		result = e.toString();
			}

	return result;
	}
    
	public String getPageName()
	{
		return pageName;
	}
    
	/**
	*  Returns the names of all pages.  This has the side-effect of creating
	*  page recorders for all those pages.
	*
	*/

	public Collection getPageNames()
	{
		List list;
		SimpleApplication application;

		application = (SimpleApplication)getApplication();

		list = new ArrayList(application.getActivePageNames());
        
        Collections.sort(list);
        
        return list;
	}
    
	public String getSerializedApplicationDump()
	{
		StringWriter writer;
		BinaryDumpOutputStream dump;
		ObjectOutputStream oos;
		String result = null;

		try
		{
			writer = new StringWriter();
			dump = new BinaryDumpOutputStream(writer);
			oos = new ObjectOutputStream(dump);

			oos.writeObject(application);

			oos.close();

			result = writer.getBuffer().toString();
		}
		catch (IOException e)
		{
			result = e.toString();
		}

	return result;
	}
    
	public String getSerializedPageRecorderDump()
	{
		StringWriter writer;
		BinaryDumpOutputStream dump;
		ObjectOutputStream oos;
		String result = null;

		try
		{
			writer = new StringWriter();
			dump = new BinaryDumpOutputStream(writer);
			oos = new ObjectOutputStream(dump);

			oos.writeObject(application.getPageRecorder(pageName));

			oos.close();

			result = writer.getBuffer().toString();
		}
		catch (IOException e)
		{
			result = e.toString();
		}

		return result;
	}
	    
	public void setPageName(String value)
	{
		pageName = value;
	}
}

