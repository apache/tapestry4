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
package net.sf.tapestry.link;

import java.util.List;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;

/**
 *  A component for creating a link using the direct service; used for actions that
 *  are not dependant on dynamic page state.
 *
 *  [<a href="../../../../../ComponentReference/DirectLink.html">Component Reference</a>]
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class DirectLink extends GestureLink implements IDirect
{
    private IBinding _listenerBinding;
    private Object _parameters;
    private IBinding _statefulBinding;

    public void setStatefulBinding(IBinding value)
    {
        _statefulBinding = value;
    }

    public IBinding getStatefulBinding()
    {
        return _statefulBinding;
    }

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.  May be invoked when not renderring.
     *
     **/

    public boolean isStateful()
    {
        if (_statefulBinding == null)
            return true;

        return _statefulBinding.getBoolean();
    }

    /**
     *  Returns {@link net.sf.tapestry.Tapestry#DIRECT_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return Tapestry.DIRECT_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle)
    {
        return constructServiceParameters(_parameters);
    }

    /**
     *  Converts a service parameters value to an array
     *  of objects.  
     *  This is used by the {@link DirectLink}, {@link ServiceLink}
     *  and {@link ExternalLink}
     *  components.
     *
     *  @param parameterValue the input value which may be
     *  <ul>
     *  <li>null  (returns null)
     *  <li>An array of Object (returns the array)
     *  <li>A {@link List} (returns an array of the values in the List})
     *  <li>A single object (returns the object as a single-element array)
     *  </ul>
     * 
     *  @return An array representation of the input object.
     * 
     *  @since 2.2
     **/

    public static Object[] constructServiceParameters(Object parameterValue)
    {
        if (parameterValue == null)
            return null;

        if (parameterValue instanceof Object[])
            return (Object[]) parameterValue;

        if (parameterValue instanceof List)
        {
            List list = (List) parameterValue;

            return list.toArray();
        }

        return new Object[] { parameterValue };
    }

    /**
     *  Invoked by the direct service to trigger the application-specific
     *  action by notifying the {@link IActionListener listener}.
     *
     *  @throws StaleSessionException if the component is stateful, and
     *  the session is new.
     * 
     **/

    public void trigger(IRequestCycle cycle) throws RequestCycleException
    {
        IActionListener listener = getListener(cycle);

        listener.actionTriggered(this, cycle);
    }

    public IBinding getListenerBinding()
    {
        return _listenerBinding;
    }

    public void setListenerBinding(IBinding value)
    {
        _listenerBinding = value;
    }

    /**
     *  Need to use the listener binding, since this method gets called even when the
     *  component is not rendering.
     * 
     **/

    private IActionListener getListener(IRequestCycle cycle) throws RequestCycleException
    {
        IActionListener result;

        try
        {
            result =
                (IActionListener) _listenerBinding.getObject("listener", IActionListener.class);

        }
        catch (BindingException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        if (result == null)
            throw new RequiredParameterException(this, "listener", _listenerBinding);

        return result;
    }

    /** @since 2.2 **/

    public Object getParameters()
    {
        return _parameters;
    }

    /** @since 2.2. **/

    public void setParameters(Object context)
    {
        _parameters = context;
    }
}