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

package org.apache.tapestry.junit.mock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpSession}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockSession extends AttributeHolder implements HttpSession
{
    private MockContext _context;
    private String _id;

    public MockSession(MockContext context, String id)
    {
        _context = context;
        _id = id;
    }

    public long getCreationTime()
    {
        return 0;
    }

    public String getId()
    {
        return _id;
    }

    public long getLastAccessedTime()
    {
        return 0;
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public void setMaxInactiveInterval(int arg0)
    {
    }

    public int getMaxInactiveInterval()
    {
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        return null;
    }

    public Object getValue(String name)
    {
        return getAttribute(name);
    }

    public String[] getValueNames()
    {
        return getAttributeNamesArray();
    }

    public void putValue(String name, Object value)
    {
        setAttribute(name, value);
    }

    public void removeValue(String name)
    {
        removeAttribute(name);
    }

    public void invalidate()
    {
    }

    public boolean isNew()
    {
        return false;
    }

    public void setAttribute(String name, Object value)
    {
        super.setAttribute(name, value);

        if (value instanceof HttpSessionBindingListener)
        {
            HttpSessionBindingListener listener = (HttpSessionBindingListener) value;
            HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name, value);

            listener.valueBound(event);
        }
    }

}
