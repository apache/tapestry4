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

package net.sf.tapestry.callback;

import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Simple callback for returning to a page.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public class PageCallback implements ICallback
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -3286806776105690068L;

    private String _pageName;

    public PageCallback(String pageName)
    {
        _pageName = pageName;
    }

    public PageCallback(IPage page)
    {
        this(page.getName());
    }

    public String toString()
    {
        return "PageCallback[" + _pageName + "]";
    }

    /**
     *  Invokes {@link IRequestCycle#setPage(String)} to select the previously
     *  identified page as the response page.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException
    {
        cycle.setPage(_pageName);
    }
}