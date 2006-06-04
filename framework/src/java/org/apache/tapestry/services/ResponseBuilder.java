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
    
    String SCRIPT_TYPE = "script";
    
    String BODY_SCRIPT = "bodyscript";
    
    String INCLUDE_SCRIPT = "includescript";
    
    String INITIALIZATION_SCRIPT = "initializationscript";
    
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
     * If the component identified by the specified id isn't already set to
     * be updated, will add it to the response for updating. (Only applicable
     * in dynamic responses such as XHR/JSON ).
     * 
     * @param id
     *          The {@link IComponent} id to update.
     */
    void updateComponent(String id);
    
    /**
     * Invoked by {@link PageRenderSupport} to write external js package
     * includes. This method will be invoked for each external script requesting
     * inclusion in the response.
     * 
     * These will typically be written out as 
     * <code>
     * <script type="text/javascript" src="url"></script>
     * </code>.
     * 
     * @param url
     *          The absolute url to the .js package to be included.
     * @param cycle
     *          The associated request.
     */    
    void writeExternalScript(String url, IRequestCycle cycle);
    
    /**
     * Marks the beginning of the core body script.
     * 
     * @param cycle
     *          The associated request.
     */
    void beginBodyScript(IRequestCycle cycle);
    
    /**
     * Intended to be written within the confines of the body script, should
     * be invoked once just after {@link #beginBodyScript(IRequestCycle)} is called
     * to include any image initializations. This method should only be called if
     * there are actually images that need pre-initialization. Ie in many instances 
     * it will not be called at all.
     * 
     * @param script
     *          The non null value of the script images to include. 
     * @param preloadName 
     *          The global variable name to give to the preloaded images array.
     * @param cycle
     *          The associated request.
     */
    void writeImageInitializations(String script, String preloadName, IRequestCycle cycle);
    
    /**
     * Called after {@link #beginBodyScript(IRequestCycle)} to write the containing
     * body script. This method may not be called at all if there is no js body 
     * to write into the response.
     * 
     * @param script
     *          The script to write into the body response.
     * @param cycle
     *          The associated request.
     */
    void writeBodyScript(String script, IRequestCycle cycle);
    
    /**
     * Marks the end of the body block being called. This method will 
     * always be called if {@link #beginBodyScript(IRequestCycle)} was previously
     * called. 
     * 
     * @param cycle
     *          The associated request.
     */
    void endBodyScript(IRequestCycle cycle);
    
    /**
     * Writes any javascript that should only execute after all other items
     * on a page have completed rendering. This is typically implemented via
     * wrapping the executing of the code to some sort of <code>window.onload</code> 
     * event, but will vary depending on the implementation of the builder being used.
     * 
     * This method will ~only~ be called if there is any queued intialization script 
     * to write.
     * 
     * @param script
     *          The initialzation script to write.
     */
    void writeInitializationScript(String script);
    
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
     * Gets a write that will output its content in a <code>response</code>
     * element with the given id and type. 
     * 
     * @param id 
     *          The response element id to give writer.
     * @param type
     *          Optional - If specified will give the response element a type
     *          attribute.
     * @return A valid {@link IMarkupWriter} instance to write content to.
     */
    IMarkupWriter getWriter(String id, String type);
    
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
