// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.easymock.MockControl;

/**
 * Common utilities for building tests for {@link org.apache.tapestry.engine.IEngineService}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public abstract class ServiceTestCase extends HiveMindTestCase
{
    protected IRequestCycle newRequestCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected IPage newPage(String name)
    {
        MockControl control = newControl(IPage.class);
        IPage result = (IPage) control.getMock();

        result.getPageName();
        control.setReturnValue(name);

        return result;
    }

    protected IEngine newEngine(boolean stateful)
    {
        MockControl control = newControl(IEngine.class);
        IEngine result = (IEngine) control.getMock();

        result.isStateful();

        control.setReturnValue(stateful);

        return result;
    }

    protected HttpServletRequest newRequest(HttpSession session)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest result = (HttpServletRequest) control.getMock();

        result.getSession();
        control.setReturnValue(session);

        return result;
    }

    protected HttpServletRequest newRequest(boolean create, HttpSession session)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest result = (HttpServletRequest) control.getMock();

        result.getSession(create);
        control.setReturnValue(session);

        return result;
    }

    protected HttpSession newSession(boolean isNew)
    {
        MockControl control = newControl(HttpSession.class);
        HttpSession session = (HttpSession) control.getMock();

        session.isNew();
        control.setReturnValue(isNew);

        return session;
    }

    protected HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    protected ILink newLink()
    {
        return (ILink) newMock(ILink.class);
    }

    protected LinkFactory newLinkFactory(IRequestCycle cycle, Map parameters, boolean stateful,
            ILink link)
    {
        MockControl control = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) control.getMock();

        lf.constructLink(cycle, parameters, stateful);

        control.setReturnValue(link);

        return lf;
    }

    protected ResponseRenderer newResponseRenderer()
    {
        return (ResponseRenderer) newMock(ResponseRenderer.class);
    }

}