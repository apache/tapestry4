package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;

/**
 *   Connector for boolean parameters.
 * 
 *   @see IBinding#getBoolean()
 * 
 *   @author Howard Lewis Ship
 *   @version $Id$
 *   @since 2.0.3
 * 
 **/

public class BooleanParameterConnector extends AbstractParameterConnector
{

    protected BooleanParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        super(component, parameterName, binding);
    }

    /**
     *  Invokes {@link IBinding#getBoolean()}, which always
     *  returns true or false (there is no concept of a null
     *  value).
     * 
     **/

    public void setParameter(IRequestCycle cycle)
    {
        if (shouldSetPropertyValue(cycle))
        {
            boolean value = getBinding().getBoolean();

            setPropertyValue(value ? Boolean.TRUE : Boolean.FALSE);
        }
    }

}
