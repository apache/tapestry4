package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Connects a parameter to an int property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class IntParameterConnector extends AbstractParameterConnector
{

    protected IntParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        super(component, parameterName, binding);
    }

    /**
     *  Invokes {@link IBinding#getInt()} to obtain
     *  an int value to assign.
     * 
     **/

    public void setParameter(IRequestCycle cycle) throws RequiredParameterException
    {
        if (shouldSetPropertyValue(cycle))
        {
            int scalar = getBinding().getInt();

            setPropertyValue(new Integer(scalar));
        }
    }

}
