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
package net.sf.tapestry.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;

/**
 *  Works with the {@link Body} component to add a script (and perhaps some initialization) 
 *  to the HTML response.
 *
 *  [<a href="../../../../../ComponentReference/Script.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Script extends AbstractComponent
{
    private static final Log LOG = LogFactory.getLog(Script.class);

    private String _scriptPath;
    private Map _baseSymbols;

    /**
     *  A Map of input and output symbols visible to the body of the Script.
     * 
     *  @since 2.2
     * 
     **/

    private Map _symbols;

    /**
     *  Constructs the symbols {@link Map}.  This starts with the
     *  contents of the symbols parameter (if specified) to which is added
     *  any informal parameters.  If both a symbols parameter and informal
     *  parameters are bound, then a copy of the symbols parameter's value is made
     *  (that is, the {@link Map} provided by the symbols parameter is read, but not modified).
     *
     **/

    private Map getInputSymbols()
    {
        Map result = new HashMap();

        if (_baseSymbols != null)
            result.putAll(_baseSymbols);

        // Now, iterate through all the binding names (which includes both
        // formal and informal parmeters).  Skip the formal ones and
        // access the informal ones.

        Iterator i = getBindingNames().iterator();
        while (i.hasNext())
        {
            String bindingName = (String) i.next();

            // Skip formal parameters

            if (getSpecification().getParameter(bindingName) != null)
                continue;

            IBinding binding = getBinding(bindingName);

            Object value = binding.getObject();

            result.put(bindingName, value);
        }

        return result;
    }

    /**
     *  Gets the {@link IScript} for the correct script.
     *
     *
     **/

    private IScript getParsedScript(IRequestCycle cycle) throws RequestCycleException
    {
        if (_scriptPath == null)
            throw new RequiredParameterException(this, "scriptPath", getBinding("scriptPath"));

        IEngine engine = cycle.getEngine();
        IScriptSource source = engine.getScriptSource();

        // If the script path is relative, it should be relative to the Script component's
        // container (i.e., relative to a page in the application).

        IResourceLocation rootLocation = getContainer().getSpecification().getSpecificationLocation();
        IResourceLocation scriptLocation = rootLocation.getRelativeLocation(_scriptPath);

        try
        {
            return source.getScript(scriptLocation);
        }
        catch (RuntimeException ex)
        {
            throw new RequestCycleException(this, ex);
        }

    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        ScriptSession session;

        if (!cycle.isRewinding())
        {
            Body body = Body.get(cycle);

            if (body == null)
                throw new RequestCycleException(Tapestry.getString("Script.must-be-contained-by-body"), this);

            _symbols = getInputSymbols();

            try
            {
                session = getParsedScript(cycle).execute(_symbols);
            }
            catch (ScriptException ex)
            {
                throw new RequestCycleException(this, ex);
            }

            body.process(session);
        }

        // Render the body of the Script;
        renderBody(writer, cycle);
    }

    public String getScriptPath()
    {
        return _scriptPath;
    }

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    public Map getBaseSymbols()
    {
        return _baseSymbols;
    }

    public void setBaseSymbols(Map baseSymbols)
    {
        _baseSymbols = baseSymbols;
    }

    /**
     *  Returns the complete set of symbols (input and output)
     *  from the script execution.  This is visible to the body
     *  of the Script, but is cleared after the Script
     *  finishes rendering.
     * 
     *  @since 2.2
     **/

    public Map getSymbols()
    {
        return _symbols;
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _symbols = null;

        super.cleanupAfterRender(cycle);
    }

}