package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
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

        ParameterSpecification pspec = _component.getSpecification().getParameter(_parameterName);
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