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

package org.apache.tapestry.junit;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.request.RequestContext;

/**
 *  Used to simulate an {@link org.apache.tapestry.IRequestCycle} in some tests.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/
public class MockRequestCycle implements IRequestCycle
{
    private IEngine _engine;
    private String _lastEncodedURL;
    private RequestContext _context;
    private Map _attributes = new HashMap();

    public MockRequestCycle()
    {
        this(new MockEngine(), null);
    }

    public MockRequestCycle(IEngine engine, RequestContext context)
    {
        _engine = engine;
        _context = context;
    }

    public void cleanup()
    {
    }

    public String encodeURL(String URL)
    {
        _lastEncodedURL = URL;

        return URL;
    }

    public IEngine getEngine()
    {
        return _engine;
    }

    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }

    public IMonitor getMonitor()
    {
        return null;
    }

    public String getNextActionId()
    {
        return null;
    }

    public IPage getPage()
    {
        return null;
    }

    public IPage getPage(String name)
    {
        return null;
    }

    public RequestContext getRequestContext()
    {
        return _context;
    }

    public boolean isRewinding()
    {
        return false;
    }

    public boolean isRewound(IComponent component) throws StaleLinkException
    {
        return false;
    }

    public void removeAttribute(String name)
    {
        _attributes.remove(name);
    }

    public void renderPage(IMarkupWriter writer)
    {
    }

    public void rewindPage(String targetActionId, IComponent targetComponent)
    {
    }

    public void setAttribute(String name, Object value)
    {
        _attributes.put(name, value);
    }

    public void setPage(IPage page)
    {
    }

    public void setPage(String name)
    {
    }

    public void commitPageChanges()
    {
    }

    public IEngineService getService()
    {
        return null;
    }

    public void rewindForm(IForm form, String targetActionId)
    {
    }

    public void discardPage(String name)
    {
    }

    public void setServiceParameters(Object[] parameters)
    {
    }

    public Object[] getServiceParameters()
    {
        return null;
    }

    public String getLastEncodedURL()
    {
        return _lastEncodedURL;
    }

    public void activate(String name)
    {
    }

    public void activate(IPage page)
    {
    }

}
