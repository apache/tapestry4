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

import javax.portlet.PortletURL;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.QueryParameterMap;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletLink}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletLink extends HiveMindTestCase
{
    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private PortletURL newPortletURL()
    {
        return (PortletURL) newMock(PortletURL.class);
    }

    private QueryParameterMap newParameters()
    {
        return (QueryParameterMap) newMock(QueryParameterMap.class);
    }

    public void testGetAbsoluteURL()
    {
        IRequestCycle cycle = newCycle();
        PortletURL url = newPortletURL();
        QueryParameterMap parameters = newParameters();

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, false);

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

        verifyControls();
    }

    public void testGetParameterNames()
    {
        IRequestCycle cycle = newCycle();
        PortletURL url = newPortletURL();

        MockControl control = newControl(QueryParameterMap.class);
        QueryParameterMap parameters = (QueryParameterMap) control.getMock();

        String[] names =
        { "Fred", "Barney" };

        parameters.getParameterNames();
        control.setReturnValue(names);

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, false);

        assertSame(names, link.getParameterNames());

        verifyControls();
    }

    public void testGetParameterValues()
    {
        IRequestCycle cycle = newCycle();
        PortletURL url = newPortletURL();

        MockControl control = newControl(QueryParameterMap.class);
        QueryParameterMap parameters = (QueryParameterMap) control.getMock();

        String[] values =
        { "Fred", "Barney" };

        parameters.getParameterValues("bedrock");
        control.setReturnValue(values);

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, false);

        assertSame(values, link.getParameterValues("bedrock"));

        verifyControls();
    }

    public void testGetURL()
    {
        IRequestCycle cycle = newCycle();
        PortletURL url = newPortletURL();

        MockControl control = newControl(QueryParameterMap.class);
        QueryParameterMap parameters = (QueryParameterMap) control.getMock();

        parameters.getParameterNames();
        control.setReturnValue(new String[0]);

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, false);

        assertEquals(url.toString(), link.getURL());

        verifyControls();
    }

    public void testGetURLIncludeParameters()
    {
        IRequestCycle cycle = newCycle();
        PortletURL url = newPortletURL();

        MockControl control = newControl(QueryParameterMap.class);
        QueryParameterMap parameters = (QueryParameterMap) control.getMock();

        String[] values =
        { "Fred", "Barney" };

        parameters.getParameterNames();
        control.setReturnValue(new String[]
        { "bedrock" });
        parameters.getParameterValues("bedrock");
        control.setReturnValue(values);

        url.setParameter("bedrock", values);

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, false);

        assertEquals(url.toString(), link.getURL());

        verifyControls();
    }

    public void testGetURLStatefulWithAnchor()
    {
        PortletURL url = newPortletURL();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.encodeURL(url.toString());
        cyclec.setReturnValue("/encoded-url");

        QueryParameterMap parameters = newParameters();

        replayControls();

        ILink link = new PortletLink(cycle, url, parameters, true);

        assertEquals("/encoded-url#anchor", link.getURL("anchor", false));

        verifyControls();
    }
}