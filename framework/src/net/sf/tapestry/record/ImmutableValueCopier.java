package net.sf.tapestry.record;

/**
 *  Copier used when the value is immutable (and thus, no copy is really needed).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ImmutableValueCopier implements IValueCopier
{
    public Object makeCopyOfValue(Object value)
    {
        return value;
    }

}
