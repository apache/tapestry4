package org.apache.tapestry.services.impl;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;

/**
 * Implementation of prototype javascript library XHR request sniffer.
 */
public class PrototypeResponseContributorImpl implements ResponseContributor {

    public static final String PROTOTYPE_HEADER = "X-Prototype-Version";

    private RequestLocaleManager _localeManager;
    private MarkupWriterSource _markupWriterSource;
    private WebResponse _webResponse;
    private WebRequest _webRequest;
    private AssetFactory _assetFactory;

    /**
     * {@inheritDoc}
     */
    public ResponseBuilder createBuilder(IRequestCycle cycle)
            throws IOException
    {
        return new PrototypeResponseBuilder(cycle, _localeManager, _markupWriterSource,
                                            _webResponse, _assetFactory, _webResponse.getNamespace());
    }

    /**
     * {@inheritDoc}
     */
    public boolean handlesResponse(IRequestCycle cycle)
    {
        return _webRequest.getHeader(PROTOTYPE_HEADER) != null;
    }

    public void setLocaleManager(RequestLocaleManager localeManager)
    {
        _localeManager = localeManager;
    }

    public void setMarkupWriterSource(MarkupWriterSource markupWriterSource)
    {
        _markupWriterSource = markupWriterSource;
    }

    public void setWebResponse(WebResponse webResponse)
    {
        _webResponse = webResponse;
    }

    public void setWebRequest(WebRequest webRequest)
    {
        _webRequest  = webRequest;
    }

    public void setAssetFactory(AssetFactory factory)
    {
        _assetFactory = factory;
    }
}
