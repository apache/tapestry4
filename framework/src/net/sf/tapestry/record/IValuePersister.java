package net.sf.tapestry.record;

/**
 *  Responsible for persisting values on behalf of a {@link net.sf.tapestry.IPageRecorder}.
 *  The values provided to the page recorder are not stored as is; there are several
 *  conversions that occur:
 *  <ul>
 *  <li>{@link javax.ejb.EJBObject} is wrapped up in a {@link net.sf.tapestry.record.EJBWrapper}
 *  <li>Non-immutable objects are copied, using a {@link net.sf.tapestry.record.IValueCopier}
 *  <li>null passes through unchanged
 *  </ul>
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public interface IValuePersister
{
    /**
     *  Converts an active value (used by the application) to
     *  a storable value (a value which can be persisted for later access).
     * 
     **/
    
    public Object convertToStorableValue(Object value)
    throws PageRecorderSerializationException;
    
    /**
     *  Reverses {@link #convertToStorableValue(Object)}, converting values
     *  back into active values.
     * 
     **/
    
    public Object convertToActiveValue(Object value)throws PageRecorderSerializationException;
}
