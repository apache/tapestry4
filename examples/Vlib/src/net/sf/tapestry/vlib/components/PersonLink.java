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

package net.sf.tapestry.vlib.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.vlib.pages.PersonPage;

/**
 *  Creates a link to the {@link net.sf.tapestry.vlib.pages.PersonPage} 
 *  page using the external service.
 *
 *
 *  <table border=1>
 *  <tr> 
 *  <th>Parameter</th> 
 *  <th>Type</th> 
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th>
 *  <th>Description</th>
 * </tr>
 * 
 * <tr>
 *  <td>primaryKey</td> 
 *  <td>{@link Integer}</td>
 *  <td>in</td>
 *  <td>yes</td> 
 *  <td>&nbsp;</td>
 *  <td>The primary key of the {@link net.sf.tapestry.vlib.ejb.IPerson} to create a link to.</td>
 * </tr>
 *
 * <tr>
 *      <td>name</td>
 *      <td>{@link String}</td>
 *      <td>in</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The name of the person to create a link to.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>omit</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If true, then the link is omitted and replaced with an &amp;nbsp;.
 *		</td>
 *	</tr>
 *
 *  </table>
 *
 *  <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PersonLink extends BaseComponent
{
    private Integer primaryKey;
    private String name;
    private boolean omit;
    private String styleClass;

    /**
     *  Return parameters for the
     *  {@link net.sf.tapestry.vlib.ExternalService}.
     *   The first parameter is the page to jump to
     *  ("Person", for {@link PersonPage}), 
     *  the second is the primary key of the person.
     *
     **/

    public Object[] getPersonParameters()
    {
        return new Object[]
        {
            "Person",
            primaryKey
        };
    }

    public String getStyleClass()
    {
        return styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getOmit()
    {
        return omit;
    }

    public void setOmit(boolean omit)
    {
        this.omit = omit;
    }

    public Integer getPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey)
    {
        this.primaryKey = primaryKey;
    }

}