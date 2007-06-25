// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.portlet;

import static org.easymock.EasyMock.expect;

import java.util.List;

import org.apache.tapestry.web.WebSession;
import org.testng.annotations.Test;

import javax.portlet.PortletSession;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class PortletWebSessionTest extends BasePortletWebTestCase
{

    public void testGetAttributeNames()
    {
        PortletSession session = newSession();

        expect(session.getAttributeNames()).andReturn(newEnumeration());

        replay();

        WebSession ws = new PortletWebSession(session);
        
        List l = ws.getAttributeNames();

        checkList(l);

        verify();
    }

    private PortletSession newSession()
    {
        return newMock(PortletSession.class);
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        PortletSession session = newSession();

        expect(session.getAttribute("attr")).andReturn(attribute);
        
        replay();

        WebSession ws = new PortletWebSession(session);

        assertSame(attribute, ws.getAttribute("attr"));

        verify();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        PortletSession session = newSession();

        session.setAttribute("name", attribute);

        replay();

        WebSession ws = new PortletWebSession(session);

        ws.setAttribute("name", attribute);

        verify();
    }

    public void testGetId()
    {
        PortletSession session = newSession();

        expect(session.getId()).andReturn("abc");
        
        replay();

        WebSession ws = new PortletWebSession(session);

        assertEquals("abc", ws.getId());

        verify();
    }

    public void testInvalidate()
    {
        PortletSession session = newSession();

        session.invalidate();

        replay();

        WebSession ws = new PortletWebSession(session);

        ws.invalidate();

        verify();
    }
}