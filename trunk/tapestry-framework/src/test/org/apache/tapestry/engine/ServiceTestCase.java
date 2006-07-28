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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * Common utilities for building tests for {@link org.apache.tapestry.engine.IEngineService}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class ServiceTestCase extends BaseComponentTestCase
{

    protected IPage newPage(String name)
    {
        IPage result = newMock(IPage.class);
        checkOrder(result, false);
        
        expect(result.getPageName()).andReturn(name);
        return result;
    }

    protected HttpServletRequest newRequest(HttpSession session)
    {
        HttpServletRequest result = newMock(HttpServletRequest.class);

        expect(result.getSession()).andReturn(session);

        return result;
    }

    protected WebRequest newWebRequest(WebSession session)
    {
        WebRequest result = newMock(WebRequest.class);

        expect(result.getSession(false)).andReturn(session);

        return result;
    }

    protected HttpServletRequest newRequest(boolean create, HttpSession session)
    {
        HttpServletRequest result = newMock(HttpServletRequest.class);

        expect(result.getSession(create)).andReturn(session);

        return result;
    }

    protected WebRequest newWebRequest(boolean create, WebSession session)
    {
        WebRequest result = newMock(WebRequest.class);

        expect(result.getSession(create)).andReturn(session);

        return result;
    }

    protected HttpSession newSession(boolean isNew)
    {
        HttpSession session = newSession();

        expect(session.isNew()).andReturn(isNew);

        return session;
    }

    protected WebSession newWebSession(boolean isNew)
    {
        WebSession session = newWebSession();
        checkOrder(session, false);
        
        expect(session.isNew()).andReturn(isNew);

        return session;
    }

    protected HttpSession newSession()
    {
        return newMock(HttpSession.class);
    }

    protected WebSession newWebSession()
    {
        return newMock(WebSession.class);
    }

    protected ILink newLink()
    {
        return newMock(ILink.class);
    }

    protected LinkFactory newLinkFactory(Map parameters, boolean stateful, ILink link)
    {
        LinkFactory lf = newMock(LinkFactory.class);

        expect(lf.constructLink(null, false, parameters, stateful)).andReturn(link);

        return lf;
    }

    protected ResponseRenderer newResponseRenderer()
    {
        return newMock(ResponseRenderer.class);
    }

    protected void trainConstructLink(LinkFactory linkFactory, IEngineService service,
            boolean post, Map parameters, boolean stateful, ILink link)
    {
        expect(linkFactory.constructLink(service, post, parameters, stateful)).andReturn(link);
    }

    protected LinkFactory newLinkFactory()
    {
        return newMock(LinkFactory.class);
    }

    protected void trainGetPage(IRequestCycle cycle, IPage page)
    {
        expect(cycle.getPage()).andReturn(page);
    }

    protected void trainGetNestedComponent(IPage page, String idPath, IComponent component)
    {
        expect(page.getNestedComponent(idPath)).andReturn(component);
    }

    protected void trainGetPage(IRequestCycle cycle, String pageName, IPage page)
    {
        expect(cycle.getPage(pageName)).andReturn(page);
    }

    protected void trainExtractListenerParameters(LinkFactory factory, IRequestCycle cycle, Object[] parameters)
    {
        expect(factory.extractListenerParameters(cycle)).andReturn(parameters);
    }

    protected void trainGetAbsoluteURL(IRequestCycle cycle, String shortURL, String fullURL)
    {
        expect(cycle.getAbsoluteURL(shortURL)).andReturn(fullURL);
    }
    
    protected Log newLog()
    {
        return newMock(Log.class);
    }

}