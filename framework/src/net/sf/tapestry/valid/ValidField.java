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
package net.sf.tapestry.valid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.form.AbstractTextField;
import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.html.Body;

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

public abstract class ValidField extends AbstractTextField implements IField, IFormComponent
{
    private static final Map TYPES = new HashMap();

    static {
        TYPES.put("boolean", boolean.class);
        TYPES.put("Boolean", Boolean.class);
        TYPES.put("java.lang.Boolean", Boolean.class);
        TYPES.put("char", char.class);
        TYPES.put("Character", Character.class);
        TYPES.put("java.lang.Character", Character.class);
        TYPES.put("short", short.class);
        TYPES.put("Short", Short.class);
        TYPES.put("java.lang.Short", Short.class);
        TYPES.put("int", int.class);
        TYPES.put("Integer", Integer.class);
        TYPES.put("java.lang.Integer", Integer.class);
        TYPES.put("long", long.class);
        TYPES.put("Long", Long.class);
        TYPES.put("java.lang.Long", Long.class);
        TYPES.put("float", float.class);
        TYPES.put("Float", Float.class);
        TYPES.put("java.lang.Float", Float.class);
        TYPES.put("byte", byte.class);
        TYPES.put("Byte", Byte.class);
        TYPES.put("java.lang.Byte", Byte.class);
        TYPES.put("double", double.class);
        TYPES.put("Double", Double.class);
        TYPES.put("java.lang.Double", Double.class);
        TYPES.put("java.math.BigInteger", BigInteger.class);
        TYPES.put("java.math.BigDecimal", BigDecimal.class);
    }


    /** @since 2.2 **/

    private Class _valueType;

    public abstract IBinding getValueBinding();
    public abstract void setValueBinding(IBinding valueBinding);

    public abstract IBinding getDisplayNameBinding();

    /**
     *  Returns the display name for the component.  Because of the interaction
     *  between {@link FieldLabel} and this component, the displayName parameter
     *  is direction custom, allowing it to be resolved even when not renderring.
     * 
     **/

    public String getDisplayName()
    {
    	return getDisplayNameBinding().getString();
    }

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
        throws RequestCycleException
    {
        IValidationDelegate delegate = getForm().getDelegate();

        if (delegate == null)
            throw new RequestCycleException(
                Tapestry.getString(
                    "ValidField.no-delegate",
                    getExtendedId(),
                    getForm().getExtendedId()),
                this);

        boolean rendering = !cycle.isRewinding();

        delegate.setFormComponent(this);

        if (rendering)
            delegate.writePrefix(writer, cycle);

        super.renderComponent(writer, cycle);

        if (rendering)
            delegate.writeSuffix(writer, cycle);

        // If rendering and there's either an error in the field,
        // or the field is required but the value is currently null,
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
        throws RequestCycleException
    {
        getValidator().renderValidatorContribution(this, writer, cycle);

        getForm().getDelegate().writeAttributes(writer, cycle);
    }

    private static final String SELECTED_ATTRIBUTE_NAME =
        "net.sf.tapestry.component.html.valid.SelectedFieldSet";

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

        body.addOtherInitialization(fullName + ".focus();");
        body.addOtherInitialization(fullName + ".select();");

        // Put a marker in, indicating that the selected field is known.

        cycle.setAttribute(SELECTED_ATTRIBUTE_NAME, Boolean.TRUE);
    }

    protected String readValue() throws RequestCycleException
    {
        IValidationDelegate delegate = getForm().getDelegate();

        if (delegate.isInError())
            return delegate.getInvalidInput();

        Object value = getValueBinding().getObject();
        String result = getValidator().toString(this, value);

        if (Tapestry.isNull(result) && getValidator().isRequired())
            addSelect(getPage().getRequestCycle());

        return result;
    }

    protected void updateValue(String value) throws RequestCycleException
    {
        Object objectValue = null;
        IValidationDelegate delegate = getForm().getDelegate();

        try
        {
            objectValue = getValidator().toObject(this, value);
        }
        catch (ValidatorException ex)
        {
            delegate.record(ex);
            return;
        }

        getValueBinding().setObject(objectValue);
        delegate.reset();
    }

    public abstract IValidator getValidator();
    
    /** @since 2.2. **/

    public abstract String getTypeName();
    
    public Class getValueType()
    {
        if (_valueType == null)
            _valueType = resolveType();

        return _valueType;
    }

    private Class resolveType()
    {
    	String typeName = getTypeName();
    	
        if (typeName == null)
            throw new NullPointerException(Tapestry.getString("ValidField.no-type", this));

        synchronized (TYPES)
        {
            Class result = (Class) TYPES.get(typeName);

            if (result != null)
                return result;
        }

        return getPage().getEngine().getResourceResolver().findClass(typeName);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _valueType = null;

        super.cleanupAfterRender(cycle);
    }

}