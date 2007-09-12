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

import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.QueryParameterMap;
import org.testng.annotations.Test;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletLink}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletLink extends BaseComponentTestCase
{
    private static class PortletURLFixture implements PortletURL
    {
        private final String _toString;

        public PortletURLFixture(String toString)
        {
            _toString = toString;
        }

        public String toString()
        {
            return _toString;
        }

        public void setWindowState(WindowState arg0) throws WindowStateException
        {
        }

        public void setPortletMode(PortletMode arg0) throws PortletModeException
        {
        }

        public void setParameter(String arg0, String arg1)
        {
        }

        public void setParameter(String arg0, String[] arg1)
        {
        }

        public void setParameters(Map arg0)
        {
        }

        public void setSecure(boolean arg0) throws PortletSecurityException
        {
        }

    }

    private PortletURL newPortletURL()
    {
        return newMock(PortletURL.class);
    }

    public void testGetAbsoluteURL()
    {
        PortletURL url = newPortletURL();
        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        replay();
        org.easymock.classextension.EasyMock.replay(parameters);
        
        ILink link = new PortletLink(url, parameters);

        try
        {
            link.getAbsoluteURL();
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Accept.
        }

        try
        {
            link.getAbsoluteURL(null, null, -1, null, false);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Accept.
        }

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetParameterNames()
    {
        PortletURL url = newPortletURL();
        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        String[] names = { "Fred", "Barney" };

        expect(parameters.getParameterNames()).andReturn(names);
        
        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);
        
        assertSame(names, link.getParameterNames());
        
        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }
    
    public void testGetParameterValues()
    {
        PortletURL url = newPortletURL();
        
        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        String[] values = { "Fred", "Barney" };

        expect(parameters.getParameterValues("bedrock")).andReturn(values);
        
        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);
        
        assertSame(values, link.getParameterValues("bedrock"));

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetURL()
    {
        PortletURL url = newPortletURL();

        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        expect(parameters.getParameterNames()).andReturn(new String[0]);
        
        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);

        assertEquals(url.toString(), link.getURL());

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetURLLongForm()
    {
        PortletURL url = newPortletURL();

        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        expect(parameters.getParameterNames()).andReturn(new String[] {"page"});
        
        String[] values = new String[] { "View" };
        
        expect(parameters.getParameterValues("page")).andReturn(values);
        
        url.setParameter("page", values);
        
        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);

        assertEquals("EasyMock for interface javax.portlet.PortletURL#anchor", link.getURL(
                "scheme",
                "server",
                99,
                "anchor",
                true));

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetURLUnencoding()
    {
        PortletURL url = new PortletURLFixture("this=foo&amp;that=bar");

        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        expect(parameters.getParameterNames()).andReturn(new String[0]);
        
        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);

        assertEquals("this=foo&that=bar", link.getURL());

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetURLIncludeParameters()
    {
        PortletURL url = newPortletURL();
        
        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);
        
        String[] values = { "Fred", "Barney" };
        
        expect(parameters.getParameterNames()).andReturn(new String[]{"bedrock"});
        
        expect(parameters.getParameterValues("bedrock")).andReturn(values);
        
        url.setParameter("bedrock", values);

        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);

        assertEquals(url.toString(), link.getURL());

        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }

    public void testGetURLWithAnchor()
    {
        PortletURL url = newPortletURL();

        QueryParameterMap parameters = org.easymock.classextension.EasyMock.createMock(QueryParameterMap.class);

        replay();
        org.easymock.classextension.EasyMock.replay(parameters);

        ILink link = new PortletLink(url, parameters);

        assertEquals("EasyMock for interface javax.portlet.PortletURL#anchor", link.getURL("anchor", false));
        
        verify();
        org.easymock.classextension.EasyMock.verify(parameters);
    }
}
