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

import java.io.IOException;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 *  Implements a hidden field within a {@link Form}.
 *
 *  [<a href="../../../../../ComponentReference/Hidden.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Hidden extends AbstractFormComponent
{
    private String _name;

    public String getName()
    {
        return _name;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);
        boolean formRewound = form.isRewinding();

        _name = form.getElementId(this);

        // If the form containing the Hidden isn't rewound, then render.

        if (!formRewound)
        {
            // Optimiziation: if the page is rewinding (some other action or
            // form was submitted), then don't bother rendering.

            if (cycle.isRewinding())
                return;

            String externalValue = null;

            if (getEncode())
            {
                Object value = getValueBinding().getObject();

                try
                {
                    externalValue = getDataSqueezer().squeeze(value);
                }
                catch (IOException ex)
                {
                    throw new RequestCycleException(this, ex);
                }
            }
            else
                externalValue = (String) getValueBinding().getObject("value", String.class);

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name);
            writer.attribute("value", externalValue);

            return;
        }

        String externalValue = cycle.getRequestContext().getParameter(_name);
        Object value = null;

        if (getEncode())
        {
            try
            {
                value = getDataSqueezer().unsqueeze(externalValue);
            }
            catch (IOException ex)
            {
                throw new RequestCycleException(this, ex);
            }
        }
        else
            value = externalValue;

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        getValueBinding().setObject(value);

IActionListener listener = getListener();

        if (listener != null)
            listener.actionTriggered(this, cycle);
    }

    /** @since 2.2 **/

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    public abstract IActionListener getListener();


    public abstract IBinding getValueBinding();


    /**
     * 
     *  Returns false.  Hidden components are never disabled.
     * 
     *  @since 2.2
     * 
     **/

    public boolean isDisabled()
    {
        return false;
    }

    /** 
     * 
     *  Returns true if the compent encodes object values using a
     *  {@link net.sf.tapestry.util.io.DataSqueezer}, false
     *  if values are always Strings.
     * 
     *  @since 2.2
     * 
     **/

    public abstract boolean getEncode();

}