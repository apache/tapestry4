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
 *  [<a href="../../../../../ComponentReference/Delegator.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Delegator extends AbstractComponent
{
	private IRender _delegate;

    /**
     *  Gets its delegate and invokes {@link IRender#render(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (_delegate != null)
        	_delegate.render(writer, cycle);
    }
    
    public IRender getDelegate()
    {
        return _delegate;
    }

    public void setDelegate(IRender delegate)
    {
        _delegate = delegate;
    }

}