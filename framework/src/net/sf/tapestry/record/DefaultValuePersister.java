package net.sf.tapestry.record;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.Handle;

import org.apache.commons.lang.enum.Enum;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.IImmutable;

/**
 *  Default implementation of {@link DefaultValuePersister}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class DefaultValuePersister implements IValuePersister
{
    private AdaptorRegistry _registry = new AdaptorRegistry();

    public DefaultValuePersister()
    {
        registerValueCopiers();
    }

    /**
     *  Invoked from {@link #registerValueCopiers()} to register the copier
     *  for a particular class.
     * 
     **/

    protected void registerValueCopier(Class registrationClass, IValueCopier copier)
    {
        _registry.register(registrationClass, copier);
    }

    protected IValueCopier getCopier(Object value)
    {
        Class valueClass = value.getClass();

        try
        {
            return (IValueCopier) _registry.getAdaptor(valueClass);
        }
        catch (IllegalArgumentException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultValuePersister.no-value-copier-for-class", valueClass.getName()),
                ex);
        }
    }

    /**
     *  Registers copiers.  Subclasses may override to add additional registrations
     *  beyond the default set.  An {@link net.sf.tapestry.record.ImmutableValueCopier}
     *  instance is registered for
     *  {@link net.sf.tapestry.util.IImmutable},
     *  {@link Enum},
     *  String, Character, Number, Boolean and Date (even though Date is, technically, mutable)
     * 
     *  <p>
     *  An instance of {@link ListCopier} is registered for {@link java.util.List}.
     * 
     *  <p>
     *  An instance of {@link EJBCopier} for {@link EJBObject}, and {@link EJBWrapperCopier}
     *  for {@link EJBWrapper}.
     * 
     *  <p>
     *  An instance of {@link ArrayCopier} for <code>Object[]</code>.
     *  
     **/

    protected void registerValueCopiers()
    {
        IValueCopier immutable = new ImmutableValueCopier();

        registerValueCopier(IImmutable.class, immutable);
        registerValueCopier(String.class, immutable);
        registerValueCopier(Character.class, immutable);
        registerValueCopier(Number.class, immutable);
        registerValueCopier(Boolean.class, immutable);
        registerValueCopier(Date.class, immutable);
        registerValueCopier(Handle.class, immutable);
        registerValueCopier(Enum.class, immutable);

        registerValueCopier(List.class, new ListCopier());
        
        registerValueCopier(EJBObject.class, new EJBCopier());
        registerValueCopier(EJBWrapper.class, new EJBWrapperCopier());
        
        registerValueCopier(Object[].class, new ArrayCopier());
    }

    protected Object copy(Object value)
    {
        IValueCopier copier = getCopier(value);

        return copier.makeCopyOfValue(value);
    }

    public Object convertToActiveValue(Object value) throws PageRecorderSerializationException
    {
        if (value == null)
            return null;

        return copy(value);
    }

    public Object convertToStorableValue(Object value) throws PageRecorderSerializationException
    {
        if (value == null)
            return null;

        return copy(value);
    }
}
