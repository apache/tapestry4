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

package tutorial.portal;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Home page for the Portal tutorial.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Home extends BasePage
{
    private PortletModel model;
    private int newPortletId;

    public void detach()
    {
        model = null;
        newPortletId = 0;

        super.detach();
    }

    public void setModel(PortletModel value)
    {
        model = value;
    }

    public PortletModel getModel()
    {
        return model;
    }

    public void setNewPortletId(int value)
    {
        newPortletId = value;
    }

    public int getNewPortletId()
    {
        return newPortletId;
    }

    public IActionListener getCloseListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
            {
                closeModel();
            }
        };
    }

    private void closeModel()
    {
        Visit visit = (Visit) getVisit();

        visit.removeModel(model);
    }

    public IActionListener getFormListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
            {
                addModel();
            }
        };
    }

    private void addModel()
    {
        Visit visit = (Visit) getVisit();

        visit.addModel(newPortletId);
    }
}