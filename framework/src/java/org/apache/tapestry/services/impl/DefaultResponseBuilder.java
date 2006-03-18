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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseBuilder;


/**
 * Class that implements JSON responses in tapestry.
 * 
 * @see <a href="http://json.org">json.org</a>
 * @author jkuhnert
 */
public class DefaultResponseBuilder implements ResponseBuilder
{
    /** Writer that creates JSON output response. */
    protected IMarkupWriter _writer;
    
    /**
     * Creates a new markup writer response builder with a valid
     * writer for persisting output to the response stream.
     * 
     * @param writer
     *          The response writer used to render the response.
     */
    public DefaultResponseBuilder(IMarkupWriter writer)
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
        cycle.renderPage(_writer);
        
        _writer.close();
    }
}
