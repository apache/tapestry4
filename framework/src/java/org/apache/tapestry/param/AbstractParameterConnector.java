// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.param;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Standard implementation of {@link IParameterConnector}.
 *  Subclasses add in the ability to clear parameters.
 *
 *  @author Howard Lewis Ship
 *  @since 2.0.3
 * 
 **/

public abstract class AbstractParameterConnector implements IParameterConnector
{
    private String _parameterName;
    private String _propertyName;
    private IBinding _binding;
    private IComponent _component;
    private boolean _required;
    private Object _clearValue;
    private Direction _direction;
    private ClassResolver _resolver;

    /**
     *  Creates a connector.  In addition, obtains the current value
     *  of the component property; this value will be used to
     *  restore the component property.
     * 
     **/

    protected AbstractParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        _component = component;
        _parameterName = parameterName;
        _binding = binding;

        IParameterSpecification pspec = _component.getSpecification().getParameter(_parameterName);
        _required = pspec.isRequired();
        _propertyName = pspec.getPropertyName();
        _direction = pspec.getDirection();

        _clearValue = readCurrentPropertyValue();
    }

    /** @since 2.2 **/

    private Object readCurrentPropertyValue()
    {
        return OgnlUtils.get(_propertyName, _component);
    }

    /**
     *  Sets the property of the component to the specified value.
     * 
     **/

    protected void setPropertyValue(Object value)
    {
        OgnlUtils.set(_propertyName, _component, value);
    }

    /**
     *  Gets the value of the binding.
     *  @param requiredType if not null, the expected type of the value object.
     * 
     * 
     *  @see IBinding#getObject()
     *  @see IBinding#getObject(String, Class)
     **/

    protected Object getBindingValue(Class requiredType)
    {
        Object result;

        if (requiredType == null)
            result = _binding.getObject();
        else
            result = _binding.getObject(_parameterName, requiredType);

        return result;
    }

    protected IBinding getBinding()
    {
        return _binding;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append('[');
        buffer.append(_component.getExtendedId());
        buffer.append(' ');
        buffer.append(_parameterName);
        buffer.append(' ');
        buffer.append(_binding);

        buffer.append(' ');
        buffer.append(_direction.getName());

        if (_required)
            buffer.append(" required");

        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Restores the property to its default value.  For
     *  {@link Direction#FORM} parameters, extracts the
     *  property value and sets the binding form it
     *  (when appropriate).
     * 
     **/

    public void resetParameter(IRequestCycle cycle)
    {
        if (_direction == Direction.FORM && cycle.isRewinding())
        {
            IFormComponent component = (IFormComponent) _component;

            if (!component.isDisabled())
            {
                IForm form = Form.get(cycle);

                if (form != null && form.isRewinding())
                {
                    Object value = readCurrentPropertyValue();

                    _binding.setObject(value);
                }
            }
        }

        // Either way, clear the value.

        setPropertyValue(_clearValue);
    }

    /**
     *  Returns true if the connector should update the property value from
     *  the binding.  For {@link org.apache.tapestry.spec.Direction#IN}, this
     *  always returns true.  For {@link org.apache.tapestry.spec.Direction#FORM},
     *  this returns true only if the request cycle and the active form
     *  are rewinding.
     * 
     *  @since 2.2
     * 
     **/

    protected boolean shouldSetPropertyValue(IRequestCycle cycle)
    {
        if (_direction == Direction.IN)
            return true;

        // Must be FORM

        return !cycle.isRewinding();
    }

}