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

package net.sf.tapestry.event;

import java.util.EventListener;

/**
 *  Listener interface for objects that need to know when the engine
 *  containing the page is discarded.  This is typically relevant
 *  only to components that have persistent page properties that
 *  require some kind of cleanup (typically, because they
 *  are references to EJBs, or something similar).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 **/

public interface PageCleanupListener extends EventListener
{
    /**
     *  Invoked when the page is notified, by the {@link net.sf.tapestry.IEngine}
     *  to cleanup; this occurs when the engine is discarded
     *  because its {@link javax.servlet.http.HttpSession} was
     *  invalidated.  The page is rolled back to its
     *  last state and then invokes this method.
     *
     *  <p>{@link PageEvent#getRequestCycle()} will return null.
     **/

    public void pageCleanup(PageEvent event);
}