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

package net.sf.tapestry.valid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
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

public class ValidField extends AbstractTextField implements IField, IFormComponent
{
    private static final Map TYPES = new HashMap();
    
    static
    {
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
        
    private IBinding _valueBinding;

    // Can't be an "in" parameter, because FieldLabel may request it when the
    // ValidField is not renderring.
    
    private IBinding _displayNameBinding;
    private String _displayNameValue;

    private IValidator _validator;

    /** @since 2.2 **/
    
    private String _typeName;
    private Class _valueType;

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    public IBinding getDisplayNameBinding()
    {
        return _displayNameBinding;
    }

    public void setDisplayNameBinding(IBinding value)
    {
        _displayNameBinding = value;

        if (value.isInvariant())
            _displayNameValue = value.getString();
    }

    /**
     *  Returns the display name for the component.  Because of the interaction
     *  between {@link FieldLabel} and this component, the displayName parameter
     *  is direction custom, allowing it to be resolved even when not renderring.
     * 
     **/

    public String getDisplayName()
    {
        // Return the static value, if known.

        if (_displayNameValue != null)
            return _displayNameValue;

        // Otherwise, a dynamic value (how strange).

        return _displayNameBinding.getString();
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
                Tapestry.getString("ValidField.no-delegate", getExtendedId(), getForm().getExtendedId()),
                this);

        String displayName = null;

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
        _validator.renderValidatorContribution(this, writer, cycle);
        
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

        Object value = _valueBinding.getObject();
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
            objectValue = _validator.toObject(this, value);
        }
        catch (ValidatorException ex)
        {
            delegate.record(ex);
            return;
        }

        _valueBinding.setObject(objectValue);
        delegate.reset();
    }

    public IValidator getValidator()
    {
        return _validator;
    }

    public void setValidator(IValidator validator)
    {
        _validator = validator;
    }

    /** @since 2.2. **/
    
    public String getTypeName()
    {
        return _typeName;
    }
    
    /** @since 2.2 **/
    
    public void setTypeName(String typeName)
    {
        _typeName = typeName;
    }

    public Class getValueType()
    {
        if (_valueType == null)
            _valueType = resolveType();
        
        return _valueType;
    }

    private Class resolveType()
    {
        if (_typeName == null)
        throw new NullPointerException(
        Tapestry.getString("ValidField.no-type", this));
        
        synchronized (TYPES)
        {
            Class result = (Class)TYPES.get(_typeName);
            
            if (result != null)
                return result;
        }
        
        return getPage().getEngine().getResourceResolver().findClass(_typeName);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _valueType = null;
        
        super.cleanupAfterRender(cycle);
    }

}