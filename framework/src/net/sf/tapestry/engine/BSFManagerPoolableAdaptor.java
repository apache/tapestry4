package net.sf.tapestry.engine;

import org.apache.bsf.BSFManager;

import net.sf.tapestry.util.pool.IPoolableAdaptor;

/**
 *  Allows a {@link net.sf.tapestry.util.pool.Pool} to
 *  properly terminate a {@link org.apache.bsf.BSFManager}
 *  when it is discarded.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class BSFManagerPoolableAdaptor implements IPoolableAdaptor
{
    /**
     *  Does nothing.
     * 
     **/
    
    public void resetForPool(Object object)
    {
    }

    /**
     *  Invokes {@link org.apache.bsf.BSFManager#terminate()}.
     * 
     **/
    
    public void discardFromPool(Object object)
    {
        BSFManager manager = (BSFManager)object;
        
        manager.terminate();
    }

}
