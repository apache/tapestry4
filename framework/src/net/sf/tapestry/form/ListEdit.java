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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 *  A specialized component used to edit a list of items
 *  within a form; it is similar to a {@link net.sf.tapestry.components.Foreach} but leverages
 *  hidden inputs within the &lt;form&gt; to store the items in the list.
 *
 *  [<a href="../../../../../ComponentReference/ListEdit.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public class ListEdit extends AbstractComponent
{
    private IBinding _valueBinding;
    private IBinding _indexBinding;
    private IBinding _sourceBinding;

    private String _element;

    /** @since 2.2 **/

    private IActionListener _listener;

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setIndexBinding(IBinding value)
    {
        _indexBinding = value;
    }

    public IBinding getIndexBinding()
    {
        return _indexBinding;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        Iterator i = null;

        IForm form = Form.get(cycle);
        if (form == null)
            throw new RequestCycleException(
                Tapestry.getString("must-be-wrapped-by-form", "ListEdit"),
                this);

        boolean cycleRewinding = cycle.isRewinding();

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).

        if (cycleRewinding && !form.isRewinding())
            return;

        String name = form.getElementId(this);

        if (!cycleRewinding)
        {
            i = Tapestry.coerceToIterator(_sourceBinding.getObject());
        }
        else
        {
            RequestContext context = cycle.getRequestContext();
            String[] submittedValues = context.getParameters(name);

            i = Tapestry.coerceToIterator(submittedValues);
        }

		// If the source (when rendering), or the submitted values (on submit)
		// are null, then skip the remainder (nothing to update, nothing to
		// render).
		
		if (i == null)
			return;
			
        int index = 0;

        while (i.hasNext())
        {
            Object value = null;

            if (_indexBinding != null)
                _indexBinding.setInt(index++);

            if (cycleRewinding)
                value = convertValue((String) i.next());
            else
            {
                value = i.next();
                writeValue(writer, name, value);
            }

            _valueBinding.setObject(value);

            if (_listener != null)
                _listener.actionTriggered(this, cycle);

            if (_element != null)
            {
                writer.begin(_element);
                generateAttributes(writer, cycle);
            }

            renderBody(writer, cycle);

            if (_element != null)
                writer.end();

        }
    }

    private void writeValue(IMarkupWriter writer, String name, Object value)
        throws RequestCycleException
    {
        String externalValue;

        try
        {
            externalValue = getDataSqueezer().squeeze(value);
        }
        catch (IOException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("ListEdit.unable-to-convert-value", value),
                this,
                ex);
        }

        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);
        writer.attribute("value", externalValue);
        writer.println();
    }

    private Object convertValue(String value) throws RequestCycleException
    {
        try
        {
            return getDataSqueezer().unsqueeze(value);
        }
        catch (IOException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("ListEdit.unable-to-convert-string", value),
                this,
                ex);
        }
    }

    public String getElement()
    {
        return _element;
    }

    public void setElement(String element)
    {
        _element = element;
    }

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    /** @since 2.2 **/

    public IActionListener getListener()
    {
        return _listener;
    }

    /** @since 2.2 **/

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

	/** @since 2.4 **/
	
    public IBinding getSourceBinding()
    {
        return _sourceBinding;
    }

	/** @since 2.4 **/
	
    public void setSourceBinding(IBinding sourceBinding)
    {
        _sourceBinding = sourceBinding;
    }

}