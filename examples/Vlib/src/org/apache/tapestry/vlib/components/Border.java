//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.components;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.vlib.IActivate;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.pages.Login;

/**
 *  The standard Border component, which provides the title of the page,
 *  the link to {@link org.apache.tapestry.vlib.pages.MyLibrary}, 
 *  the {@link org.apache.tapestry.vlib.pages.Login} page and the 
 *  Logout page.
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

    public abstract IBinding getBrowserBinding();

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
                this,
                null,
                null);

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

        IRequestCycle cycle = getPage().getRequestCycle();

        return visit.isUserLoggedIn() && visit.getUser(cycle).isAdmin();
    }

    public void editProfile(IRequestCycle cycle)
    {
        activate("EditProfile", cycle);
    }

    public void viewBorrowedBooks(IRequestCycle cycle)
    {
        activate("BorrowedBooks", cycle);
    }

    public void viewMyLibrary(IRequestCycle cycle)
    {
        activate("MyLibrary", cycle);
    }

    private void activate(String pageName, IRequestCycle cycle)
    {
        IActivate page = (IActivate) cycle.getPage(pageName);

        page.validate(cycle);

        page.activate(cycle);
    }

    public void login(IRequestCycle cycle)
    {
        Visit visit = (Visit) cycle.getEngine().getVisit();

        if (visit != null && visit.isUserLoggedIn())
            return;

        ICallback callback = new PageCallback(getPage().getPageName());
        Login loginPage = (Login) cycle.getPage("Login");
        loginPage.setCallback(callback);
        cycle.activate(loginPage);
    }

    public void logout(IRequestCycle cycle)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getPage().getEngine();

        vengine.logout();

        IMessageProperty home = (IMessageProperty) cycle.getPage("Home");

        home.setMessage(getMessage("goodbye"));

        cycle.activate(home);
    }

    public void selectBrowserPage(IRequestCycle cycle)
    {
        Integer page = (Integer) cycle.getServiceParameters()[0];

        // Not rendering, so have to access the browser the hard way.

        IBinding browserBinding = getBrowserBinding();

        Browser browser = (Browser) browserBinding.getObject("browser", Browser.class);

        if (browser == null)
            throw Tapestry.createRequiredParameterException(this, "browser");

        browser.jump(page.intValue());
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