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

import java.util.HashSet;
import java.util.Set;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

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

public class Select extends AbstractFormComponent
{
    private boolean _multiple;
    private boolean _disabled;
    private boolean _rewinding;
    private boolean _rendering;

    private Set _selections;
    private int _nextOptionId;

    private String _name;

    public String getName()
    {
        return _name;
    }

    /**
     *  Used by the <code>Select</code> to record itself as a
     *  {@link IRequestCycle} attribute, so that the
     *  {@link Option} components it wraps can have access to it.
     *
     **/

    private final static String ATTRIBUTE_NAME = "net.sf.tapestry.active.Select";

    public static Select get(IRequestCycle cycle)
    {
        return (Select) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public boolean isMultiple()
    {
        return _multiple;
    }

    public void setMultiple(boolean newValue)
    {
        _multiple = newValue;
    }

    public boolean isRewinding()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    public String getNextOptionId()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "nextOptionId");

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

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("Select.may-not-nest"), this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        _rewinding = form.isRewinding();

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        if (_rewinding)
        {
            _selections = buildSelections(cycle, _name);
        }
        else
        {
            writer.begin("select");

            writer.attribute("name", _name);

            if (_multiple)
                writer.attribute("multiple");

            if (_disabled)
                writer.attribute("disabled");

            generateAttributes(writer, cycle);
        }

        _rendering = true;
        _nextOptionId = 0;

        renderWrapped(writer, cycle);

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
        if (parameters.length == 0)
            return null;

        int size = (parameters.length > 30) ? 101 : 7;

        Set result = new HashSet(size);

        for (int i = 0; i < length; i++)
            result.add(parameters[i]);

        return result;
    }
}