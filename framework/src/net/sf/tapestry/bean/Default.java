package net.sf.tapestry.bean;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.util.pool.IPoolable;

/**
 *  A helper bean to assist with providing defaults for unspecified
 *  parameters.    It is initalized
 *  with an {@link IBinding} and a default value.  It's value property
 *  is either the value of the binding, but if the binding is null,
 *  or the binding returns null, the default value is returned.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class Default implements IPoolable
{
    private IBinding binding;
    private Object defaultValue;

    public void resetForPool()
    {
        binding = null;
        defaultValue = null;
    }

    public void setBinding(IBinding value)
    {
        binding = value;
    }

    public IBinding getBinding()
    {
        return binding;
    }

    public void setDefaultValue(Object value)
    {
        defaultValue = value;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    /**
     *  Returns the value of the binding.  However, if the binding is null, or the binding
     *  returns null, then the defaultValue is returned instead.
     *
     **/

    public Object getValue()
    {
        if (binding == null)
            return defaultValue;

        Object value = binding.getObject();

        if (value == null)
            return defaultValue;

        return value;
    }
    
    /** @since 2.4 **/
    
    public void discardFromPool()
    {
    }

}