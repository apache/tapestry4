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

package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.prop.OgnlUtils;
import ognl.Ognl;

/**
 *  Standard implementation of {@link IParameterConnector}.
 *  Subclasses add in the ability to clear parameters.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
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

    /**
     *  Parsed OGNL expression, used to get and set the value.
     * 
     *  @since 2.2
     * 
     **/

    private Object _expression;

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

        ParameterSpecification pspec = _component.getSpecification().getParameter(_parameterName);
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

    protected Object getBindingValue(Class requiredType) throws RequiredParameterException
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
        buffer.append(_direction.getEnumerationId());

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
     *  the binding.  For {@link net.sf.tapestry.spec.Direction#IN}, this
     *  always returns true.  For {@link net.sf.tapestry.spec.Direction#FORM},
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