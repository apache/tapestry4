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

package org.apache.tapestry.valid;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractTextField;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.Body;

/**
 *
 *  A {@link Form} component that creates a text field that
 *  allows for validation of user input and conversion between string and object
 *  values. 
 * 
 *  [<a href="../../../../../ComponentReference/ValidField.html">Component Reference</a>]
 * 
 *  <p> A ValidatingTextField uses an {@link IValidationDelegate} to 
 *  track errors and an {@link IValidator} to convert between strings and objects
 *  (as well as perform validations).  The validation delegate is shared by all validating
 *  text fields in a form, the validator may be shared my multiple elements as desired.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class ValidField extends AbstractTextField implements IFormComponent
{
    public abstract Object getValue();
    public abstract void setValue(Object value);

    public abstract String getDisplayName();

    /**
     *
     *  Renders the component, which involves the {@link IValidationDelegate delegate}.
     *
     *  <p>During a render, the <em>first</em> field rendered that is either
     *  in error, or required but null gets special treatment.  JavaScript is added
     *  to select that field (such that the cursor jumps right to the field when the
     *  page loads).
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
        IValidationDelegate delegate = form.getDelegate();

        if (delegate == null)
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ValidField.no-delegate",
                    getExtendedId(),
                    getForm().getExtendedId()),
                this);

        IValidator validator = getValidator();

        if (validator == null)
            throw Tapestry.createRequiredParameterException(this, "validator");

        boolean rendering = !cycle.isRewinding();

        if (rendering)
            delegate.writePrefix(writer, cycle, this, validator);

        super.renderComponent(writer, cycle);

        if (rendering)
            delegate.writeSuffix(writer, cycle, this, validator);

        // If rendering and there's either an error in the field,
        // then we may have identified the default field (which will
        // automatically receive focus).

        if (rendering && delegate.isInError())
            addSelect(cycle);

        // That's OK, but an ideal situation would know about non-validating
        // text fields, and also be able to put the cursor in the
        // first field, period (even if there are no required or error fields).
        // Still, this pretty much rocks!

    }

    /**
     *  Invokes {@link IValidationDelegate#writeAttributes(IMarkupWriter,IRequestCycle)}.
     *
     **/

    protected void beforeCloseTag(IMarkupWriter writer, IRequestCycle cycle)
    {
        IValidator validator = getValidator();

        validator.renderValidatorContribution(this, writer, cycle);

        getForm().getDelegate().writeAttributes(writer, cycle, this, validator);
    }

    private static final String SELECTED_ATTRIBUTE_NAME =
        "org.apache.tapestry.component.html.valid.SelectedFieldSet";

    /**
     *  Creates JavaScript to set the cursor on the first required or error
     *  field encountered while rendering.  This only works if the text field
     *  is wrapped by a {@link Body} component (which is almost always true).
     *
     **/

    private void addSelect(IRequestCycle cycle)
    {
        // If some other field has taken the honors, then let it.

        if (cycle.getAttribute(SELECTED_ATTRIBUTE_NAME) != null)
            return;

        Body body = Body.get(cycle);

        // If not wrapped by a Body, then do nothing.

        if (body == null)
            return;

        IForm form = Form.get(cycle);

        String formName = form.getName();
        String textFieldName = getName();

        String fullName = "document." + formName + "." + textFieldName;

        body.addInitializationScript(fullName + ".focus();");
        body.addInitializationScript(fullName + ".select();");

        // Put a marker in, indicating that the selected field is known.

        cycle.setAttribute(SELECTED_ATTRIBUTE_NAME, Boolean.TRUE);
    }

    protected String readValue()
    {
        IValidationDelegate delegate = getForm().getDelegate();

        if (delegate.isInError())
            return delegate.getFieldInputValue();

        Object value = getValue();
        String result = getValidator().toString(this, value);

        if (StringUtils.isEmpty(result) && getValidator().isRequired())
            addSelect(getPage().getRequestCycle());

        return result;
    }

    protected void updateValue(String value)
    {
        Object objectValue = null;
        IValidationDelegate delegate = getForm().getDelegate();

        delegate.recordFieldInputValue(value);

        try
        {
            objectValue = getValidator().toObject(this, value);
        }
        catch (ValidatorException ex)
        {
            delegate.record(ex);
            return;
        }

        setValue(objectValue);
    }

    public abstract IValidator getValidator();
}