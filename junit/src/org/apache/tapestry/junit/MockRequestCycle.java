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
