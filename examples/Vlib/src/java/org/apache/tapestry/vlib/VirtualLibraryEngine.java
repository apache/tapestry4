// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.vlib;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.vlib.pages.ApplicationUnavailable;

/**
 * The engine for the Virtual Library. This exists to implement the external service, which allows
 * the {@link org.apache.tapestry.vlib.pages.ViewBook} and
 * {@link org.apache.tapestry.vlib.pages.ViewPerson} pages to be bookmarked, and to provide a way
 * for shutting down the application when the user logs out.
 * 
 * @author Howard Lewis Ship
 */

public class VirtualLibraryEngine extends BaseEngine
{
    public static final Log LOG = LogFactory.getLog(VirtualLibraryEngine.class);

    private static final boolean DEBUG_ENABLED = Boolean
            .getBoolean("org.apache.tapestry.vlib.debug-enabled");

    private transient boolean _killSession;

    private transient String _applicationUnavailableMessage;

    /**
     * Removes the operations bean instance, if accessed this request cycle.
     * <p>
     * May invalidate the {@link HttpSession} (see {@link #logout()}).
     */

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {
        _applicationUnavailableMessage = null;

        if (_killSession)
        {
            try
            {
                HttpSession session = cycle.getRequestContext().getSession();

                if (session != null)
                    session.invalidate();
            }
            catch (IllegalStateException ex)
            {
                // Ignore.
            }
        }
    }

    /**
     * Sets the visit property to null, and sets a flag that invalidates the {@link HttpSession} at
     * the end of the request cycle.
     */

    public void logout()
    {
        Visit visit = (Visit) getVisit();

        if (visit != null)
            visit.setUser(null);

        _killSession = true;
    }

    public boolean isDebugEnabled()
    {
        return DEBUG_ENABLED;
    }

    protected void handleStaleSessionException(IRequestCycle cycle, StaleSessionException exception)
    {
        try
        {
            IMessageProperty home = (IMessageProperty) cycle.getPage("Home");

            home.setMessage("You have been logged out due to inactivity.");

            redirect("Home", cycle, exception);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    /**
     * Invoked when any kind of runtime exception percolates up to the top level service method.
     * Normally, the standard Exception page is displayed; we logout and setup our own version of
     * the page instead.
     */

    @Override
    protected void activateExceptionPage(IRequestCycle cycle, Throwable cause)
    {
        try
        {
            logout();

            ApplicationUnavailable page = (ApplicationUnavailable) cycle
                    .getPage("ApplicationUnavailable");

            String message = _applicationUnavailableMessage;

            if (message == null)
                message = cause.getMessage();

            if (message == null)
                message = cause.getClass().getName();

            page.activate(message, cause);

            cycle.activate(page);

            renderResponse(cycle);
        }
        catch (Throwable t)
        {
            super.activateExceptionPage(cycle, cause);
        }
    }

}