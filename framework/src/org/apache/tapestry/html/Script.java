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

package org.apache.tapestry.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IScriptSource;

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

public abstract class Script extends AbstractComponent
{
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

    private IScript getParsedScript(IRequestCycle cycle)
    {
        String scriptPath = getScriptPath();

        if (scriptPath == null)
            throw Tapestry.createRequiredParameterException(this, "scriptPath");

        IEngine engine = cycle.getEngine();
        IScriptSource source = engine.getScriptSource();

        // If the script path is relative, it should be relative to the Script component's
        // container (i.e., relative to a page in the application).

        IResourceLocation rootLocation =
            getContainer().getSpecification().getSpecificationLocation();
        IResourceLocation scriptLocation = rootLocation.getRelativeLocation(scriptPath);

        try
        {
            return source.getScript(scriptLocation);
        }
        catch (RuntimeException ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), this, null, ex);
        }

    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding())
        {
            Body body = Body.get(cycle);

            if (body == null)
                throw new ApplicationRuntimeException(
                    Tapestry.getMessage("Script.must-be-contained-by-body"),
                    this,
                    null,
                    null);

            _symbols = getInputSymbols();

            getParsedScript(cycle).execute(cycle, body, _symbols);
        }

        // Render the body of the Script;
        renderBody(writer, cycle);
    }

    public abstract String getScriptPath();

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