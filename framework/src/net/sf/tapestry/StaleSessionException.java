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

package net.sf.tapestry;

/**
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the {@link javax.servlet.http.HttpSession}
 *  has timed out (and been replaced by a new, empty
 *  one).
 *
 *  <p>The application should redirect to the stale-session page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class StaleSessionException extends RequestCycleException
{
    private transient IPage _page;
    private String _pageName;

    public StaleSessionException()
    {
        super();
    }

    public StaleSessionException(String message, IPage page)
    {
        super(message, null, null);
        _page = page;

        if (page != null)
            _pageName = page.getName();
    }

    public String getPageName()
    {
        return _pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return _page;
    }
}