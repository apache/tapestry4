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
package net.sf.tapestry.contrib.inspector;

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.html.Body;

/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 * 
 *  [<a href="../../../../../ComponentReference/InspectorButton.html">Component Reference</a>]
 *
 *  <p>Because the InspectorButton component is implemented using a {@link net.sf.tapestry.html.Rollover},
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

    public void trigger(IRequestCycle cycle) throws RequestCycleException
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
        throws RequestCycleException
    {
        if (_disabled || cycle.isRewinding())
            return;

        IEngine engine = getPage().getEngine();
        IScriptSource source = engine.getScriptSource();

        IResourceLocation scriptLocation =
            getSpecification().getSpecificationLocation().getRelativeLocation(
                "InspectorButton.script");

        IScript script = source.getScript(scriptLocation);

        Map symbols = new HashMap();

        IEngineService service = engine.getService(IEngineService.DIRECT_SERVICE);
        Gesture g = service.buildGesture(cycle, this, null);

        symbols.put("URL", g.getURL());

        ScriptSession scriptSession = null;

        try
        {
            scriptSession = script.execute(symbols);
        }
        catch (ScriptException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(
                Tapestry.getString("InspectorButton.must-be-contained-by-body"),
                this);

        body.process(scriptSession);

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