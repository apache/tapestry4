//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.PageCallback;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.pages.EditProfile;
import net.sf.tapestry.vlib.pages.Login;
import net.sf.tapestry.vlib.pages.NewBook;

/**
 *  The standard Border component, which provides the title of the page,
 *  the link to {@link MyLibrary}, the {@link Login} page and the {@link Logout}
 *  page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Border extends BaseComponent
{
    private static final String WINDOW_TITLE = "Virtual Library";

    private IBinding titleBinding;
    private IBinding subtitleBinding;

    private static final int SEARCH_PAGE_TYPE = 1;
    private static final int LIBRARY_PAGE_TYPE = 2;

    // Also used for logout and registration pages.

    private static final int LOGIN_PAGE_TYPE = 3;

    private static final int ADMIN_PAGE_TYPE = 4;

    private int pageType = 0;

    private IAsset subheader;

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

    /**
     *  Determines the 'type' of page, which is used to highlight (with an icon) one of the options
     *  on the left-side navigation bar.
     *
     *  This is determined from the page's specification; a property named "page-type" is read.  It
     *  should be one of the following values:
     *  <ul>
     *  <li>search
     *  <li>library
     *  <li>login
     * </ul>
     * 
     *  <p>If not specified, "search" is assumed.
     *
     **/

    protected int getPageType()
    {
        if (pageType == 0)
        {
            String typeName = getPage().getSpecification().getProperty("page-type");

            pageType = SEARCH_PAGE_TYPE;

            if ("library".equals(typeName))
                pageType = LIBRARY_PAGE_TYPE;
            else if ("login".equals(typeName))
                pageType = LOGIN_PAGE_TYPE;
            else if ("admin".equals(typeName))
                pageType = ADMIN_PAGE_TYPE;
        }

        return pageType;
    }

    public boolean isLoggedOut()
    {
        return !isLoggedIn();
    }

    public boolean isLoggedIn()
    {
        // Get the visit, if it exists, without creating it.

        Visit visit = (Visit) page.getEngine().getVisit();

        if (visit == null)
            return false;

        return visit.isUserLoggedIn();
    }

    /**
     *  Returns true if the user is logged in and is an adminstrator.
     *  This makes additional left-side options appear.
     *
     **/

    public boolean isAdmin()
    {
        Visit visit = (Visit) page.getEngine().getVisit();

        return (visit != null && visit.isUserLoggedIn() && visit.getUser().isAdmin());
    }

    /**
     *  Show the Logout button on all pages except the Logout page itself.
     *
     **/

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

    public void login(IRequestCycle cycle) throws RequestCycleException
    {
        Login login = (Login) cycle.getPage("Login");

        // If on one of the Login pages (including Logout)
        // then don't set a callback (this will cause the user
        // to go to the MyLibrary page).

        if (getPageType() != LOGIN_PAGE_TYPE)
            login.setCallback(new PageCallback(page));

        cycle.setPage(login);
    }

    public IAsset getSearchIcon()
    {
        return getIcon(SEARCH_PAGE_TYPE);
    }

    public IAsset getMyLibraryIcon()
    {
        return getIcon(LIBRARY_PAGE_TYPE);
    }

    public IAsset getLoginIcon()
    {
        return getIcon(LOGIN_PAGE_TYPE);
    }

    public boolean isLibraryPage()
    {
        return getPageType() == LIBRARY_PAGE_TYPE;
    }

    /**
     *  Show the slash on library pages that aren't "MyLibrary".
     *
     **/

    public boolean getShowSlash()
    {
        return !getPage().getName().equals("MyLibrary");
    }

    public IAsset getAdminIcon()
    {
        return getIcon(ADMIN_PAGE_TYPE);
    }

    private IAsset getIcon(int type)
    {
        String name = (type == getPageType()) ? "dot" : "spacer";

        return getAsset(name);
    }

    /**
     *  Listener that invokes the {@link EditProfile} page to allow a user
     *  to edit thier name, etc.
     *
     **/

    public void editProfile(IRequestCycle cycle)
    {
        EditProfile page;

        page = (EditProfile) cycle.getPage("EditProfile");

        page.beginEdit(cycle);
    }

    /**
     *  Listener used to add a new book.
     *
     **/

    public void addNewBook(IRequestCycle cycle)
    {
        NewBook page = (NewBook) cycle.getPage("NewBook");

        // Setup defaults for the new book.

        page.getAttributes().put("lendable", Boolean.TRUE);

        cycle.setPage(page);
    }

    public IAsset getSubheader()
    {
        if (subheader == null)
        {
            String name = "header-" + getPage().getName();

            subheader = getAsset(name);

            if (subheader == null)
                subheader = getAsset("spacer");
        }

        return subheader;
    }

}