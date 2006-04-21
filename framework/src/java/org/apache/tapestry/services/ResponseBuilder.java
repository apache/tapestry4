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
package org.apache.tapestry.services;

import java.io.IOException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;


/**
 * Represents the class responsible for interacting
 * with components for an ajax request library version and type.
 *
 * @author jkuhnert
 * @since 4.1
 */
public interface ResponseBuilder {

    /**
     * Renders the response to a client. Handles transitioning logic
     * for setting up page and associated components for response.
     * 
     * @param cycle
     *          The main request cycle object for this request.
     */
    
    void renderResponse(IRequestCycle cycle)
    throws IOException;
    
    /**
     * Invoked to render a renderable object. Performs any necessary
     * under the hood type logic involving ajax/json/normal responses, where
     * needed.
     * 
     * @param writer 
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param render The renderable object to render
     * @param cycle Render request cycle
     */
    
    void render(IMarkupWriter writer, IRender render, IRequestCycle cycle);
    
    /**
     * Returns the IMarkupWriter associated with this response, it may or may
     * not be a NullWriter instance depending on the response type or stage 
     * of the render cycle. (specifically during rewind)
     * 
     * @return A validly writable markup writer, even if the content is sometimes
     * ignored.
     */
    
    IMarkupWriter getWriter();
    
    /**
     * Determines if the specified component should have its javascript 
     * body added to the response.
     * 
     * @param target
     *          The component to allow/disallow body script content from.
     * @return True if the component script should be allowed.
     */
    boolean isBodyScriptAllowed(IComponent target);
    
    /**
     * Determines if the specified component should have its javascript 
     * initialization added to the response.
     * 
     * @param target
     *          The component to allow/disallow initialization script content from.
     * @return True if the component script should be allowed.
     */
    boolean isInitializationScriptAllowed(IComponent target);
    
    /**
     * Determines if the specified component should have its javascript 
     * external resource scripts added to the response.
     * 
     * @param target
     *          The component to check for inclusion/exclusion.
     * @return True if external scripts from this component should be added to
     *          the response.
     */
    boolean isExternalScriptAllowed(IComponent target);
}
