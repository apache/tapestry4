package net.sf.tapestry.record;

import java.util.ArrayList;
import java.util.List;

/**
 *  Makes a copy of a {@link java.util.List} by
 *  creating a new {@link java.util.ArrayList}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/
public class ListCopier implements IValueCopier
{

    public Object makeCopyOfValue(Object value)
    {
        List list = (List)value;
        
        return new ArrayList(list);
    }

}
