/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.pages.EditProfile;
import org.apache.tapestry.vlib.pages.Home;
import org.apache.tapestry.vlib.pages.Login;
import org.apache.tapestry.vlib.pages.NewBook;

/**
 *  The standard Border component, which provides the title of the page,
 *  the link to {@link org.apache.tapestry.vlib.pages.MyLibrary}, 
 *  the {@link org.apache.tapestry.vlib.pages.Login} page and the 
 *  {@link org.apache.tapestry.vlib.pages.Logout} page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Border extends BaseComponent
{
    private static final String WINDOW_TITLE = "Virtual Library";

    private String _subtitle;

    private static final int SEARCH_PAGE_TYPE = 1;
    private static final int LIBRARY_PAGE_TYPE = 2;

    // Also used for logout and registration pages.

    private static final int LOGIN_PAGE_TYPE = 3;

    private static final int ADMIN_PAGE_TYPE = 4;

    private int _pageType = 0;

    private IAsset _subheader;

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
        if (_pageType == 0)
        {
            String typeName = getPage().getSpecification().getProperty("page-type");

            _pageType = SEARCH_PAGE_TYPE;

            if ("library".equals(typeName))
                _pageType = LIBRARY_PAGE_TYPE;
            else
                if ("login".equals(typeName))
                    _pageType = LOGIN_PAGE_TYPE;
                else
                    if ("admin".equals(typeName))
                        _pageType = ADMIN_PAGE_TYPE;
        }

        return _pageType;
    }

    public boolean isLoggedIn()
    {
        // Get the visit, if it exists, without creating it.

        Visit visit = (Visit) getPage().getEngine().getVisit();

        return visit != null && visit.isUserLoggedIn();
    }

    /**
     *  Returns true if the user is logged in and is an adminstrator.
     *  This makes additional left-side options appear.
     *
     **/

    public boolean isAdmin()
    {
        Visit visit = (Visit) getPage().getEngine().getVisit();

        return visit != null && visit.isUserLoggedIn() && visit.getUser().isAdmin();
    }

    public String getWindowTitle()
    {
        if (_subtitle == null)
            return WINDOW_TITLE;

        return WINDOW_TITLE + ": " + _subtitle;
    }

    public void login(IRequestCycle cycle)
    {
        Login login = (Login) cycle.getPage("Login");

        // If on one of the Login pages (including Logout)
        // then don't set a callback (this will cause the user
        // to go to the MyLibrary page).

        if (getPageType() != LOGIN_PAGE_TYPE)
            login.setCallback(new PageCallback(getPage()));

        cycle.setPage(login);
    }

    public void logout(IRequestCycle cycle)
    {
        VirtualLibraryEngine engine = (VirtualLibraryEngine) getPage().getEngine();

        engine.logout();

        Home home = (Home) cycle.getPage("Home");

        home.setMessage("Goodbye.");

        cycle.setPage(home);
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
        return !getPage().getPageName().equals("MyLibrary");
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

        cycle.setPage(page);
    }

    public IAsset getSubheader()
    {
        if (_subheader == null)
        {
            String name = "header_" + getPage().getPageName();

            _subheader = getAsset(name);

            if (_subheader == null)
                _subheader = getAsset("spacer");
        }

        return _subheader;
    }

    public String getSubtitle()
    {
        return _subtitle;
    }

    public void setSubtitle(String subtitle)
    {
        _subtitle = subtitle;
    }

}