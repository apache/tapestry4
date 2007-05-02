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

import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.PageRenderSupportImpl;
import org.apache.tapestry.web.WebResponse;


/**
 * Manages normal html responses for tapestry request/response cycles.
 * 
 * @author jkuhnert
 */
public class DefaultResponseBuilder implements ResponseBuilder
{   
    private final AssetFactory _assetFactory;
    
    private final String _namespace;
    
    private PageRenderSupportImpl _prs;
    
    private RequestLocaleManager _localeManager;
    
    private MarkupWriterSource _markupWriterSource;

    private WebResponse _webResponse;
    
    /** Default writer for rendering html output. */
    private IMarkupWriter _writer;
    
    private boolean _closeWriter = true;
    
    /**
     * Portlet constructor.
     * 
     * @param writer
     */
    public DefaultResponseBuilder(IMarkupWriter writer, 
            AssetFactory assetFactory, String namespace, boolean closeWriter)
    {
        _writer = writer;
        _assetFactory = assetFactory;
        _namespace = namespace;
        _closeWriter = closeWriter;
    }
    
    /**
     * Used in testing only.
     * @param writer
     */
    public DefaultResponseBuilder(IMarkupWriter writer)
    {
        _writer = writer;
        _assetFactory = null;
        _namespace = null;
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
            MarkupWriterSource markupWriterSource, WebResponse webResponse,
            AssetFactory assetFactory, String namespace)
    {
        Defense.notNull(assetFactory, "assetService");
        
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _webResponse = webResponse;
        
        // Used by PageRenderSupport
        
        _assetFactory = assetFactory;
        _namespace = namespace;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public boolean isDynamic()
    {
        return false;
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
        
        _prs = new PageRenderSupportImpl(_assetFactory, _namespace, cycle.getPage().getLocation(), this);
        
        TapestryUtils.storePageRenderSupport(cycle, _prs);
        
        cycle.renderPage(this);
        
        TapestryUtils.removePageRenderSupport(cycle);
        
        if (_closeWriter)
            _writer.close();
    }
    
    public void flush()
    throws IOException
    {
        // Important - causes any cookies stored to properly be written out before the
        // rest of the response starts being written - see TAPESTRY-825

        _writer.flush();
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
    public boolean contains(IComponent target)
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean explicitlyContains(IComponent target)
    {
        return false;
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
    public boolean isImageInitializationAllowed(IComponent target)
    {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getPreloadedImageReference(IComponent target, IAsset source)
    {
        return _prs.getPreloadedImageReference(target, source);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getPreloadedImageReference(IComponent target, String url)
    {
        return _prs.getPreloadedImageReference(target, url);
    }

    /**
     * {@inheritDoc}
     */
    public String getPreloadedImageReference(String url)
    {
        return _prs.getPreloadedImageReference(url);
    }

    /**
     * {@inheritDoc}
     */
    public void addBodyScript(IComponent target, String script)
    {
        _prs.addBodyScript(target, script);
    }

    /**
     * {@inheritDoc}
     */
    public void addBodyScript(String script)
    {
        _prs.addBodyScript(script);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addExternalScript(IComponent target, Resource resource)
    {
        _prs.addExternalScript(target, resource);
    }

    /**
     * {@inheritDoc}
     */
    public void addExternalScript(Resource resource)
    {
        _prs.addExternalScript(resource);
    }

    /**
     * {@inheritDoc}
     */
    public void addInitializationScript(IComponent target, String script)
    {
        _prs.addInitializationScript(target, script);
    }

    /**
     * {@inheritDoc}
     */
    public void addInitializationScript(String script)
    {
        _prs.addInitializationScript(script);
    }

    /**
     * {@inheritDoc}
     */
    public String getUniqueString(String baseValue)
    {
        return _prs.getUniqueString(baseValue);
    }
    
    /**
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        _prs.writeBodyScript(writer, cycle);
    }
    
    /**
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter writer)
    {
        _prs.writeInitializationScript(writer);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void beginBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--");
        writer.println();
    }
    
    /** 
     * {@inheritDoc}
     */
    public void endBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.println();
        writer.printRaw("// -->");
        writer.end();
    }

    /** 
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter writer, String script, IRequestCycle cycle)
    {
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
        
        writer.println();
        writer.printRaw("dojo.addOnLoad(function(e) {\n");
        
        writer.printRaw(preloadName + " = [];\n");
        writer.printRaw("if (document.images)\n");
        writer.printRaw("{\n");
        writer.printRaw(script);
        writer.printRaw("}\n");
        
        writer.printRaw("});");
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter writer, String script)
    {
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--\n");
        
        writer.printRaw("dojo.addOnLoad(function(e) {\n");
        
        writer.printRaw(script);
        
        writer.printRaw("});");
        
        writer.printRaw("\n// -->");
        writer.end();
    }

    /**
     * This implementation does nothing.
     * {@inheritDoc}
     */
    public void addStatusMessage(IMarkupWriter normalWriter, String category, String text)
    {
    }
}
