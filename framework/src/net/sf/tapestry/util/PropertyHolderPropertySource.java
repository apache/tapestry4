package net.sf.tapestry.util;

import net.sf.tapestry.IPropertySource;

/**
 *  Implements the {@link net.sf.tapestry.IPropertySource} interface
 *  for instances that implement {@link net.sf.tapestry.util.IPropertyHolder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class PropertyHolderPropertySource implements IPropertySource
{
    private IPropertyHolder _holder;
    
    public PropertyHolderPropertySource(IPropertyHolder holder)
    {
        _holder = holder;
    }

    public String getPropertyValue(String propertyName)
    {
        return _holder.getProperty(propertyName);
    }

}
