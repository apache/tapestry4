package net.sf.tapestry.util.pool;

/**
 *  Defines methods that define an adaptor to provide
 *  {@link net.sf.tapestry.util.pool.IPoolable}
 *  type behavior to arbitrary objects.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public interface IPoolableAdaptor
{
    /**
     *  Invoked just as an object is returned to the pool; this
     *  allows it to reset any state back to newly initialized.
     * 
     **/
    
    public void resetForPool(Object object);
    
    /**
     *  Invoked when a pooled object is discarded from the pool.
     * 
     **/
    
    public void discardFromPool(Object object);
}
