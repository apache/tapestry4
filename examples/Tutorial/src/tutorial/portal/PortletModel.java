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

import java.io.Serializable;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;

/**
 *  Models an active portlet within
 *  the application.  Tracks whether the portlet is expanded (maximimized)
 *  or minimized.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PortletModel implements Serializable
{
    private int id;
    private boolean expanded = true;
    private String title;
    private String bodyPage;
    private String bodyIdPath;

    public PortletModel(int id, String title, String bodyPage, String bodyIdPath)
    {
        this.id = id;
        this.title = title;
        this.bodyPage = bodyPage;
        this.bodyIdPath = bodyIdPath;
    }

    public Block getBodyBlock(IRequestCycle cycle)
    {
        IPage page = cycle.getPage(bodyPage);
        IComponent component = page.getNestedComponent(bodyIdPath);

        return (Block) component;
    }

    public void toggleExpanded()
    {
        expanded = !expanded;
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean value)
    {
        expanded = value;
    }

    public String getTitle()
    {
        return title;
    }

    public int getId()
    {
        return id;
    }
}