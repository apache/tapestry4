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

/**
 *  Creates a link to the {@link PersonPage} page using the external service.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>primaryKey</td> <td>{@link Integer}</td>
 *  <td>R</td>
 *  <td>yes</td> <td>&nbsp;</td>
 *  <td>The primary key of the {@link IPerson} to create a link to.</td>
 * </tr>
 *
 * <tr>
 *      <td>name</td>
 *      <td>{@link String}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The name of the person to create a link to.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>omit</td>
 *		<td>boolean</td>
 *		<td>R</td>
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
    private IBinding primaryKeyBinding;
    private IBinding nameBinding;

    private String[] context;
    private Integer primaryKey;

    private IBinding omitBinding;
    private boolean staticOmit;
    private boolean staticOmitValue;

    public IBinding getPrimaryKeyBinding()
    {
        return primaryKeyBinding;
    }

    public void setPrimaryKeyBinding(IBinding value)
    {
        primaryKeyBinding = value;
    }

    public IBinding getNameBinding()
    {
        return nameBinding;
    }

    public void setNameBinding(IBinding value)
    {
        nameBinding = value;
    }

    public IBinding getOmitBinding()
    {
        return omitBinding;
    }

    public void setOmitBinding(IBinding value)
    {
        omitBinding = value;

        staticOmit = value.isStatic();

        if (staticOmit)
            staticOmitValue = value.getBoolean();
    }

    public boolean getShowLink()
    {
        if (omitBinding == null)
            return true;

        if (staticOmit)
            return !staticOmitValue;

        return !omitBinding.getBoolean();
    }

    /**
     *  The context has two elements.  The first is the page to jump to
     *  ("Person", for {@link PersonPage}), the second is the primary key of the person.
     *
     **/

    public String[] getContext()
    {
        if (context == null)
        {
            context = new String[2];
            context[0] = "Person";
        }

        context[1] = primaryKeyBinding.getString();

        return context;
    }

}