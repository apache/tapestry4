/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.ejb.impl;

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

import org.apache.tapestry.contrib.ejb.XEJBException;
import org.apache.tapestry.vlib.ejb.IKeyAllocator;
import org.apache.tapestry.vlib.ejb.IKeyAllocatorHome;
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

    private transient Context _environment;

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

    protected Object getEnvironmentObject(String name, Class objectClass)
        throws RemoteException, NamingException
    {
        Object result = null;

        if (_environment == null)
        {
            Context initial = new InitialContext();
            _environment = (Context) initial.lookup("java:comp/env");
        }

        Object raw = _environment.lookup(name);

        try
        {
            result = PortableRemoteObject.narrow(raw, objectClass);
        }
        catch (ClassCastException ex)
        {
            throw new RemoteException(
                "Could not narrow " + raw + " (" + name + ") to class " + objectClass + ".");
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
        IKeyAllocator allocator;

        if (_keyAllocatorHome == null)
        {
            try
            {
                Context initial = new InitialContext();
                Context environment = (Context) initial.lookup("java:comp/env");

                Object raw = environment.lookup("ejb/KeyAllocator");
                _keyAllocatorHome =
                    (IKeyAllocatorHome) PortableRemoteObject.narrow(raw, IKeyAllocatorHome.class);
            }
            catch (NamingException ex)
            {
                throw new XEJBException("Unable to locate IKeyAllocatorHome.", ex);
            }
        }

        // Get a reference to *some* KeyAllocator bean ... it may be fresh,
        // or one reused from a pool.

        try
        {
            allocator = _keyAllocatorHome.create();
        }
        catch (CreateException ex)
        {
            throw new RemoteException(
                "Unable to create a KeyAllocator from " + _keyAllocatorHome + ".",
                ex);
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
        _context = context;
    }

    protected EntityContext geEntityContext()
    {
        return _context;
    }
}