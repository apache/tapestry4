package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Creates a connection between a parameter and a property of type {@link String}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class StringParameterConnector extends AbstractParameterConnector
{

    protected StringParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        super(component, parameterName, binding);
    }

    /**
     *  Invokes {@link IBinding#getString()} to obtain the property
     *  value.
     * 
     **/

    public void setParameter(IRequestCycle cycle) throws RequiredParameterException
    {
        if (shouldSetPropertyValue(cycle))
            setPropertyValue(getBinding().getString());
    }
}
