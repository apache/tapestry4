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
        IPage result = (IPage) newMock(IPage.class);

        result.getPageName();
        setReturnValue(result, name);

        return result;
    }

    protected HttpServletRequest newRequest(HttpSession session)
    {
        HttpServletRequest result = (HttpServletRequest) newMock(HttpServletRequest.class);

        result.getSession();
        setReturnValue(result, session);

        return result;
    }

    protected WebRequest newWebRequest(WebSession session)
    {
        WebRequest result = (WebRequest) newMock(WebRequest.class);

        result.getSession(false);
        setReturnValue(result, session);

        return result;
    }

    protected HttpServletRequest newRequest(boolean create, HttpSession session)
    {
        HttpServletRequest result = (HttpServletRequest) newMock(HttpServletRequest.class);

        result.getSession(create);
        setReturnValue(result, session);

        return result;
    }

    protected WebRequest newWebRequest(boolean create, WebSession session)
    {
        WebRequest result = (WebRequest) newMock(WebRequest.class);

        result.getSession(create);
        setReturnValue(result, session);

        return result;
    }

    protected HttpSession newSession(boolean isNew)
    {
        HttpSession session = newSession();

        session.isNew();
        setReturnValue(session, isNew);

        return session;
    }

    protected WebSession newWebSession(boolean isNew)
    {
        WebSession session = newWebSession();

        session.isNew();
        setReturnValue(session, isNew);

        return session;
    }

    protected HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    protected WebSession newWebSession()
    {
        return (WebSession) newMock(WebSession.class);
    }

    protected ILink newLink()
    {
        return (ILink) newMock(ILink.class);
    }

    protected LinkFactory newLinkFactory(Map parameters, boolean stateful, ILink link)
    {
        LinkFactory lf = (LinkFactory) newMock(LinkFactory.class);

        lf.constructLink(null, false, parameters, stateful);

        setReturnValue(lf, link);

        return lf;
    }

    protected ResponseRenderer newResponseRenderer()
    {
        return (ResponseRenderer) newMock(ResponseRenderer.class);
    }

    protected void trainConstructLink(LinkFactory linkFactory, IEngineService service,
            boolean post, Map parameters, boolean stateful, ILink link)
    {
        linkFactory.constructLink(service, post, parameters, stateful);
        setReturnValue(linkFactory, link);
    }

    protected LinkFactory newLinkFactory()
    {
        return (LinkFactory) newMock(LinkFactory.class);
    }

    protected void trainGetPage(IRequestCycle cycle, IPage page)
    {
        cycle.getPage();
        setReturnValue(cycle, page);
    }

    protected void trainGetNestedComponent(IPage page, String idPath, IComponent component)
    {
        page.getNestedComponent(idPath);
        setReturnValue(page, component);
    }

    protected void trainGetPage(IRequestCycle cycle, String pageName, IPage page)
    {
        cycle.getPage(pageName);
        setReturnValue(cycle, page);
    }

    protected void trainExtractListenerParameters(LinkFactory factory, IRequestCycle cycle, Object[] parameters)
    {
        factory.extractListenerParameters(cycle);
        setReturnValue(factory, parameters);
    }

    protected void trainGetAbsoluteURL(IRequestCycle cycle, String shortURL, String fullURL)
    {
        cycle.getAbsoluteURL(shortURL);
        setReturnValue(cycle, fullURL);
    }

    protected Log newLog()
    {
        return (Log) newMock(Log.class);
    }

}