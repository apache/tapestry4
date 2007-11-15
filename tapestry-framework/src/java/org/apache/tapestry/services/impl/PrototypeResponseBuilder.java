package org.apache.tapestry.services.impl;

import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.*;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.markup.NestedMarkupWriterImpl;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.PageRenderSupportImpl;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Implementation of response builder for prototype client side library initiated XHR requests.
 *
 */
public class PrototypeResponseBuilder implements ResponseBuilder {

    public static final String CONTENT_TYPE = "text/html";

    private final AssetFactory _assetFactory;

    private final String _namespace;

    private PageRenderSupportImpl _prs;

    // used to create IMarkupWriter
    private RequestLocaleManager _localeManager;
    private MarkupWriterSource _markupWriterSource;
    private WebResponse _response;

    // our response writer
    private IMarkupWriter _writer;

    // Parts that will be updated.
    private List _parts = new ArrayList();

    // Map of specialized writers, like scripts

    private Map _writers = new HashMap();
    private IRequestCycle _cycle;

    /**
     * Used for unit testing only.
     *
     * @param cycle Request.
     * @param writer Markup writer.
     * @param parts Update parts list.
     */
    public PrototypeResponseBuilder(IRequestCycle cycle, IMarkupWriter writer, List parts)
    {
        _cycle = cycle;
        _writer = writer;

        if (parts != null)
            _parts.addAll(parts);

        _assetFactory = null;
        _namespace = null;
    }

    /**
     * Creates a new response builder with the required services it needs
     * to render the response when {@link #renderResponse(IRequestCycle)} is called.
     *
     * @param cycle
     *          Associated request.
     * @param localeManager
     *          Locale manager to use for response.
     * @param markupWriterSource
     *          Creates necessary {@link IMarkupWriter} instances.
     * @param webResponse
     *          The http response.
     * @param assetFactory
     *          Asset manager for script / other resource inclusion.
     * @param namespace
     *          Javascript namespace value - used in portlets.
     */
    public PrototypeResponseBuilder(IRequestCycle cycle,
                                    RequestLocaleManager localeManager,
                                    MarkupWriterSource markupWriterSource,
                                    WebResponse webResponse,
                                    AssetFactory assetFactory, String namespace)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(assetFactory, "assetService");

        _cycle = cycle;
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _response = webResponse;

        // Used by PageRenderSupport

        _assetFactory = assetFactory;
        _namespace = namespace;
        
        _prs = new PageRenderSupportImpl(_assetFactory, _namespace, this, cycle);
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

        ContentType contentType = new ContentType(CONTENT_TYPE + ";charset=" + cycle.getInfrastructure().getOutputEncoding());

        String encoding = contentType.getParameter(ENCODING_KEY);

        if (encoding == null)
        {
            encoding = cycle.getEngine().getOutputEncoding();

            contentType.setParameter(ENCODING_KEY, encoding);
        }

        if (_writer == null)
        {
            parseParameters(cycle);

            PrintWriter printWriter = _response.getPrintWriter(contentType);

            _writer = _markupWriterSource.newMarkupWriter(printWriter, contentType);
        }

        // render response

        TapestryUtils.storePageRenderSupport(cycle, _prs);

        cycle.renderPage(this);

        TapestryUtils.removePageRenderSupport(cycle);

        endResponse();

        _writer.close();
    }

    public void flush()
      throws IOException
    {
        _writer.flush();
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

    public void addScriptAfterInitialization(IComponent target, String script)
    {
        _prs.addScriptAfterInitialization(target, script);
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
        _writer.begin("script");
        _writer.printRaw("\n//<![CDATA[\n");
    }

    /**
     * {@inheritDoc}
     */
    public void endBodyScript(IMarkupWriter normalWriter, IRequestCycle cycle)
    {
        _writer.printRaw("\n//]]>\n");
        _writer.end();
    }

    /**
     * {@inheritDoc}
     */
    public void writeBodyScript(IMarkupWriter normalWriter, String script, IRequestCycle cycle)
    {
        _writer.printRaw(script);
    }

    /**
     * {@inheritDoc}
     */
    public void writeExternalScript(IMarkupWriter normalWriter, String url, IRequestCycle cycle)
    {
        _writer.begin("script");
        _writer.attribute("type", "text/javascript");
        _writer.attribute("src", url);
        _writer.end();
    }

    /**
     * {@inheritDoc}
     */
    public void writeImageInitializations(IMarkupWriter normalWriter, String script, String preloadName, IRequestCycle cycle)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void writeInitializationScript(IMarkupWriter normalWriter, String script)
    {
        _writer.begin("script");

        // return is in XML so must escape any potentially non-xml compliant content
        _writer.printRaw("\n//<![CDATA[\n");
        _writer.printRaw(script);
        _writer.printRaw("\n//]]>\n");
        _writer.end();
    }

    public void addStatus(IMarkupWriter normalWriter, String text)
    {
        throw new UnsupportedOperationException("Can't return a status response with prototype based requests.");
    }

    public void addStatusMessage(IMarkupWriter normalWriter, String category, String text)
    {
        throw new UnsupportedOperationException("Can't return a status response with prototype based requests.");
    }

    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRender render, IRequestCycle cycle)
    {
        // must be a valid writer already

        if (NestedMarkupWriterImpl.class.isInstance(writer))
        {
            render.render(writer, cycle);
            return;
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

        IMarkupWriter w = (IMarkupWriter)_writers.get(id);
        if (w != null)
            return w;

        IMarkupWriter nestedWriter = _writer.getNestedWriter();
        _writers.put(id, nestedWriter);

        return nestedWriter;
    }

    void beginResponse()
    {
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
        Iterator keys = _writers.keySet().iterator();

        while (keys.hasNext())
        {
            String key = (String)keys.next();
            NestedMarkupWriter nw = (NestedMarkupWriter)_writers.get(key);

            nw.close();
        }

        _writer.flush();
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
        {
            _parts.add(updateParts[i].toString());
        }
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
        while (it.hasNext())
        {
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
