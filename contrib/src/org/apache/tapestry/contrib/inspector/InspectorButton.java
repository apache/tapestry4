/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.contrib.inspector;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.commons.hivemind.Resource;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;

/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 * 
 *  [<a href="../../../../../ComponentReference/InspectorButton.html">Component Reference</a>]
 *
 *  <p>Because the InspectorButton component is implemented using a {@link org.apache.tapestry.html.Rollover},
 *  the containing page must use a {@link Body} component instead of
 *  a &lt;body&gt; tag.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class InspectorButton extends BaseComponent implements IDirect
{
    private boolean _disabled = false;

    /**
     *  Gets the listener for the link component.
     *
     *  @since 1.0.5
     **/

    public void trigger(IRequestCycle cycle)
    {
        String name = getNamespace().constructQualifiedName("Inspector");

        Inspector inspector = (Inspector) cycle.getPage(name);

        inspector.inspect(getPage().getPageName(), cycle);
    }

    /**
     *  Renders the script, then invokes the normal implementation.
     *
     *  @since 1.0.5
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_disabled || cycle.isRewinding())
            return;

        IEngine engine = getPage().getEngine();
        IScriptSource source = engine.getScriptSource();

        Resource scriptLocation =
            getSpecification().getSpecificationLocation().getRelativeResource(
                "InspectorButton.script");

        IScript script = source.getScript(scriptLocation);

        Map symbols = new HashMap();

        IEngineService service = engine.getService(Tapestry.DIRECT_SERVICE);
        ILink link = service.getLink(cycle, this, null);

        symbols.put("URL", link.getURL());

        Body body = Body.get(cycle);

        if (body == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("InspectorButton.must-be-contained-by-body"),
                this,
                null,
                null);

        script.execute(cycle, body, symbols);

        // Now, go render the rest from the template.

        super.renderComponent(writer, cycle);
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     *  Always returns false.
     * 
     *  @since 2.3
     * 
     **/

    public boolean isStateful()
    {
        return false;
    }

}