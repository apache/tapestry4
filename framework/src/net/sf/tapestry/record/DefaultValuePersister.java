/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.record;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.IImmutable;
import org.apache.commons.lang.enum.Enum;

/**
 *  Default implementation of {@link DefaultValuePersister}.
 * 
 *  <p>
 *  In general, objects that are stored as persistent page properties should 
 *  be immutable (such as String or Integer), implement the 
 *  {@link net.sf.tapestry.util.IImmutable} interface, or
 *  be {@link java.io.Serializable}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class DefaultValuePersister implements IValuePersister
{
    private AdaptorRegistry _registry = new AdaptorRegistry();

    /**
     *  Invoked from {@link #initialize(IRequestCycle)} to register the copier
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
                Tapestry.getString(
                    "DefaultValuePersister.no-value-copier-for-class",
                    valueClass.getName()),
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
     *  An instance of {@link MapCopier} is registered for {@link java.util.Map}.
     * 
     *  <p>
     *  An instance of {@link ArrayCopier} for <code>Object[]</code>.
     * 
     *  <p>
     *  An instance of {@link SerializableCopier} for {@link java.io.Serializable}.
     * 
     *  <p>
     *  Any object that doesn't fit into one of the above categories
     *  is treated as immutable.  This encompasses the J2EE classes
     *  that the application server is responsible for (such as EJBObject and
     *  UserTransaction), but also any user-defined classes (which
     *  may come back to bite you when you turn on clustering).
     *  
     **/

    public void initialize(IRequestCycle cycle)
    {
        IValueCopier immutable = new ImmutableValueCopier();

		registerValueCopier(Object.class, immutable);
        registerValueCopier(IImmutable.class, immutable);
        registerValueCopier(String.class, immutable);
        registerValueCopier(Character.class, immutable);
        registerValueCopier(Number.class, immutable);
        registerValueCopier(Boolean.class, immutable);
        registerValueCopier(Date.class, immutable);
        registerValueCopier(Enum.class, immutable);

        registerValueCopier(List.class, new ListCopier());
        registerValueCopier(Map.class, new MapCopier());

        registerValueCopier(Object[].class, new ArrayCopier());

        registerValueCopier(
            Serializable.class,
            new SerializableCopier(cycle.getEngine().getResourceResolver()));
    }

    protected Object copy(Object value) throws PageRecorderSerializationException
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
