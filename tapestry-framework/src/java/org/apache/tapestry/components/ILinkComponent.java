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

package org.apache.tapestry.components;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;

/**
 * A component that renders an HTML &lt;a&gt; element. It exposes some
 * properties to the components it wraps. This is basically to facilitate the
 * {@link org.apache.tapestry.html.Rollover} component.
 * 
 * @author Howard Lewis Ship
 */

public interface ILinkComponent extends IComponent
{

    /**
     * Returns the desired scheme (i.e., "http" or "https") for the link, or
     * null to not output a specific scheme (in which case the URL will fall
     * under the incoming request's scheme).
     *
     * @return The scheme portion of the url to be generated. 
     * @since 4.0
     */

    String getScheme();

    /**
     * Returns the desired port (i.e., "80" or "443") for the link, or null to
     * not output a specific port (in which case the URL will fall under the
     * incoming request's port).
     *
     * @return The http port to use.
     * @since 4.1
     */

    Integer getPort();

    /**
     * Returns whether this service link component is enabled or disabled.
     *
     * @return True if disabled, false otherwise.
     * @since 0.2.9
     */

    boolean isDisabled();

    /**
     * Returns the anchor defined for this link, or null for no anchor.
     *
     * @return The <code>#anchorid</code> portion of the url to be generated - if any.
     * @since 3.0
     */

    String getAnchor();

    /**
     * Returns the name of the target window or frame for this link, or null if
     * current window or frame is to be used.
     *
     * @return The <code>target="_this"</code> portion of the link to be generated - if any.
     * @since 4.0
     */
    String getTarget();

    /**
     * Adds a new event handler. When the event occurs, the JavaScript function
     * specified is executed. Multiple functions can be specified, in which case
     * all of them are executed.
     * <p>
     * This was created for use by {@link org.apache.tapestry.html.Rollover} to
     * set mouse over and mouse out handlers on the {@link ILinkComponent} that
     * wraps it, but can be used for many other things as well.
     *
     * @param type
     *          The type of event to add.
     * @param functionName
     *          The name of the client side javascript function to generate.
     *
     * @since 0.2.9
     * @deprecated To be removed in Tapestry 4.1.4. 
     */

    void addEventHandler(LinkEventType type, String functionName);

    /**
     * Invoked by the {@link org.apache.tapestry.link.ILinkRenderer} (if the
     * link is not disabled) to provide a
     * {@link org.apache.tapestry.engine.EngineServiceLink} that the renderer
     * can convert into a URL.
     *
     * @param cycle
     *          The current request.
     * @return A {@link ILink} instance representing the link information for this component.
     */

    ILink getLink(IRequestCycle cycle);

    /**
     * Invoked (by the {@link org.apache.tapestry.link.ILinkRenderer}) to make
     * the link render any additional attributes. These are informal parameters,
     * plus any attributes related to events. This is only invoked for
     * non-disabled links.
     *
     * @param writer
     *          Markup writer to write content to.
     * @param cycle
     *          The current request.
     * @since 3.0
     */

    void renderAdditionalAttributes(IMarkupWriter writer, IRequestCycle cycle);
}
