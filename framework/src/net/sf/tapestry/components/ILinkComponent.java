/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.components;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.engine.ILink;

/**
 *  A component that renders an HTML &lt;a&gt; element.  It exposes some
 *  properties to the components it wraps.  This is basically to facilitate
 *  the {@link net.sf.tapestry.html.Rollover} component.
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
     *  @since 2.4
     * 
     **/

    public String getAnchor();

    /**
     *  Adds a new event handler.  When the event occurs, the JavaScript function
     *  specified is executed.  Multiple functions can be specified, in which case
     *  all of them are executed.
     *
     *  <p>This was created for use by
     *  {@link net.sf.tapestry.html.Rollover} to set mouse over and mouse out handlers on
     *  the {@link ILinkComponent} that wraps it, but can be used for
     *  many other things as well.
     *
     *  @since 0.2.9
     **/

    public void addEventHandler(LinkEventType type, String functionName);

    /**
     *  Invoked by the {@link net.sf.tapestry.link.ILinkRenderer} (if
     *  the link is not disabled) to provide a
     *  {@link net.sf.tapestry.EngineServiceLink} that the renderer can convert
     *  into a URL.
     * 
     **/

    public ILink getLink(IRequestCycle cycle) throws RequestCycleException;

    /**
     *  Invoked (by the {@link net.sf.tapestry.link.ILinkRenderer})
     *  to make the link render any additional attributes.  These
     *  are informal parameters, plus any attributes related to events.
     *  This is only invoked for non-disabled links.
     * 
     *  @since 2.4
     * 
     **/

    public void renderAdditionalAttributes(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException;
}