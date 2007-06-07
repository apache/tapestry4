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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.*;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.markup.NestedMarkupWriterImpl;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.PageRenderSupportImpl;
import org.apache.tapestry.util.ScriptUtils;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 * Main class that handles dojo based ajax responses. These responses are wrapped
 * by an xml document format that segments off invididual component/javascript response
 * types into easy to manage xml elements that can then be interpreted and managed by 
 * running client-side javascript.
 * 
 */
public class DojoAjaxResponseBuilder implements ResponseBuilder
{
    private static final Log _log = LogFactory.getLog(DojoAjaxResponseBuilder.class);
    
    private final AssetFactory _assetFactory;
    
    private final String _namespace;
    
    private PageRenderSupportImpl _prs;
    
    // used to create IMarkupWriter
    private RequestLocaleManager _localeManager;
    private MarkupWriterSource _markupWriterSource;
    private WebResponse _response;
    
    private List _errorPages;
    
    private ContentType _contentType;
    
    // our response writer
    private IMarkupWriter _writer;
    // Parts that will be updated.
    private List _parts = new ArrayList();
    // Map of specialized writers, like scripts
    private Map _writers = new HashMap();
    // List of status messages.
    private List _statusMessages;
    
    private IRequestCycle _cycle;
    
    private IEngineService _pageService;
    
    /**
     * Keeps track of renders involving a whole page response, such 
     * as exception pages or pages activated via {@link IRequestCycle#activate(IPage)}.
     */
    private boolean _pageRender = false;
    
    /**
     * Used to keep track of whether or not the appropriate xml response start
     * block has been started.
     */
    private boolean _responseStarted = false;

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
    public DojoAjaxResponseBuilder(IRequestCycle cycle, IMarkupWriter writer, List parts, List errorPages)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(writer, "writer");

        _writer = writer;
        _cycle = cycle;

        if (parts != null)
            _parts.addAll(parts);

        _namespace = null;
        _assetFactory = null;
        _errorPages = errorPages;
    }

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
        this(cycle, writer, parts, null);
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
            WebResponse webResponse, WebRequest request, List errorPages, 
            AssetFactory assetFactory, String namespace, IEngineService service)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(assetFactory, "assetService");
        
        _cycle = cycle;
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _response = webResponse;
        _errorPages = errorPages;
        _pageService = service;
        
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
        return true;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void renderResponse(IRequestCycle cycle)
        throws IOException
    {
        _localeManager.persistLocale();
        
        _contentType = new ContentType(CONTENT_TYPE + ";charset=" + cycle.getInfrastructure().getOutputEncoding());
        
        String encoding = _contentType.getParameter(ENCODING_KEY);
        
        if (encoding == null)
        {
            encoding = cycle.getEngine().getOutputEncoding();
            
            _contentType.setParameter(ENCODING_KEY, encoding);
        }
        
        if (_writer == null) {
            
            parseParameters(cycle);
            
            PrintWriter printWriter = _response.getPrintWriter(_contentType);
            
            _writer = _markupWriterSource.newMarkupWriter(printWriter, _contentType);
        }
        
        // render response
        
        _prs = new PageRenderSupportImpl(_assetFactory, _namespace, cycle.getPage().getLocation(), this);
        
        TapestryUtils.storePageRenderSupport(cycle, _prs);
        
        cycle.renderPage(this);
        
        TapestryUtils.removePageRenderSupport(cycle);
        
        endResponse();
        
        _writer.close();
    }
    
    public void flush()
    throws IOException
    {
        // Important - causes any cookies stored to properly be written out before the
        // rest of the response starts being written - see TAPESTRY-825
        
        _writer.flush();
        
        if (!_responseStarted)
            beginResponse();
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
    
    void setWriter(IMarkupWriter writer)
    {
        _writer = writer;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isBodyScriptAllowed(IComponent target)
    {
        if (_pageRender)
            return true;
        
        if (target != null 
                && IPage.class.isInstance(target)
                || (IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating()))
            return true;
        
        return contains(target);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isExternalScriptAllowed(IComponent target)
    {
        if (_pageRender)
            return true;
        
        if (target != null 
                && IPage.class.isInstance(target)
                || (IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating()))
            return true;
        
        return contains(target);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isInitializationScriptAllowed(IComponent target)
    {
        if (_log.isDebugEnabled()) {
            
            _log.debug("isInitializationScriptAllowed(" + target + ") contains?: " + contains(target) + " _pageRender: " + _pageRender);
        }
        
        if (_pageRender)
            return true;
        
        if (target != null 
                && IPage.class.isInstance(target)
                || (IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating()))
            return true;
        
        return contains(target);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isImageInitializationAllowed(IComponent target)
    {
        if (_pageRender)
            return true;
        
        if (target != null 
                && IPage.class.isInstance(target)
                || (IForm.class.isInstance(target)
                && ((IForm)target).isFormFieldUpdating()))
            return true;
        
        return contains(target);
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
        writer.beginEmpty("include");
        writer.attribute("url", url);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void writeImageInitializations(IMarkupWriter normalWriter, String script, String preloadName, IRequestCycle cycle)
    {
        IMarkupWriter writer = getWriter(ResponseBuilder.BODY_SCRIPT, ResponseBuilder.SCRIPT_TYPE);
        
        writer.printRaw("\n" + preloadName + " = [];\n");
        writer.printRaw("if (document.images) {\n");
        
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
        
    public void addStatus(IMarkupWriter normalWriter, String text)
    {
        addStatusMessage(normalWriter, "info", text);
    }  
    
    /**
     * Adds a status message to the current response. This implementation keeps track
     * of all messages and appends them to the XHR response. On the client side, 
     * the default behavior is to publish the message to a topic matching the category name
     * using <code>dojo.event.topic.publish(category,text);</code>.
     *
     * @param normalWriter
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param category
     *          Allows setting a category that best describes the type of the status message,
     *          i.e. info, error, e.t.c.
     * @param text
     *          The status message. 
     */
    public void addStatusMessage(IMarkupWriter normalWriter, String category, String text)
    {
        if (_statusMessages==null)
        {
            _statusMessages = new ArrayList();
        }
        
        _statusMessages.add(category);
        _statusMessages.add(text);        
    }
    
    void writeStatusMessages() {

        for (int i=0; i < _statusMessages.size(); i+=2)
        {
            IMarkupWriter writer = getWriter((String) _statusMessages.get(i), "status");

            writer.printRaw((String) _statusMessages.get(i+1));                
        }
        
        _statusMessages = null;            
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
            
            IPage page = (IPage)render;
            String errorPage = getErrorPage(page.getPageName());
            
            if (errorPage != null) {
                
                _pageRender = true;
                clearPartialWriters();
                render.render(getWriter(errorPage, EXCEPTION_TYPE), cycle);
                return;
            }
            
            // If a page other than the active page originally requested is rendered
            // it means someone activated a new page, so we need to tell the client to handle
            // this appropriately. (usually by replacing the current dom with whatever this renders)
            
            if (_cycle.getParameter(ServiceConstants.PAGE) != null
                    && !page.getPageName().equals(_cycle.getParameter(ServiceConstants.PAGE))) {
                
                IMarkupWriter urlwriter = _writer.getNestedWriter();
                
                urlwriter.begin("response");
                urlwriter.attribute("type", PAGE_TYPE);
                urlwriter.attribute("url", _pageService.getLink(true, page.getPageName()).getAbsoluteURL());
                
                _writers.put(PAGE_TYPE, urlwriter);
                return;
            }
        }
        
        if (IComponent.class.isInstance(render)
                && contains((IComponent)render, ((IComponent)render).peekClientId()))
        {
            render.render(getComponentWriter( ((IComponent)render).peekClientId() ), cycle);
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
    
    IMarkupWriter getComponentWriter(String id)
    {
        return getWriter(id, ELEMENT_TYPE);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public IMarkupWriter getWriter(String id, String type)
    {
        Defense.notNull(id, "id can't be null");
        
        if (!_responseStarted)
            beginResponse();
        
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
        _responseStarted = true;
        
        _writer.printRaw("<?xml version=\"1.0\" encoding=\"" + _cycle.getInfrastructure().getOutputEncoding() + "\"?>");
        _writer.printRaw("<!DOCTYPE html "
                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n"
                + "<!ENTITY nbsp '&#160;'>\n"
                + "]>\n");
        _writer.printRaw("<ajax-response>");
    }

    /**
     * Invoked to clear out tempoary partial writer buffers before rendering exception
     * page.
     */
    void clearPartialWriters()
    {
        _writers.clear();
    }

    /**
     * Called after the entire response has been captured. Causes
     * the writer buffer output captured to be segmented and written
     * out to the right response elements for the client libraries to parse.
     */
    void endResponse()
    {
        if (!_responseStarted)
        {
            beginResponse();
        }
        
        // write out captured content
        
        if (_statusMessages != null)        
            writeStatusMessages();
        
        Iterator keys = _writers.keySet().iterator();
        String buffer = null;
        
        while (keys.hasNext()) {
            
            String key = (String)keys.next();
            NestedMarkupWriter nw = (NestedMarkupWriter)_writers.get(key);
                        
            buffer = nw.getBuffer();
            
            if (_log.isDebugEnabled()) {
                
                _log.debug("Ajax markup buffer for key <" + key + " contains: " + buffer);
            }
            
            if (!isScriptWriter(key))
                _writer.printRaw(ScriptUtils.ensureValidScriptTags(buffer));
            else
                _writer.printRaw(buffer);
        }
        
        // end response
        
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
}
