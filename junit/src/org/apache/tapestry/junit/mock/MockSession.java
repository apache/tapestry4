//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
            HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name);

            listener.valueBound(event);
        }
    }

}
