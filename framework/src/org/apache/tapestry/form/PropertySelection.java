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
import org.apache.tapestry.Tapestry;

/**
 *  A component used to render a drop-down list of options that
 *  the user may select.
 * 
 *  [<a href="../../../../../ComponentReference/PropertySelection.html">Component Reference</a>]
 *
 *  <p>Earlier versions of PropertySelection (through release 2.2)
 *  were more flexible, they included a <b>renderer</b> property
 *  that controlled how the selection was rendered.  Ultimately,
 *  this proved of little value and this portion of
 *  functionality was deprecated in 2.3 and will be removed in 2.3.
 * 
 *  <p>Typically, the values available to be selected
 *  are defined using an {@link org.apache.commons.lang.enum.Enum}.
 *  A PropertySelection is dependent on
 *  an {@link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select a particular 
 *  {@link org.apache.commons.lang.enum.Enum} to assign to a property; the
 *  {@link EnumPropertySelectionModel} class simplifies this.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class PropertySelection extends AbstractFormComponent
{
    /**
     *  A shared instance of {@link SelectPropertySelectionRenderer}.
     *
     * 
     **/

    public static final IPropertySelectionRenderer DEFAULT_SELECT_RENDERER =
        new SelectPropertySelectionRenderer();

    /**
     *  A shared instance of {@link RadioPropertySelectionRenderer}.
     *
     * 
     **/

    public static final IPropertySelectionRenderer DEFAULT_RADIO_RENDERER =
        new RadioPropertySelectionRenderer();

    /**
     *  Renders the component, much of which is the responsiblity
     *  of the {@link IPropertySelectionRenderer renderer}.  The possible options,
     *  thier labels, and the values to be encoded in the form are provided
     *  by the {@link IPropertySelectionModel model}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        if (rewinding)
        {
            // If disabled, ignore anything that comes up from the client.

            if (isDisabled())
                return;

            String optionValue = cycle.getRequestContext().getParameter(name);

            Object value = (optionValue == null) ? null : getModel().translateValue(optionValue);

            setValue(value);

            return;
        }

        IPropertySelectionRenderer renderer = getRenderer();

        if (renderer != null)
        {
            renderWithRenderer(writer, cycle, renderer);
            return;
        }

        writer.begin("select");
        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (getSubmitOnChange())
            writer.attribute("onchange", "javascript:this.form.submit();");

        // Apply informal attributes.

        renderInformalParameters(writer, cycle);

        writer.println();

        IPropertySelectionModel model = getModel();

        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");

        int count = model.getOptionCount();
        boolean foundSelected = false;
        boolean selected = false;
        Object value = getValue();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            if (!foundSelected)
            {
                selected = isEqual(option, value);
                if (selected)
                    foundSelected = true;
            }

            writer.begin("option");
            writer.attribute("value", model.getValue(i));

            if (selected)
                writer.attribute("selected", "selected");

            writer.print(model.getLabel(i));

            writer.end();

            writer.println();

            selected = false;
        }

        writer.end(); // <select>

    }

    /**
     *  Renders the property selection using a {@link IPropertySelectionRenderer}.
     *  Support for this will be removed in 2.3.
     * 
     **/

    private void renderWithRenderer(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionRenderer renderer)
    {
        renderer.beginRender(this, writer, cycle);

        IPropertySelectionModel model = getModel();

        int count = model.getOptionCount();

        boolean foundSelected = false;
        boolean selected = false;

        Object value = getValue();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            if (!foundSelected)
            {
                selected = isEqual(option, value);
                if (selected)
                    foundSelected = true;
            }

            renderer.renderOption(this, writer, cycle, model, option, i, selected);

            selected = false;
        }

        // A PropertySelection doesn't allow a body, so no need to worry about
        // wrapped components.

        renderer.endRender(this, writer, cycle);
    }

    private boolean isEqual(Object left, Object right)
    {
        // Both null, or same object, then are equal

        if (left == right)
            return true;

        // If one is null, the other isn't, then not equal.

        if (left == null || right == null)
            return false;

        // Both non-null; use standard comparison.

        return left.equals(right);
    }

    public abstract IPropertySelectionModel getModel();

    public abstract IPropertySelectionRenderer getRenderer();

    /** @since 2.2 **/

    public abstract boolean getSubmitOnChange();

    /** @since 2.2 **/

    public abstract Object getValue();

    /** @since 2.2 **/

    public abstract void setValue(Object value);

    /**
     *  Returns true if this PropertySelection's disabled parameter yields true.
     *  The corresponding HTML control(s) should be disabled.
     **/

    public abstract boolean isDisabled();

}