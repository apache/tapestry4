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
 * Represents the service responsible for managing all content output that is sent
 * to the client. In the case of normal http responses this management would inlude 
 * handing out {@link IMarkupWriter} instances to render components with, as well as 
 * managing any javascript written to the output using Script templates.
 *
 * <p>
 *  This is a major internal change in terms of the way tapestry renders pages/components.
 *  Traditionally a response has been rendered via:
 *  <em>
 *  IPage.render(writer, cycle);
 *  </em>
 *  The logic has now changed somewhat, while the IPage.render(writer, cycle) does still happen, this
 *  service is the primary invoker of all renders, even nested component bodies. That means that in the majority
 *  of cases the ResponseBuilder service is used to invoke IComponent.render() throught the entire render
 *  cycle, creating a great deal of flexibility in terms of what can be done to control the output of a given
 *  response.
 * </p>
 *
 * <p>
 * This service was primarily created to help bolster support for more dynamic content responses, such 
 * as XHR/JSON/etc - where controlling individual component output (and javascript) becomes very important
 * when managaing client side browser state. 
 * </p>
 *
 * @author jkuhnert
 * @since 4.1
 */
public interface ResponseBuilder {
    
    /**
     * Inside a {@link org.apache.tapestry.util.ContentType}, the output encoding is called
     * "charset".
     */
    String ENCODING_KEY = "charset";

    /**
     * The content type of the response that will be returned.
     */
    String CONTENT_TYPE = "text/xml";

    /**
     * The response element type.
     */
    String ELEMENT_TYPE = "element";

    /**
     * The response exception type.
     */
    String EXCEPTION_TYPE = "exception";

    String SCRIPT_TYPE = "script";
    
    String BODY_SCRIPT = "bodyscript";
    
    String INCLUDE_SCRIPT = "includescript";
    
    String INITIALIZATION_SCRIPT = "initializationscript";
    
    /**
     * Implementors that manage content writes dynamically (ie {@link DojoAjaxResponseBuilder}) should
     * return true to denote that dynamic behaviour is on for a particular response.
     * @return
     */
    boolean isDynamic();
    
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
     * Checks if the rendered response contains a particular component. Contains
     * can mean many things. In the instance of a dynamic response it could potentially
     * mean a component explicitly set to be updated - or a component that has a containing
     * component explicitly set to be updated.
     * 
     * @param target The component to check containment of.
     * @return True if response contains the specified component, false otherwise.
     */
    boolean contains(IComponent target);
    
    /**
     * Similar to {@link #contains(IComponent)}, but only returns true if the component
     * has been marked for update directly via an <code>updateComponents</code> property 
     * or by calling {@link ResponseBuilder#updateComponent(String)} directly. 
     * 
     * <p>
     * <b>IMPORTANT!:</b> This will not return true for components contained by a component
     *  marked for update. If you want that kind of behaviour use {@link #contains(IComponent)}. 
     * </p>
     * 
     * @param target The component to check.
     * @return True if the component as listed as one to be updated, false otherwise.
     */
    boolean explicitlyContains(IComponent target);
    
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
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param url
     *          The absolute url to the .js package to be included.
     * @param cycle
     *          The associated request.
     */    
    void writeExternalScript(IMarkupWriter writer, String url, IRequestCycle cycle);
    
    /**
     * Marks the beginning of the core body script.
     *
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param cycle
     *          The associated request.
     */
    void beginBodyScript(IMarkupWriter writer, IRequestCycle cycle);
    
    /**
     * Intended to be written within the confines of the body script, should
     * be invoked once just after {@link #beginBodyScript(IMarkupWriter, IRequestCycle)} is called
     * to include any image initializations. This method should only be called if
     * there are actually images that need pre-initialization. Ie in many instances 
     * it will not be called at all.
     *
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param script
     *          The non null value of the script images to include. 
     * @param preloadName 
     *          The global variable name to give to the preloaded images array.
     * @param cycle
     *          The associated request.
     */
    void writeImageInitializations(IMarkupWriter writer, String script, String preloadName, IRequestCycle cycle);
    
    /**
     * Called after {@link #beginBodyScript(IMarkupWriter, IRequestCycle)} to write the containing
     * body script. This method may not be called at all if there is no js body 
     * to write into the response.
     *
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param script
     *          The script to write into the body response.
     * @param cycle
     *          The associated request.
     */
    void writeBodyScript(IMarkupWriter writer, String script, IRequestCycle cycle);
    
    /**
     * Marks the end of the body block being called. This method will 
     * always be called if {@link #beginBodyScript(IMarkupWriter, IRequestCycle)} was previously
     * called. 
     *
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param cycle
     *          The associated request.
     */
    void endBodyScript(IMarkupWriter writer, IRequestCycle cycle);
    
    /**
     * Writes any javascript that should only execute after all other items
     * on a page have completed rendering. This is typically implemented via
     * wrapping the executing of the code to some sort of <code>window.onload</code> 
     * event, but will vary depending on the implementation of the builder being used.
     * 
     * This method will ~only~ be called if there is any queued intialization script 
     * to write.
     *
     * @param writer
     *          The markup writer to use, this may be ignored or swapped
     *          out for a different writer depending on the implementation being used.
     * @param script
     *          The initialzation script to write.
     */
    void writeInitializationScript(IMarkupWriter writer, String script);
    
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
     * Determines if the specified component should have any asset image URL
     * references embedded in the response.
     * 
     * @param target
     *          The component to allow/disallow image initialization script content from.
     * @return True if the component script should be allowed.
     */
    boolean isImageInitializationAllowed(IComponent target);
    
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
