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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.pages.EditProfile;

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

public abstract class Border extends BaseComponent
{
    public static final String WINDOW_TITLE = "Tapestry Virtual Library";

    // Determined by finishLoad(), static threafter.

    private String _pageType;
    private IAsset _titleImage;
    private IAsset _searchImage;
    private IAsset _searchRolloverImage;
    private IAsset _myLibraryImage;
    private IAsset _myLibraryRolloverImage;
    private IAsset _borrowedBooksImage;
    private IAsset _borrowedBooksRolloverImage;
    private IAsset _loginImage;
    private IAsset _loginRolloverImage;
    private IAsset _newBookImage;
    private IAsset _newBookRolloverImage;
    private IAsset _editProfileImage;
    private IAsset _editProfileRolloverImage;
    private IAsset _giveAwayImage;
    private IAsset _giveAwayRolloverImage;
    private IAsset _editUsersImage;
    private IAsset _editUsersRolloverImage;
    private IAsset _editPublishersImage;
    private IAsset _editPublishersRolloverImage;
    private IAsset _transferBooksImage;
    private IAsset _transferBooksRolloverImage;

    public abstract String getSubtitle();

    public void finishLoad()
    {
        IPage page = getPage();

        String pageName = page.getPageName();

        _pageType = page.getSpecification().getProperty("page-type");

        if (_pageType == null)
            _pageType = pageName;

        _titleImage = getAsset("title_" + pageName);

        if (_titleImage == null)
            _titleImage = getAsset("title_" + _pageType);

        if (_titleImage == null)
            throw new ApplicationRuntimeException(
                "Cannot find title image for " + pageName + " or " + _pageType + ".",
                this);

        // Based on the type, select the images to use on this instance of Border
        // in this particular page.

        _searchImage = selectImage("Search", "search");
        _searchRolloverImage = selectImage("Search", "search_h");

        _myLibraryImage = selectImage("MyLibrary", "mylibrary");
        _myLibraryRolloverImage = selectImage("MyLibrary", "mylibrary_h");

        _borrowedBooksImage = selectImage("BorrowedBooks", "borrowedbooks");
        _borrowedBooksRolloverImage = selectImage("BorrowedBooks", "borrowedbooks_h");

        _newBookImage = selectImage("NewBook", "newbook");
        _newBookRolloverImage = selectImage("NewBook", "newbook_h");

        _editProfileImage = selectImage("EditProfile", "editprofile");
        _editProfileRolloverImage = selectImage("EditProfile", "editprofile_h");

        _giveAwayImage = selectImage("GiveAwayBooks", "giveaway");
        _giveAwayRolloverImage = selectImage("GiveAwayBooks", "giveaway_h");

        _editUsersImage = selectImage("EditUsers", "editusers");
        _editUsersRolloverImage = selectImage("EditUsers", "editusers_h");

        _editPublishersImage = selectImage("EditPublishers", "editpublishers");
        _editPublishersRolloverImage = selectImage("EditPublishers", "editpublishers_h");

        _transferBooksImage = selectImage("TransferBooks", "transferbooks");
        _transferBooksRolloverImage = selectImage("TransferBooks", "transferbooks_h");

        _loginImage = selectImage("Login", "login");
        _loginRolloverImage = selectImage("Login", "login_h");
    }

    private IAsset selectImage(String type, String baseName)
    {
        String key = _pageType.equals(type) ? baseName + "_s" : baseName;

        return getAsset(key);
    }

    public String getWindowTitle()
    {
        String subtitle = getSubtitle();

        if (subtitle == null)
            return WINDOW_TITLE;

        return WINDOW_TITLE + ": " + subtitle;
    }

    public boolean isLoggedIn()
    {
        IEngine engine = getPage().getEngine();
        Visit visit = (Visit) engine.getVisit();

        if (visit == null)
            return false;

        return visit.isUserLoggedIn();
    }

    public boolean isAdmin()
    {
        IEngine engine = getPage().getEngine();
        Visit visit = (Visit) engine.getVisit();

        if (visit == null)
            return false;

        return visit.isUserLoggedIn() && visit.getUser().isAdmin();
    }

    public void editProfile(IRequestCycle cycle)
    {
        EditProfile page = (EditProfile) cycle.getPage("EditProfile");

        page.beginEdit(cycle);
    }

    public void logout(IRequestCycle cycle)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getPage().getEngine();

        vengine.logout();

        IMessageProperty home = (IMessageProperty) cycle.getPage("Home");

        home.setMessage(getString("goodbye"));

        cycle.setPage(home);
    }

    public IAsset getBorrowedBooksImage()
    {
        return _borrowedBooksImage;
    }

    public IAsset getBorrowedBooksRolloverImage()
    {
        return _borrowedBooksRolloverImage;
    }

    public IAsset getTitleImage()
    {
        return _titleImage;
    }

    public IAsset getSearchImage()
    {
        return _searchImage;
    }

    public IAsset getSearchRolloverImage()
    {
        return _searchRolloverImage;
    }

    public IAsset getMyLibraryImage()
    {
        return _myLibraryImage;
    }

    public IAsset getMyLibraryRolloverImage()
    {
        return _myLibraryRolloverImage;
    }

    public IAsset getLoginImage()
    {
        return _loginImage;
    }

    public IAsset getLoginRolloverImage()
    {
        return _loginRolloverImage;
    }

    public String getPageType()
    {
        return _pageType;
    }

    public IAsset getNewBookImage()
    {
        return _newBookImage;
    }

    public IAsset getNewBookRolloverImage()
    {
        return _newBookRolloverImage;
    }

    public IAsset getEditProfileImage()
    {
        return _editProfileImage;
    }

    public IAsset getEditProfileRolloverImage()
    {
        return _editProfileRolloverImage;
    }

    public IAsset getGiveAwayImage()
    {
        return _giveAwayImage;
    }

    public IAsset getGiveAwayRolloverImage()
    {
        return _giveAwayRolloverImage;
    }

    public IAsset getEditPublishersImage()
    {
        return _editPublishersImage;
    }

    public IAsset getEditPublishersRolloverImage()
    {
        return _editPublishersRolloverImage;
    }

    public IAsset getEditUsersImage()
    {
        return _editUsersImage;
    }

    public IAsset getEditUsersRolloverImage()
    {
        return _editUsersRolloverImage;
    }

    public IAsset getTransferBooksImage()
    {
        return _transferBooksImage;
    }

    public IAsset getTransferBooksRolloverImage()
    {
        return _transferBooksRolloverImage;
    }

}