/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;

/**
 *  Implements a component that manages an HTML &lt;select&gt; form element.
 *  The most common situation, using a &lt;select&gt; to set a specific
 *  property of some object, is best handled using a {@link PropertySelection} component.
 *
 *  [<a href="../../../../../ComponentReference/Select.html">Component Reference</a>]
 * 
 *  <p>Otherwise, this component is very similar to {@link RadioGroup}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Select extends AbstractFormComponent
{
    private boolean _rewinding;
    private boolean _rendering;

    private Set _selections;
    private int _nextOptionId;

    /**
     *  Used by the <code>Select</code> to record itself as a
     *  {@link IRequestCycle} attribute, so that the
     *  {@link Option} components it wraps can have access to it.
     *
     **/

    private final static String ATTRIBUTE_NAME = "org.apache.tapestry.active.Select";

    public static Select get(IRequestCycle cycle)
    {
        return (Select) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public abstract boolean isDisabled();
    
    public abstract boolean isMultiple();
    
    public boolean isRewinding()
    {
        if (!_rendering)
            throw  Tapestry.createRenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    public String getNextOptionId()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "nextOptionId");

        // Return it as a hex value.

        return Integer.toString(_nextOptionId++);
    }

    public boolean isSelected(String value)
    {
        if (_selections == null)
            return false;

        return _selections.contains(value);
    }

    /**
     *  Renders the &lt;option&gt; element, or responds when the form containing the element
     *  is submitted (by checking {@link IForm#isRewinding()}.
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
		
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(Tapestry.getMessage("Select.may-not-nest"), this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        _rewinding = form.isRewinding();

        // Used whether rewinding or not.

        String name = form.getElementId(this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        if (_rewinding)
        {
            _selections = buildSelections(cycle, name);
        }
        else
        {
            writer.begin("select");

            writer.attribute("name", name);

            if (isMultiple())
                writer.attribute("multiple", "multiple");

            if (isDisabled())
                writer.attribute("disabled", "disabled");

            renderInformalParameters(writer, cycle);
        }

        _rendering = true;
        _nextOptionId = 0;

        renderBody(writer, cycle);

        if (!_rewinding)
        {
            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);

    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _rendering = false;
        _selections = null;

        super.cleanupAfterRender(cycle);
    }

    /**
     *  Cut-and-paste with {@link RadioGroup}!
     *
     **/

    private Set buildSelections(IRequestCycle cycle, String parameterName)
    {
        RequestContext context = cycle.getRequestContext();

        String[] parameters = context.getParameters(parameterName);

        if (parameters == null)
            return null;

        int length = parameters.length;

        int size = (parameters.length > 30) ? 101 : 7;

        Set result = new HashSet(size);

        for (int i = 0; i < length; i++)
            result.add(parameters[i]);

        return result;
    }
}