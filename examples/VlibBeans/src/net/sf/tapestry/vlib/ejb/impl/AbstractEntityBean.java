package net.sf.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import net.sf.tapestry.contrib.ejb.XEJBException;
import net.sf.tapestry.vlib.ejb.IKeyAllocator;
import net.sf.tapestry.vlib.ejb.IKeyAllocatorHome;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Provides basic support for the entity context, empty or minimal
 *  implementations of the required methods, and some utilties.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class AbstractEntityBean implements EntityBean
{
    /**
     *  The EntityContext provided by the application server.
     *
     **/

    private EntityContext _context;

    private transient String[] _attributePropertyNames;

    private transient IKeyAllocatorHome _keyAllocatorHome;

    /**
     *  The environment naming context, which is configured for this bean
     *  in the deployment descriptor.
     *
     **/

    private transient Context environment;

    public void setEntityContext(EntityContext context)
    {
        _context = context;
    }

    public void unsetEntityContext()
    {
        _context = null;
    }

    /**
     *  Gets a named object from the bean's environment naming context.
     *
     **/

    protected Object getEnvironmentObject(String name, Class objectClass) throws RemoteException, NamingException
    {
        Object raw;
        Object result;
        Context initial;

        if (environment == null)
        {
            initial = new InitialContext();
            environment = (Context) initial.lookup("java:comp/env");
        }

        raw = environment.lookup(name);

        try
        {
            result = PortableRemoteObject.narrow(raw, objectClass);
        }
        catch (ClassCastException e)
        {
            throw new RemoteException("Could not narrow " + raw + " (" + name + ") to class " + objectClass + ".");
        }

        return result;
    }

    /**
     *  Empty implementation; subclasses may override.
     *
     **/

    public void ejbActivate() throws EJBException, RemoteException
    {
        // does nothing
    }

    /**
     *  Empty implementation; subclasses may override.
     *
     **/

    public void ejbPassivate() throws EJBException, RemoteException
    {
        // does nothing
    }

    /**
     *  Empty implementation; subclasses may override.
     *
     **/

    public void ejbRemove() throws EJBException, RemoteException
    {
        // does nothing
    }

    /**
     *  Does nothing.
     *
     **/

    public void ejbLoad() throws EJBException, RemoteException
    {
    }

    /**
     *  Does nothing.
     *
     **/

    public void ejbStore() throws EJBException, RemoteException
    {
    }

    /**
     *  Uses the KeyAllocator session bean to allocate a necessary key.
     *
     **/

    protected Integer allocateKey() throws RemoteException
    {
        Context initial;
        Context environment;
        IKeyAllocator allocator;
        Object raw;

        if (_keyAllocatorHome == null)
        {
            try
            {
                initial = new InitialContext();
                environment = (Context) initial.lookup("java:comp/env");

                raw = environment.lookup("ejb/KeyAllocator");
                _keyAllocatorHome = (IKeyAllocatorHome) PortableRemoteObject.narrow(raw, IKeyAllocatorHome.class);
            }
            catch (NamingException e)
            {
                throw new XEJBException("Unable to locate IKeyAllocatorHome.", e);
            }
        }

        // Get a reference to *some* KeyAllocator bean ... it may be fresh,
        // or one reused from a pool.

        try
        {
            allocator = _keyAllocatorHome.create();
        }
        catch (CreateException e)
        {
            throw new RemoteException("Unable to create a KeyAllocator from " + _keyAllocatorHome + ".", e);
        }

        // Finally, invoke the method that gets a key.

        return allocator.allocateKey();
    }

    /**
     *  Implemented in subclasses to provide a list of property names to be included
     *  in the entity attributes map.
     *
     **/

    protected abstract String[] getAttributePropertyNames();

    /**
     *  Returns a {@link Map} of the properties of the bean.  This Map is
     *  returned to the client, where it can be modified and then used to update
     *  the entity bean in a single method
     *
     *  <p>The properties included in the Map are defined by the
     *  {@link #getAttributePropertyNames()} method, which is implemented
     *  by concrete subclasses.
     *
     **/

    public Map getEntityAttributes()
    {
        Map result = new HashMap();

        if (_attributePropertyNames == null)
            _attributePropertyNames = getAttributePropertyNames();

        for (int i = 0; i < _attributePropertyNames.length; i++)
        {
            String key = _attributePropertyNames[i];

            try
            {
                Object value = Ognl.getValue(key, this);

                result.put(key, value);
            }
            catch (OgnlException ex)
            {
            }
        }

        return result;
    }

    /**
     *  Updates the bean with property changes from the update {@link Map}.
     *  Only the keys defined by {@link #getAttributePropertyNames()} will be
     *  accessed (keys and values that are not in that list are ignored). 
     *
     *  <p>The corresponding bean property will only be updated
     *  if the key is present ... this means that the update may contain just
     *  the <em>changed</em> keys.  Remember that a Map may store null values.
     *
     **/

    public void updateEntityAttributes(Map update)
    {
        if (_attributePropertyNames == null)
            _attributePropertyNames = getAttributePropertyNames();

        for (int i = 0; i < _attributePropertyNames.length; i++)
        {
            String key = _attributePropertyNames[i];

            if (update.containsKey(key))
            {
                Object value = update.get(key);

                try
                {
                    Ognl.setValue(key, this, value);
                }
                catch (OgnlException ex)
                {
                }

            }

        }

    }

    protected void setContext(EntityContext context)
    {
        this._context = context;
    }

    protected EntityContext geEntityContext()
    {
        return _context;
    }
}