package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Connects a parameter to a property of type double.
 * 
 *  @author Howard Lewis Ship 
 *  @version $Id$
 *  @since 2.0.3
 * 
 **/

public class DoubleParameterConnector extends AbstractParameterConnector
{

    protected DoubleParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        super(component, parameterName, binding);
    }

    /**
     *  Invokes {@link IBinding#getDouble()} to obtain the value
     *  to assign to the property.
     * 
     **/

    public void setParameter(IRequestCycle cycle) throws RequiredParameterException
    {
        if (shouldSetPropertyValue(cycle))
        {
            double scalar = getBinding().getDouble();

            setPropertyValue(new Double(scalar));
        }
    }

}
