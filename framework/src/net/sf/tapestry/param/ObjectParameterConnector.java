package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Implements {@link IParameterConnector} for object parameters.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class ObjectParameterConnector extends AbstractParameterConnector
{
    private Class _requiredType;

    protected ObjectParameterConnector(
        IComponent component,
        String parameterName,
        IBinding binding,
        Class requiredType)
    {
        super(component, parameterName, binding);

        _requiredType = requiredType;
    }

    /**
     *  Sets the parameter property to null.
     * 
     **/

    public void setParameter(IRequestCycle cycle) throws RequiredParameterException
    {
        if (shouldSetPropertyValue(cycle))
            setPropertyValue(getBindingValue(_requiredType));
    }
}