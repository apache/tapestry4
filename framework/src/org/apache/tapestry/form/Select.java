//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
            throw Tapestry.createRenderOnlyPropertyException(this, "rewinding");

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
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Select.may-not-nest"),
                this,
                null,
                null);

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