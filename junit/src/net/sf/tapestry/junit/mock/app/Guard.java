//
// Tapestry Web Application Framework
// Copyright (c) 2002 by Howard Lewis Ship
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

package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.ICallback;
import net.sf.tapestry.html.BasePage;

/**
 *  Guard page for the Protected page.  Protected checks the "visited" property of this page and,
 *  if false, redirects to this page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Guard extends BasePage
{
    private ICallback _callback;
    private boolean _visited;
    
    public void detach()
    {
        _callback = null;
        _visited = false;
        
        super.detach();
    }
    
    public ICallback getCallback()
    {
        return _callback;
    }

    public boolean isVisited()
    {
        return _visited;
    }

    public void setCallback(ICallback callback)
    {
        _callback = callback;
        fireObservedChange("callback", callback);
    }

    public void setVisited(boolean visited)
    {
        _visited = visited;
        fireObservedChange("visited", visited);
    }
    
    public void linkClicked(IRequestCycle cycle)
    throws RequestCycleException
    {
        setVisited(true);
        
        ICallback callback = _callback;
        
        setCallback(null);
        
        callback.performCallback(cycle);
    }

}
