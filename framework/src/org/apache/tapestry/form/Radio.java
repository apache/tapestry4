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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  [<a href="../../../../../ComponentReference/Radio.html">Component Reference</a>]
 *
 * 
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Radio extends AbstractComponent
{
    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {

        RadioGroup group = RadioGroup.get(cycle);
        if (group == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Radio.must-be-contained-by-group"),
                this);

        // The group determines rewinding from the form.

        boolean rewinding = group.isRewinding();

        int option = group.getNextOptionId();

        if (rewinding)
        {
            // If not disabled and this is the selected button within the radio group,
            // then update set the selection from the group to the value for this
            // radio button.  This will update the selected parameter of the RadioGroup.

            if (!isDisabled() && !group.isDisabled() && group.isSelected(option))
                group.updateSelection(getValue());
            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", "radio");

        writer.attribute("name", group.getName());

        // As the group if the value for this Radio matches the selection
        // for the group as a whole; if so this is the default radio and is checked.

        if (group.isSelection(getValue()))
            writer.attribute("checked");

        if (isDisabled() || group.isDisabled())
            writer.attribute("disabled");

        // The value for the Radio matches the option number (provided by the RadioGroup).
        // When the form is submitted, the RadioGroup will know which option was,
        // in fact, selected by the user.

        writer.attribute("value", option);

        generateAttributes(writer, cycle);

    }

    public abstract boolean isDisabled();

    public abstract Object getValue();
}