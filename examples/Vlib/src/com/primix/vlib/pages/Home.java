package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;

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
 * @author Howard Ship
 * @version $Id$
 */


public class Home extends BasePage
{
	private String searchTitle;
	private Object searchPublisherPK;
	private String error;
	private String message;
	
	public Home(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		searchTitle = null;
		searchPublisherPK = null;
		error = null;
		message = null;
	}
	
	public String getSearchTitle()
	{
		return searchTitle;
	}
	
	public void setSearchTitle(String value)
	{
		searchTitle = value;
		
		fireObservedChange("searchTitle", value);
	}

	public Object getSearchPublisherPK()
	{
		return searchPublisherPK;
	}
	
	public void setSearchPublisherPK(Object value)
	{
		searchPublisherPK = value;
		
		fireObservedChange("searchPublisherPK", value);
	}

	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setMessage(String value)
	{
		message = value;
	}
	
	public String getMessage()
	{
		return message;
	}

	
	public IActionListener getSearchFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				Matches matches;
				
				matches = (Matches)cycle.getPage("matches");
				
				matches.performTitleQuery(searchTitle, searchPublisherPK);
				
				cycle.setPage(matches);	
			}
		};
	}
	
}