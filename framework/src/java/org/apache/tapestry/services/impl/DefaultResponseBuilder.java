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
 * Class that implements JSON responses in tapestry.
 * 
 * @see <a href="http://json.org">json.org</a>
 * @author jkuhnert
 */
public class DefaultResponseBuilder implements ResponseBuilder
{   
    /**
     * Inside a {@link org.apache.tapestry.util.ContentType}, the output encoding is called
     * "charset".
     */
    
    public static final String ENCODING_KEY = "charset";
    
    protected RequestLocaleManager _localeManager;
    
    protected MarkupWriterSource _markupWriterSource;

    protected WebResponse _webResponse;
    
    /** Writer that creates JSON output response. */
    protected IMarkupWriter _writer;
    
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
            MarkupWriterSource markupWriterSource,
            WebResponse webResponse)
    {
        _localeManager = localeManager;
        _markupWriterSource = markupWriterSource;
        _webResponse = webResponse;
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
    public IMarkupWriter getWriter()
    {
        if (_writer == null)
            return NullWriter.getSharedInstance();
        
        return _writer;
    }
}
