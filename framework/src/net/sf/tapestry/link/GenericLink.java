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

package net.sf.tapestry.link;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  An implementation of {@link net.sf.tapestry.components.IServiceLink} 
 *  that allows
 *  the exact HREF to be specified, usually used for client side
 *  scripting.  
 * 
 *  [<a href="../../../../../ComponentReference/GenericLink.html">Component Reference</a>]
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.2
 * 
 **/

public class GenericLink extends AbstractServiceLink
{
    private String href;

    /**
     *  Returns the String specified by the href binding (this method is invoked
     *  while renderring).
     * 
     *  @throws RequiredParameterException if no href value was supplied.
     * 
     **/

    protected String getURL(IRequestCycle cycle) throws RequestCycleException
    {
        return href;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

}