/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.link;

import java.util.List;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;

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

public abstract class DirectLink extends AbstractLinkComponent implements IDirect
{

    public abstract IBinding getStatefulBinding();
    public abstract IActionListener getListener();

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.  May be invoked when not renderring.
     *
     **/

    public boolean isStateful()
    {
        IBinding statefulBinding = getStatefulBinding();

        if (statefulBinding == null)
            return true;

        return statefulBinding.getBoolean();
    }

    public ILink getLink(IRequestCycle cycle)
    {
        return getLink(cycle, Tapestry.DIRECT_SERVICE, constructServiceParameters(getParameters()));
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

    public void trigger(IRequestCycle cycle)
    {
        IActionListener listener = getListener();
        
        if (listener == null)
        	throw Tapestry.createRequiredParameterException(this, "listener");

        listener.actionTriggered(this, cycle);
    }

    /** @since 2.2 **/

    public abstract Object getParameters();
}