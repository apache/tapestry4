package net.sf.tapestry.util.pool;

/**
 *  Adaptor for {@link java.lang.StringBuffer}, that clears
 *  the buffer as it is returned to the pool.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class StringBufferAdaptor extends NullPoolableAdaptor
{

    /**
     *  Sets the length of the {@link java.lang.StringBuffer}
     *  to zero.
     * 
     **/
    
    public void resetForPool(Object object)
    {
        StringBuffer buffer = (StringBuffer)object;
        
        buffer.setLength(0);
    }

}
