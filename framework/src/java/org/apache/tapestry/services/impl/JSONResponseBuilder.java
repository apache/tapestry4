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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.util.Defense;
import org.apache.log4j.Logger;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IJSONRender;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;


/**
 * Class that implements JSON responses in tapestry.
 * 
 * @see <a href="http://json.org">json.org</a>
 * @author jkuhnert
 */
public class JSONResponseBuilder implements ResponseBuilder
{
    /* logger */
    protected static final Logger _log = Logger.getLogger(JSONResponseBuilder.class);
    
    /** Writer that creates JSON output response. */
    protected IJSONWriter _writer;
    /** Passed in to bypass normal rendering. */
    protected IMarkupWriter _nullWriter = NullWriter.getSharedInstance();
    
    /** Parts that will be updated. */
    protected List parts = new ArrayList();
    
    /**
     * Creates a new JSON response builder with a valid
     * writer for persisting output to the response stream.
     * 
     * @param writer
     *          The response writer used to render the response.
     */
    public JSONResponseBuilder(IJSONWriter writer)
    {
        Defense.notNull(writer, "writer");
        
        _writer = writer;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public void renderResponse(IRequestCycle cycle)
    {
        _log.warn("renderResponse()");
        parseParameters(cycle);
        
        cycle.renderPage(this);
        
        _writer.close();
    }
    
    /**
     * Grabs the incoming parameters needed for json responses,
     * most notable the {@link ServiceConstants#UPDATE_PARTS} parameter.
     * @param cycle The request cycle to parse from
     */
    protected void parseParameters(IRequestCycle cycle)
    {
        Object[] updateParts = cycle.getParameters(ServiceConstants.UPDATE_PARTS);
        for (int i = 0; i < updateParts.length; i++)
            parts.add(updateParts[i].toString());
    }
    
    /** 
     * {@inheritDoc}
     */
    public void render(IRender render, IRequestCycle cycle)
    {
        if (IJSONRender.class.isInstance(render) && IComponent.class.isInstance(render)) {
            IJSONRender json = (IJSONRender)render;
            IComponent component = (IComponent)render;
            
            if (!parts.contains(component.getId())) {
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
    public IMarkupWriter getWriter()
    {
        return _nullWriter;
    }
}
