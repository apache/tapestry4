package net.sf.tapestry.record;

import java.util.HashMap;
import java.util.Map;

/**
 *  Copies a {@link java.util.Map} by creating a 
 *  {@link java.util.HashMap}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class MapCopier implements IValueCopier
{
    public Object makeCopyOfValue(Object value) throws PageRecorderSerializationException
    {
        Map inputMap = (Map)value;
        
        return new HashMap(inputMap);        
    }
}
