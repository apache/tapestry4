package com.primix.tapestry.app;

import javax.servlet.http.*;
import com.primix.tapestry.components.*;
import java.io.IOException;
import javax.servlet.*;
import com.primix.tapestry.record.*;
import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.pageload.*;
import java.io.*;
import javax.servlet.*;
import com.primix.foundation.*;


/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Concrete implementation of {@link IApplication} used for relatively
 *  small applications.  All page state information is maintained in memory.  Since
 * the instance is stored within the {@link HttpSession}, all page state information
 * will be carried along to other servers in the cluster.
 *
 * <p>TBD:  A custom serialization method could be very useful.  It seems to me that
 * in a real application, the page recorders
 * ({@link SimplePageRecorder}) will be the only large objects in the
 * session.  They are also mostly Strings, and will compress nicely.  We could implement
 * a <code>writeObject()</code> method that writes the recorders more effeciently.
 * Also, the recorders themselves could serialize themselves more efficiently.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


 
public class SimpleApplication extends AbstractApplication
{
	private final static int MAP_SIZE = 3;

	private Map recorders;

	public SimpleApplication(RequestContext context, Locale locale)
	{
		super(context, locale);
	}

	/**
	*  Removes all page recorders that contain no changes.  Subclasses
	*  should invoke this implementation in addition to providing
	*  thier own.
	*
	*/

	protected void cleanupAfterRequest()
	{
		Iterator i;
		Map.Entry entry;
		IPageRecorder recorder;
		
		i = recorders.entrySet().iterator();
		
		while (i.hasNext())
		{
			entry = (Map.Entry)i.next();
			recorder = (IPageRecorder)entry.getValue();
			
			if (!recorder.getHasChanges())
				i.remove();
		}
	}


	public void forgetPage(String name)
	{
		IPageRecorder recorder;

		if (recorders == null)
			return;

		recorder = (IPageRecorder)recorders.get(name);
		if (recorder == null)
			return;

		if (recorder.isDirty())
			throw new ApplicationRuntimeException(
				"Could not forget changes to page " + name 
				+ " because the page's recorder has uncommitted changes.");

		recorders.remove(name);		
	}

	/**
	*  Returns an unmodifiable {@link Collection} of the page names for which
	*  {@link IPageRecorder} instances exist.
	*
	*/

	public Collection getActivePageNames()
	{
		if (recorders == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(recorders.keySet());
	}

	public IPageRecorder getPageRecorder(String pageName)
	{
		PageRecorder result = null;

		if (recorders != null)
			result = (PageRecorder)recorders.get(pageName);

		if (result == null)
		{

			// Here's the key thing that identifies SimpleApplication as simple.
			// It uses a SimplePageRecorder (that simply stores the page property changes
			// in the HttpSession).

			result = new SimplePageRecorder();

			if (recorders == null)
				recorders = new HashMap(MAP_SIZE);

			recorders.put(pageName, result);
		}

		return result;
	}

}
