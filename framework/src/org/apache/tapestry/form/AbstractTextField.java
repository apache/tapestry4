/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.form;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Base class for implementing various types of text input fields.
 *  This includes {@link TextField} and
 *  {@link org.apache.tapestry.valid.ValidField}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public abstract class AbstractTextField extends AbstractFormComponent
{
    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
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

        String name = form.getElementId(this);
		
        if (rewinding)
        {
            if (!isDisabled())
            {
                value = cycle.getRequestContext().getParameter(name);

                updateValue(value);
            }

            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", isHidden() ? "password" : "text");

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        writer.attribute("name", name);

        value = readValue();
        if (value != null)
            writer.attribute("value", value);

        renderInformalParameters(writer, cycle);

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
    {
        // Do nothing.
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when a value is obtained from the
     *  {@link javax.servlet.http.HttpServletRequest}.
     *
     **/

    abstract protected void updateValue(String value);

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when rendering a response.
     *
     *  @return the current value for the field, as a String, or null.
     **/

    abstract protected String readValue();

    public abstract boolean isHidden();

    public abstract boolean isDisabled();
}