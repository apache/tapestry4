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

package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  A component which delegates it's behavior to another object.
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *	  <th>Read / Write </th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>delegate</td>
 *  <td>{@link IRender}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The object which will provide the rendering for the component.</td>
 * </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Delegator extends AbstractComponent
{
    private IBinding delegateBinding;

    public void setDelegateBinding(IBinding value)
    {
        delegateBinding = value;
    }

    public IBinding getDelegateBinding()
    {
        return delegateBinding;
    }

    /**
     *  Gets its delegate and invokes {@link IRender#render(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IRender delegate = null;

        try
        {
            delegate = (IRender) delegateBinding.getObject("delegate", IRender.class);
        }
        catch (BindingException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        if (delegate == null)
            throw new RequiredParameterException(this, "delegate", delegateBinding);

        delegate.render(writer, cycle);
    }
}