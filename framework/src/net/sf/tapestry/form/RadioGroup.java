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

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A special type of form component that is used to contain {@link Radio}
 *  components.  The Radio and {@link Radio} group components work together to
 *  update a property of some other object, much like a more flexible
 *  version of a {@link PropertySelection}.
 *
 *  [<a href="../../../../../ComponentReference/RadioGroup.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class RadioGroup extends AbstractFormComponent
{  	
    // Cached copy of the value from the selectedBinding
    private Object _selection;

    // The value from the HTTP request indicating which
    // Radio was selected by the user.
    private int _selectedOption;

    // The HTML field name used for this group (i.e., by all Radio buttons
    // within this group).

    private String _name;

    private boolean _rewinding;
    private boolean _rendering;
    private int _nextOptionId;

    /**
     *  A <code>RadioGroup</code> places itself into the {@link IRequestCycle} as
     *  an attribute, so that its wrapped {@link Radio} components can identify thier
     *  state.
     *
     **/

    private static final String ATTRIBUTE_NAME = "net.sf.tapestry.active.RadioGroup";

    public static RadioGroup get(IRequestCycle cycle)
    {
        return (RadioGroup) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public abstract IBinding getSelectedBinding();


    public String getName()
    {
        return _name;
    }

    public int getNextOptionId()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "nextOptionId");

        return _nextOptionId++;
    }

    /**
     *  Used by {@link Radio} components wrapped by this <code>RadioGroup</code> to see
     *  if the group as a whole is disabled.
     *
     **/

    public abstract boolean isDisabled();
    
    public boolean isRewinding()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    /**
     *  Returns true if the value is equal to the current selection for the
     *  group.  This is invoked by a {@link Radio} during rendering
     *  to determine if it should be marked 'checked'.
     *
     **/

    public boolean isSelection(Object value)
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "selection");

        if (_selection == value)
            return true;

        if (_selection == null || value == null)
            return false;

        return _selection.equals(value);
    }

    /**
    *  Invoked by the {@link Radio} which is selected to update the 
    *  property bound to the selected parameter.
    *
    **/

    public void updateSelection(Object value)
    {
        getSelectedBinding().setObject(value);
    }

    /**
    *  Used by {@link Radio} components when rewinding to see if their value was submitted.
    *
    **/

    public boolean isSelected(int option)
    {
        return _selectedOption == option;
    }

    /**
     * Doesn't actual render an HTML element as there is no direct equivalent for
     * an HTML element.  A <code>RadioGroup</code> component exists to organize the
     * {@link Radio} components it wraps (directly or indirectly).
     *
     * A {@link Radio} can finds its {@link RadioGroup} as a {@link IRequestCycle} attribute.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("RadioGroup.may-not-nest"), this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        _rewinding = form.isRewinding();

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        // When rewinding, find out which (if any) radio was selected by
        // the user.

        if (_rewinding)
        {
            String value = cycle.getRequestContext().getParameter(_name);
            if (value == null)
                _selectedOption = -1;
            else
                _selectedOption = Integer.parseInt(value);
        }

        try
        {
            _rendering = true;
            _nextOptionId = 0;

            // For rendering, the Radio components need to know what the current
            // selection is, so that the correct one can mark itself 'checked'.

            if (!_rewinding)
                _selection = getSelectedBinding().getObject();

            renderBody(writer, cycle);
        }
        finally
        {
            _rendering = false;
            _selection = null;
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }
}