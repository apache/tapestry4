//  Copyright 2004 The Apache Software Foundation
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
 *  A component that renders an HTML &lt;a&gt; element.  It exposes some
 *  properties to the components it wraps.  This is basically to facilitate
 *  the {@link org.apache.tapestry.html.Rollover} component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface ILinkComponent extends IComponent
{

    /**
     *  Returns whether this service link component is enabled or disabled.
     *
     *  @since 0.2.9
     *
     **/

    public boolean isDisabled();

    /**
     *  Returns the anchor defined for this link, or null for no anchor.
     * 
     *  @since 3.0
     * 
     **/

    public String getAnchor();

    /**
     *  Adds a new event handler.  When the event occurs, the JavaScript function
     *  specified is executed.  Multiple functions can be specified, in which case
     *  all of them are executed.
     *
     *  <p>This was created for use by
     *  {@link org.apache.tapestry.html.Rollover} to set mouse over and mouse out handlers on
     *  the {@link ILinkComponent} that wraps it, but can be used for
     *  many other things as well.
     *
     *  @since 0.2.9
     **/

    public void addEventHandler(LinkEventType type, String functionName);

    /**
     *  Invoked by the {@link org.apache.tapestry.link.ILinkRenderer} (if
     *  the link is not disabled) to provide a
     *  {@link org.apache.tapestry.engine.EngineServiceLink} that the renderer can convert
     *  into a URL.
     * 
     **/

    public ILink getLink(IRequestCycle cycle);

    /**
     *  Invoked (by the {@link org.apache.tapestry.link.ILinkRenderer})
     *  to make the link render any additional attributes.  These
     *  are informal parameters, plus any attributes related to events.
     *  This is only invoked for non-disabled links.
     * 
     *  @since 3.0
     * 
     **/

    public void renderAdditionalAttributes(IMarkupWriter writer, IRequestCycle cycle);
}