/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.workbench.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.workbench.Visit;

/**
 *  Common navigational border for the Workbench tutorial.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public abstract class Border extends BaseComponent implements PageRenderListener
{

    /**
     * Array of page names, read from the Strings file; this is the same
     * regardless of localization, so it is static (shared by all).
     * 
     **/

    private static String[] _tabOrder;

    public void pageBeginRender(PageEvent event)
    {
        Visit visit = (Visit) getPage().getEngine().getVisit(event.getRequestCycle());

        setActivePageName(visit.getActiveTabName());

        if (_tabOrder == null)
        {
            String tabOrderValue = getMessage("tabOrder");

            StringSplitter splitter = new StringSplitter(' ');

            _tabOrder = splitter.splitToArray(tabOrderValue);
        }
    }

    /**
     *  Returns the logical names of the pages accessible via the
     *  navigation bar, in appopriate order.
     *
     **/

    public String[] getPageTabNames()
    {
        return _tabOrder;
    }

    public abstract void setPageName(String value);
    
    public abstract String getPageName();

	public abstract void setActivePageName(String activePageName);
	
	public abstract String getActivePageName();
	
	public boolean isActivePage()
	{
		return getPageName().equals(getActivePageName());
	}

    public String getPageTitle()
    {
        // Need to check for synchronization issues, but I think
        // ResourceBundle is safe.

        return getMessage(getPageName());
    }

    public IAsset getLeftTabAsset()
    {
        String name = isActivePage() ? "activeLeft" : "inactiveLeft";

        return getAsset(name);
    }

    public IAsset getMidTabAsset()
    {
        String name = isActivePage() ? "activeMid" : "inactiveMid";

        return getAsset(name);
    }

    public IAsset getRightTabAsset()
    {
        String name = isActivePage() ? "activeRight" : "inactiveRight";

        return getAsset(name);
    }

    public void selectPage(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        String newPageName = (String) parameters[0];

        Visit visit = (Visit) getPage().getEngine().getVisit(cycle);

        visit.setActiveTabName(newPageName);

        cycle.activate(newPageName);
    }
}