package net.sf.tapestry.util.pool;

/**
 *  A default, empty implementation, for objects that
 *  have no special behavior related to being pooled.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class NullPoolableAdaptor implements IPoolableAdaptor
{

    public void resetForPool(Object object)
    {
    }

    public void discardFromPool(Object object)
    {
    }

}
