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
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 *
 *
 * <tr>
 *   <td>disabled</td> 
 *   <td>boolean</td> 
 *   <td>in</td> 
 *   <td>no</td> 
 *   <td>false</td>
 *   <td>Controls whether the link is produced.  If disabled, the portion of the template
 *  the link surrounds is still rendered, but not the link itself.
 *  </td></tr>
 *
 *
 *  <tr>
 *   <td>href</td>
 *   <td>{@link String}
 *   <td>in</td>
 *   <td>yes</td>
 *   <td>&nbsp;</td>
 *   <td>The exact URL to invoked; typically of the form <code>javascript:...</code>.
 *   </td>
 *  </tr>
 *
 * </table>
 *
 *
 *  <p>Informal parameters are allowed.
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