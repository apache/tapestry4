package net.sf.tapestry.record;

import javax.ejb.EJBObject;

/**
 *  Makes a copy of an EJB reference ({@link javax.ejb.EJBObject}) by wrapping
 *  the EJB in a {@link net.sf.tapestry.record.EJBWrapper}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class EJBCopier implements IValueCopier
{

    public Object makeCopyOfValue(Object value)
    {
        EJBObject ejb = (EJBObject) value;

        return new EJBWrapper(ejb);
    }

}
