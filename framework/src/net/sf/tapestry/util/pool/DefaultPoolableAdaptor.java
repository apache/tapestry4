package net.sf.tapestry.util.pool;

/**
 *  Implementation for objects that implement
 *  the {@link net.sf.tapestry.util.pool.IPoolable} interface.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class DefaultPoolableAdaptor implements IPoolableAdaptor
{

    public void resetForPool(Object object)
    {
        IPoolable poolable = (IPoolable)object;
        
        poolable.resetForPool();
    }

    public void discardFromPool(Object object)
    {
        IPoolable poolable = (IPoolable)object;
        
        poolable.discardFromPool();        
    }

}
