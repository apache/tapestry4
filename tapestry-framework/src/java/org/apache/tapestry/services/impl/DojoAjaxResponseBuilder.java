// Copyright May 8, 2006 The Apache Software Foundation
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.markup.NestedMarkupWriterImpl;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.ScriptUtils;
import org.apache.tapestry.web.WebResponse;


/**
 * Main class that handles dojo based ajax responses. These responses are wrapped
 * by an xml document format that segments off invididual component/javascript response
 * types into easy to manage xml elements that can then be interpreted and managed by 
 * running client-side javascript.
 * 
 * @author jkuhnert
 */
public class DojoAjaxResponseBuilder implements ResponseBuilder
{
    // used to create IMarkupWriter
    private RequestLocaleManager _localeManager;
    private MarkupWriterSource _markupWriterSource;
    private WebResponse _webResponse;
    private List _errorPages;
    
    // our response writer
    private IMarkupWriter _writer;
    // Parts that will be updated.
    private List _parts = new ArrayList();
    // Map of specialized writers, like scripts
    private Map _writers = new HashMap();
    
    private IRequestCycle _cycle;
    
    /**
     * Creates a builder with a pre-configured {@link IMarkupWriter}. 
     * Currently only used for testing.
     * 
     * @param writer
     *          The markup writer to render all "good" content to.
     * @param parts
     *          A set of string ids of the components that may have 
     *          their responses rendered.
     */
    public DojoAjaxResponseBuilder(IRequestCycle cycle, IMarkupWriter writer, List parts)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(writer, "writer");
        
        _writer = writer;
        _cycle = cycle;
        
        if (parts != null) 
            _parts.addAll(parts);
    }
    
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
    public DojoAjaxResponseBuilder(IRequestCycle cycle, 
            RequestLocaleManager localeManager, 
            MarkupWriterSource markupWriterSource,
            WebResponse webResponse, List errorPages)
    {
        Defense.notNull(cycle, "cycle");
        
        _cycle = cycle;
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _webResponse = webResponse;
        _errorPages = errorPages;
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
        
        ContentType contentType = new ContentType(CONTENT_TYPE
                + ";charset=" + cycle.getInfrastructure().getOutputEncoding());
        
        String encoding = contentType.getParameter(ENCODING_KEY);
        
        if (encoding == null)
        {
            encoding = cycle.getEngine().getOutputEncoding();
            
            contentType.setParameter(ENCODING_KEY, encoding);
        }
        
        PrintWriter printWriter = _webResponse.getPrintWriter(contentType);
        
        _writer = _markupWriterSource.newMarkupWriter(printWriter, contentType);
        
        parseParameters(cycle);
        
        beginResponse();
        
        // render response
        cycle.renderPage(this);
        
        endResponse();
        
        _writer.close();
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
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter()
    {
        return _writer;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isBodyScriptAllowed(IComponent target)
    {
        if (target != null 
                && IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating())
            return true;
        
        return contains(target);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isExternalScriptAllowed(IComponent target)
    {
        if (target != null 
                && IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating())
            return true;
        
        return contains(target);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isInitializationScriptAllowed(IComponent target)
    {
        if (target != null 
                && IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating())
            return true;
        
        return contains(target);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void beginBodyScript(IMarkupWriter normalWriter, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.BODY_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.begin("script");
        writer.printRaw("\n//<![CDATA[\n");
    }
    
    /** 
     * {@inheritDoc}
     */
    public void endBodyScript(IMarkupWriter normalWriter, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.BODY_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.printRaw("\n//]]>\n");
        writer.end();
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter normalWriter, String script, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.BODY_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.printRaw(script);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeExternalScript(IMarkupWriter normalWriter, String url, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.INCLUDE_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        // causes asset includes to be loaded dynamically into document head
        writer.printRaw("tapestry.loadScriptFromUrl(\"");
        writer.print(url);
        writer.printRaw("\");");
        
        writer.println();
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeImageInitializations(IMarkupWriter normalWriter, String script, String preloadName, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.BODY_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.printRaw("\n\nvar " + preloadName + " = new Array();\n");
        writer.printRaw("if (document.images)\n");
        writer.printRaw("{\n");
        
        writer.printRaw(script);
        
        writer.printRaw("}\n");
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter normalWriter, String script)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.INITIALIZATION_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.begin("script");
        
        // return is in XML so must escape any potentially non-xml compliant content
        writer.printRaw("\n//<![CDATA[\n");
        
        writer.printRaw(script);
        
        writer.printRaw("\n//]]>\n");
        
        writer.end();
    }
    
    /** 
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRender render, IRequestCycle cycle)
    {
        // must be a valid writer already
        if (NestedMarkupWriterImpl.class.isInstance(writer)) {
            render.render(writer, cycle);
            return;
        }
        
        // check for page exception renders and write content to writer so client can display them
        if (IPage.class.isInstance(render)) {
            String errorPage = getErrorPage(((IPage)render).getPageName());
            if (errorPage != null) {
                render.render(getWriter(errorPage, EXCEPTION_TYPE), cycle);
                return;
            }
        }
        
        if (IComponent.class.isInstance(render)
                && contains((IComponent)render))
        {
            render.render(getComponentWriter((IComponent)render), cycle);
            return;
        }
        
        // Nothing else found, throw out response
        render.render(NullWriter.getSharedInstance(), cycle);
    }
    
    private String getErrorPage(String pageName)
    {
        for (int i=0; i < _errorPages.size(); i++) {
            String page = (String)_errorPages.get(i);
            
            if (pageName.indexOf(page) > -1)
                return page;
        }
        
        return null;
    }
    
    /**
     * Gets a {@link NestedMarkupWriter} for the specified
     * component to write to and caches the buffer for later
     * writing.
     * 
     * @param target
     *          The component to get a writer for.
     * @return An IMarkuPWriter instance.
     */
    IMarkupWriter getComponentWriter(IComponent target)
    {
        String id = getComponentId(target);
        
        return getWriter(id, ELEMENT_TYPE);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter(String id, String type)
    {
        Defense.notNull(id, "id can't be null");
        
        IMarkupWriter w = (IMarkupWriter)_writers.get(id);
        if (w != null) 
            return w;
        
        // Make component write to a "nested" writer
        // so that element begin/ends don't conflict
        // with xml element response begin/ends. This is very
        // important.
        
        IMarkupWriter nestedWriter = _writer.getNestedWriter();
        nestedWriter.begin("response");
        nestedWriter.attribute("id", id);
        if (type != null)
            nestedWriter.attribute("type", type);
        
        _writers.put(id, nestedWriter);
        
        return nestedWriter;
    }
    
    /**
     * Called to start an ajax response. Writes xml doctype and starts
     * the <code>ajax-response</code> element that will contain all of
     * the returned content.
     */
    void beginResponse()
    {
        _writer.printRaw("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _writer.printRaw("<!DOCTYPE html "
                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n"
                + "<!ENTITY nbsp '&#160;'>\n"
                + "]>\n");
        _writer.printRaw("<ajax-response>");
    }
    
    /**
     * Called after the entire response has been captured. Causes
     * the writer buffer output captured to be segmented and written
     * out to the right response elements for the client libraries to parse.
     */
    void endResponse()
    {
        // write out captured content
        Iterator keys = _writers.keySet().iterator();
        while (keys.hasNext()) {
            
            String key = (String)keys.next();
            NestedMarkupWriter nw = (NestedMarkupWriter)_writers.get(key);
            
            nw.end();
            
            if (!isScriptWriter(key))
                _writer.printRaw(ScriptUtils.ensureValidScriptTags(nw.getBuffer()));
            else
                _writer.printRaw(nw.getBuffer());
        }
        
        //end response
        _writer.printRaw("</ajax-response>");
        _writer.flush();
    }
    
    /**
     * Determines if the specified markup writer key is one of
     * the pre-defined script keys from ResponseBuilder.
     * 
     * @param key
     *          The key to check.
     * @return True, if key is one of the ResponseBuilder keys. 
     *         (BODY_SCRIPT,INCLUDE_SCRIPT,INITIALIZATION_SCRIPT)
     */
    boolean isScriptWriter(String key)
    {
        if (key == null) 
            return false;
        
        if (ResponseBuilder.BODY_SCRIPT.equals(key)
                || ResponseBuilder.INCLUDE_SCRIPT.equals(key)
                || ResponseBuilder.INITIALIZATION_SCRIPT.equals(key))
            return true;
        
        return false;
    }
    
    /**
     * Grabs the incoming parameters needed for json responses, most notable the
     * {@link ServiceConstants#UPDATE_PARTS} parameter.
     * 
     * @param cycle
     *            The request cycle to parse from
     */
    void parseParameters(IRequestCycle cycle)
    {
        Object[] updateParts = cycle.getParameters(ServiceConstants.UPDATE_PARTS);
        
        if (updateParts == null)
            return;
        
        for(int i = 0; i < updateParts.length; i++)
            _parts.add(updateParts[i].toString());
    }
    
    /**
     * Determines if the specified component is contained in the 
     * responses requested update parts.
     * @param target
     *          The component to check for.
     * @return True if the request should capture the components output.
     */
    boolean contains(IComponent target)
    {
        if (target == null) 
            return false;
        
        String id = getComponentId(target);
        
        if (_parts.contains(id))
            return true;
        
        IComponent parent = (IComponent)_cycle.renderStackPeek();
        
        if (parent != null && parent != target)
            return contains(parent);
        
        return false;
    }
    
    /**
     * Gets the id of the specified component, choosing the "id" element
     * binding over any other id.
     * @param comp
     * @return The id of the component.
     */
    String getComponentId(IComponent comp)
    {
        return comp.getClientId();
    }
}
