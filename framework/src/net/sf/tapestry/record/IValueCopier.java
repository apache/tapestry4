package net.sf.tapestry.record;

/**
 *  Interface used to define how to make a copy of an object to 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public interface IValueCopier
{
    /**
     *  Passed an object, this should make a copy of that object.
     * 
     **/
    
    public Object makeCopyOfValue(Object value);
}
