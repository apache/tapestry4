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

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Base class for implementing various types of text input fields.
 *  This includes {@link TextField} and
 *  {@link net.sf.tapestry.valid.ValidField}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public abstract class AbstractTextField extends AbstractFormComponent
{
    private int displayWidth;
    private int maximumLength;
    private boolean hidden;
    private boolean disabled;

    private String name;

    public String getName()
    {
        return name;
    }

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        String value;

        IForm form = getForm(cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // If the cycle is rewinding, but the form containing this field is not,
        // then there's no point in doing more work.

        if (!rewinding && cycle.isRewinding())
            return;

        // Used whether rewinding or not.

        name = form.getElementId(this);

        if (rewinding)
        {
            if (!disabled)
            {
                value = cycle.getRequestContext().getParameter(name);

                updateValue(value);
            }

            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", hidden ? "password" : "text");

        if (disabled)
            writer.attribute("disabled");

        writer.attribute("name", name);

        if (displayWidth != 0)
            writer.attribute("size", displayWidth);

        if (maximumLength != 0)
            writer.attribute("maxlength", maximumLength);

        value = readValue();
        if (value != null)
            writer.attribute("value", value);

        generateAttributes(writer, cycle);

        beforeCloseTag(writer, cycle);

        writer.closeTag();
    }

    /**
     *  Invoked from {@link #render(IMarkupWriter, IRequestCycle)}
     *  just before the tag is closed.  This implementation does nothing,
     *  subclasses may override.
     *
     **/

    protected void beforeCloseTag(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        // Do nothing.
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when a value is obtained from the
     *  {@link HttpServletRequest}.
     *
     **/

    abstract protected void updateValue(String value) throws RequestCycleException;

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when rendering a response.
     *
     *  @returns the current value for the field, as a String, or null.
     **/

    abstract protected String readValue() throws RequestCycleException;

    public boolean getHidden()
    
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

}