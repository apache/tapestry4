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

package net.sf.tapestry.form;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A base class for building components that correspond to HTML form elements.
 *  All such components must be wrapped (directly or indirectly) by
 *  a {@link Form} component.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractFormComponent extends AbstractComponent implements IFormComponent
{
    /**
     *  Returns the {@link Form} wrapping this component.
     *
     *  @throws RequestCycleException if the component is not wrapped by a {@link Form}.
     *
     **/

    public IForm getForm(IRequestCycle cycle) throws RequestCycleException
    {
        IForm result = Form.get(cycle);

        if (result == null)
            throw new RequestCycleException(
                Tapestry.getString("AbstractFormComponent.must-be-contained-by-form"),
                this);

        return result;
    }

    public IForm getForm()
    {
        return Form.get(getPage().getRequestCycle());
    }

    abstract public String getName();

    /**
     *  Implemented in some subclasses to provide a display name (suitable
     *  for presentation to the user as a label or error message).  This implementation
     *  return null.
     * 
     **/

    public String getDisplayName()
    {
        return null;
    }
}