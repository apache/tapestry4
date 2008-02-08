// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.html;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseBuilder;

/**
 * The body of a Tapestry page. This is used since it allows components on the page access to an
 * initialization script (that is written the start, just inside the &lt;body&gt; tag). This is
 * currently used by {@link Rollover}and {@link Script}components. [ <a
 * href="../../../../../components/general/body.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class Body extends AbstractComponent
{
    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IMarkupWriter nested = writer.getNestedWriter();

        renderBody(nested, cycle);
        
        getRenderWorker().renderBody(cycle, this);
        
        // This is a little tricky, if we didn't call this manually
        // now the page would never be able to have any javascript related
        // render workers actually work because the javascript output would
        // essentially be non writable
        
        getRenderWorker().renderComponent(cycle, getPage());
        
        // Start the body tag.
        writer.println();
        writer.begin(getElement());
        
        renderInformalParameters(writer, cycle);
        
        renderIdAttribute(writer, cycle);
        
        writer.println();
        
        // Write the page's scripting. This is included scripts
        // and dynamic JavaScript.
        
        getBuilder().writeBodyScript(writer, cycle);
        
        // Close the nested writer, which dumps its buffered content
        // into its parent.
        
        nested.close();
        
        // Any initialization should go at the very end of the document
        // just before the close body tag. Older version of Tapestry
        // would create a window.onload event handler, but this is better
        // (it doesn't have to wait for external images to load).
        
        getBuilder().writeInitializationScript(writer);
        
        writer.end(); // <body>
    }

    /**
     * Parameter.
     */
    public abstract String getElement();
    
    public abstract ResponseBuilder getBuilder();
}
