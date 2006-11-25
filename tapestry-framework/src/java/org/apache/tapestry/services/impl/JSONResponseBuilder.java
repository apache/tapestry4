// Copyright Mar 18, 2006 The Apache Software Foundation
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
package org.apache.tapestry.services.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IJSONRender;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

/**
 * Class that implements JSON responses in tapestry.
 * 
 * @see <a href="http://json.org">json.org</a>
 * @author jkuhnert
 */
public class JSONResponseBuilder implements ResponseBuilder
{
    /** Writer that creates JSON output response. */
    protected IJSONWriter _writer;
    /** Passed in to bypass normal rendering. */
    protected IMarkupWriter _nullWriter = NullWriter.getSharedInstance();
    
    /** Parts that will be updated. */
    protected List _parts = new ArrayList();
    
    protected RequestLocaleManager _localeManager;
    
    protected MarkupWriterSource _markupWriterSource;

    protected WebResponse _webResponse;
    
    private IRequestCycle _cycle;
    
    /**
     * Creates a new response builder with the required services it needs
     * to render the response when {@link #renderResponse(IRequestCycle)} is called.
     * 
     * @param localeManager 
     *          Used to set the locale on the response.
     * @param markupWriterSource
     *          Creates IJSONWriter instance to be used.
     * @param webResponse
     *          Web response for output stream.
     */
    public JSONResponseBuilder(IRequestCycle cycle, RequestLocaleManager localeManager, 
            MarkupWriterSource markupWriterSource,
            WebResponse webResponse)
    {
        Defense.notNull(cycle, "cycle");
        
        _cycle = cycle;
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _webResponse = webResponse;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public boolean isDynamic()
    {
        return Boolean.TRUE;
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderResponse(IRequestCycle cycle)
    throws IOException
    {
        _localeManager.persistLocale();
        
        IPage page = cycle.getPage();
        
        ContentType contentType = page.getResponseContentType();
        
        String encoding = contentType.getParameter(ENCODING_KEY);
        
        if (encoding == null)
        {
            encoding = cycle.getEngine().getOutputEncoding();
            
            contentType.setParameter(ENCODING_KEY, encoding);
        }
        
        PrintWriter printWriter = _webResponse.getPrintWriter(contentType);
        
        _writer = _markupWriterSource.newJSONWriter(printWriter, contentType);
        
        // render response
        
        parseParameters(cycle);

        cycle.renderPage(this);

        _writer.close();
    }
    
    /**
     * Grabs the incoming parameters needed for json responses, most notable the
     * {@link ServiceConstants#UPDATE_PARTS} parameter.
     * 
     * @param cycle
     *            The request cycle to parse from
     */
    protected void parseParameters(IRequestCycle cycle)
    {
        Object[] updateParts = cycle.getParameters(ServiceConstants.UPDATE_PARTS);
        
        if (updateParts == null)
            return;
        
        for(int i = 0; i < updateParts.length; i++)
            _parts.add(updateParts[i].toString());
    }
    
    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRender render, IRequestCycle cycle)
    {
        if (IJSONRender.class.isInstance(render)
                && IComponent.class.isInstance(render))
        {
            IJSONRender json = (IJSONRender) render;
            IComponent component = (IComponent) render;
            
            if (!contains(component, component.peekClientId()))
            {
                render.render(_nullWriter, cycle);
                return;
            }
            
            json.renderComponent(_writer, cycle);
        }
        
        render.render(_nullWriter, cycle);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void updateComponent(String id)
    {
        if (!_parts.contains(id))
            _parts.add(id);
    }
    
    /**
     * Determines if the specified component is contained in the 
     * responses requested update parts.
     * @param target
     *          The component to check for.
     * @return True if the request should capture the components output.
     */
    public boolean contains(IComponent target)
    {
        if (target == null) 
            return false;
        
        String id = target.getClientId();
        
        return contains(target, id);
    }
    
    boolean contains(IComponent target, String id)
    {
        if (_parts.contains(id))
            return true;
        
        Iterator it = _cycle.renderStackIterator();
        while (it.hasNext()) {
            
            IComponent comp = (IComponent)it.next();
            String compId = comp.getClientId();
            
            if (comp != target && _parts.contains(compId))
                return true;
        }
        
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean explicitlyContains(IComponent target)
    {
        if (target == null)
            return false;
        
        return _parts.contains(target.getId());
    }
    
    /**
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter()
    {
        return _nullWriter;
    }
    
    /** 
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter(String id, String type)
    {
        return _nullWriter;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isBodyScriptAllowed(IComponent target)
    {
        return false;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isExternalScriptAllowed(IComponent target)
    {
        return false;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isInitializationScriptAllowed(IComponent target)
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isImageInitializationAllowed(IComponent target)
    {
        return false;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void beginBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        // does nothing
    }

    /** 
     * {@inheritDoc}
     */
    public void endBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        // does nothing
    }

    /** 
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter writer, String script, IRequestCycle cycle)
    {
        // does nothing
    }

    /** 
     * {@inheritDoc}
     */
    public void writeExternalScript(IMarkupWriter normalWriter, String url, IRequestCycle cycle)
    {
        // does nothing
    }

    /** 
     * {@inheritDoc}
     */
    public void writeImageInitializations(IMarkupWriter writer, String script, String preloadName, IRequestCycle cycle)
    {
        // does nothing
    }

    /** 
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter writer, String script)
    {
        // does nothing
    }
}
