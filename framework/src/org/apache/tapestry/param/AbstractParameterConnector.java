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

package org.apache.tapestry.param;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;
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
    private IResourceResolver _resolver;

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

        _resolver = component.getPage().getEngine().getResourceResolver();

        IParameterSpecification pspec = _component.getSpecification().getParameter(_parameterName);
        _required = pspec.isRequired();
        _propertyName = pspec.getPropertyName();
        _direction = pspec.getDirection();

        _clearValue = readCurrentPropertyValue();
    }

    /** @since 2.2 **/

    private Object readCurrentPropertyValue()
    {
        return OgnlUtils.get(_propertyName, _resolver, _component);
    }

    /**
     *  Sets the property of the component to the specified value.
     * 
     **/

    protected void setPropertyValue(Object value)
    {
        OgnlUtils.set(_propertyName, _resolver, _component, value);
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