package net.sf.tapestry.record;

/**
 *  Converts an {@link net.sf.tapestry.record.EJBWrapper}
 *  back into an {@link javax.ejb.EJBObject}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class EJBWrapperCopier implements IValueCopier
{
    public Object makeCopyOfValue(Object value)
    {
        EJBWrapper wrapper = (EJBWrapper)value;
        
        return wrapper.getEJBObject();
    }

}
