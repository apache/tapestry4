/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.form;

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
    private int _displayWidth;
    private int _maximumLength;
    private boolean _hidden;
    private boolean _disabled;

    private String _name;

    public String getName()
    {
        return _name;
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

        _name = form.getElementId(this);

        if (rewinding)
        {
            if (!_disabled)
            {
                value = cycle.getRequestContext().getParameter(_name);

                updateValue(value);
            }

            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", _hidden ? "password" : "text");

        if (_disabled)
            writer.attribute("disabled");

        writer.attribute("name", _name);

        if (_displayWidth != 0)
            writer.attribute("size", _displayWidth);

        if (_maximumLength != 0)
            writer.attribute("maxlength", _maximumLength);

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
     *  {@link javax.servlet.http.HttpServletRequest}.
     *
     **/

    abstract protected void updateValue(String value) throws RequestCycleException;

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when rendering a response.
     *
     *  @return the current value for the field, as a String, or null.
     **/

    abstract protected String readValue() throws RequestCycleException;

    public boolean getHidden()
    
    {
        return _hidden;
    }

    public void setHidden(boolean hidden)
    {
        _hidden = hidden;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public int getDisplayWidth()
    {
        return _displayWidth;
    }

    public void setDisplayWidth(int displayWidth)
    {
        _displayWidth = displayWidth;
    }

    public int getMaximumLength()
    {
        return _maximumLength;
    }

    public void setMaximumLength(int maximumLength)
    {
        _maximumLength = maximumLength;
    }

}