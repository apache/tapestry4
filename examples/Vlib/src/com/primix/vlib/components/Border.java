/*
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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

package com.primix.vlib.components;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.tapestry.callback.*;
import com.primix.vlib.*;
import com.primix.vlib.pages.*;

/**
 *  The standard Border component, which provides the title of the page,
 *  the link to {@link MyLibrary}, the {@link Login} page and the {@link Logout}
 *  page.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class Border extends BaseComponent
{	
    private static final String WINDOW_TITLE = "Primix Virtual Library";

    private IBinding titleBinding;
    private IBinding subtitleBinding;
	private IBinding stickyBinding;
	
    public void setTitleBinding(IBinding value)
    {
        titleBinding = value;
    }

    public IBinding getTitleBinding()
    {
        return titleBinding;
    }

    public void setSubtitleBinding(IBinding value)
    {
        subtitleBinding = value;
    }

    public IBinding getSubtitleBinding()
    {
        return subtitleBinding;
    }

	public IBinding getStickyBinding()
	{
		return stickyBinding;
	}
	
	public void setStickyBinding(IBinding value)
	{
		stickyBinding = value;
	}
	
	
	public boolean isLoggedOut()
	{
		return ! isLoggedIn();
	}
	
	public boolean isLoggedIn()
	{
		// Get the visit, if it exists, without creating it.
		
		Visit visit = (Visit)page.getEngine().getVisit();
		
		if (visit == null)
			return false;
		
		return visit.isUserLoggedIn();
	}
	
	/**
	 *  Show the Logout button on all pages except the Logout page itself.
	 *
	 */
	 
	public boolean getShowLogout()
	{
		return !getPage().getName().equals("Logout");
	}

    public String getWindowTitle()
    {
        String subtitle = null;

        if (subtitleBinding != null)
            subtitle = subtitleBinding.getString();

        if (subtitle == null)
            return WINDOW_TITLE;
        else
            return WINDOW_TITLE + ": " + subtitle;
    }
	
	public IDirectListener getLoginListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IDirect component, String[] context, IRequestCycle cycle)
			throws RequestCycleException
			{
				login(cycle);
			}
		};
	}
	
	private void login(IRequestCycle cycle)
		throws RequestCycleException
	{
		Login login = (Login)cycle.getPage("Login");
		
		if (stickyBinding != null)
			if (stickyBinding.getBoolean())
			{
				// Setup the login page to return to this page (whatever it is)
				// after the login.
				
				login.setCallback(new PageCallback(page));
			}
		
		cycle.setPage(login);
	}

}	
