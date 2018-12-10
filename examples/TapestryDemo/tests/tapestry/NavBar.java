package tests.tapestry;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import java.util.*;

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
 *  Implements the navigation bar for the demo application.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class NavBar extends BaseComponent
{
	private static final String[] pageNames =
    { "home", "empty", "survey", "debug", "serialization", "history", "credits" };

	private static final Hashtable displayNames = new Hashtable(5);

	static
	{
		displayNames.put("home", "Home");
		displayNames.put("empty", "Empty");
		displayNames.put("survey", "Survey");
		displayNames.put("debug", "Debug");
		displayNames.put("serialization", "Serialization Test");
		displayNames.put("credits", "Credits");
		displayNames.put("history", "History");

		displayNames.put("db", "Database");
		displayNames.put("logging", "Logging");
	}

	private String currentPageName;

	public NavBar(IPage page, IComponent container, String id, 
		ComponentSpecification spec)
	{
		super(page, container, id, spec);
	}

	public String getCurrentPageName()
	{
		return currentPageName;
	}

	/**
	*  Returns the display name of the current page name.
	*
	*/

	public String getDisplayName()
	{
		return (String)displayNames.get(currentPageName);
	}

	public String[] getPageNames()
	{
		return pageNames;
	}

	public void setCurrentPageName(String value)
	{
		currentPageName = value;
	}
	
	public boolean isPageLinkEnabled()
	{
		return !currentPageName.equals(page.getName());
	}
}

