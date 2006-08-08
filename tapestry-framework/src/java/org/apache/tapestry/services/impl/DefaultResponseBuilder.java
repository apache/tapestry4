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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;


/**
 * Manages normal html responses for tapestry request/response cycles.
 * 
 * @author jkuhnert
 */
public class DefaultResponseBuilder implements ResponseBuilder
{   
    private RequestLocaleManager _localeManager;
    
    private MarkupWriterSource _markupWriterSource;

    private WebResponse _webResponse;
    
    /** Default writer for rendering html output. */
    private IMarkupWriter _writer;
    
    /**
     * Used in testing only.
     * @param writer
     */
    public DefaultResponseBuilder(IMarkupWriter writer)
    {
        _writer = writer;
    }
    
    /**
     * Creates a new response builder with the required services it needs
     * to render the response when {@link #renderResponse(IRequestCycle)} is called.
     * 
     * @param localeManager 
     *          Used to set the locale on the response.
     * @param markupWriterSource
     *          Creates IMarkupWriter instance to be used.
     * @param webResponse
     *          Web response for output stream.
     */
    public DefaultResponseBuilder(RequestLocaleManager localeManager, 
            MarkupWriterSource markupWriterSource, WebResponse webResponse)
    {
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
        return Boolean.FALSE;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public void renderResponse(IRequestCycle cycle)
    throws IOException
    {
        if (_writer == null) {
            
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
            
            _writer = _markupWriterSource.newMarkupWriter(printWriter, contentType);
        
        }
        
        // render response
        
        cycle.renderPage(this);
        
        _writer.close();
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRender render, IRequestCycle cycle)
    {
        if (writer == null)
            render.render(_writer, cycle);
        else
            render.render(writer, cycle);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void updateComponent(String id)
    {
    }
    
    /** 
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter()
    {
        if (_writer == null)
            return NullWriter.getSharedInstance();
        
        return _writer;
    }
    
    /** 
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter(String id, String type)
    {
        if (_writer == null)
            return NullWriter.getSharedInstance();
        
        return _writer;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isBodyScriptAllowed(IComponent target)
    {
        return true;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isExternalScriptAllowed(IComponent target)
    {
        return true;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isInitializationScriptAllowed(IComponent target)
    {
        return true;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void beginBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--");
    }
    
    /** 
     * {@inheritDoc}
     */
    public void endBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.printRaw("\n\n// -->");
        writer.end();
    }

    /** 
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter writer, String script, IRequestCycle cycle)
    {
        writer.printRaw("\n\n");
        writer.printRaw(script);
    }

    /** 
     * {@inheritDoc}
     */
    public void writeExternalScript(IMarkupWriter writer, String url, IRequestCycle cycle)
    {        
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.attribute("src", url);
        writer.end();
        writer.println();
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeImageInitializations(IMarkupWriter writer, String script, String preloadName, IRequestCycle cycle)
    {
        writer.printRaw("\n\nvar " + preloadName + " = new Array();\n");
        writer.printRaw("if (document.images)\n");
        writer.printRaw("{\n");
        writer.printRaw(script);
        writer.printRaw("}\n");
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter writer, String script)
    {
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--\n");
        
        writer.printRaw("dojo.event.connect(window, 'onload', function(e) {\n");
        
        writer.printRaw(script);
        
        writer.printRaw("});");
        
        writer.printRaw("\n// -->");
        writer.end();
    }
}
