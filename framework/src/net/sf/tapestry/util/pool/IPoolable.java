package net.sf.tapestry.util.pool;

/**
 *  Marks an object as being aware that is to be stored into a {@link Pool}.
 *  This gives the object a last chance to reset any state.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public interface IPoolable
{
    /**
     *  Invoked by a {@link net.sf.tapestry.util.pool.Pool} 
     *  just before the object is added to the pool.
     *  The object should return its state to how it was when freshly instantiated
     *  (or at least, its state should be indistinguishable from a freshly
     *  instantiated instance).
     *
     **/

    public void resetForPool();
}