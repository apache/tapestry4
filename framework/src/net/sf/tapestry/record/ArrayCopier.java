package net.sf.tapestry.record;

import java.util.Arrays;

/**
 *  Makes a copy of the array, by invoking <code>clone()</code>.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ArrayCopier implements IValueCopier
{

    public Object makeCopyOfValue(Object value)
    {
        Object[] array = (Object[])value;
        
        return array.clone();
    }

}
