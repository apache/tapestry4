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

package org.apache.tapestry.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.IScriptSource;

/**
 * Works with the {@link Body}component to add a script (and perhaps some
 * initialization) to the HTML response. [ <a
 * href="../../../../../ComponentReference/Script.html">Component Reference
 * </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class Script extends AbstractComponent
{
    /**
     * A Map of input and output symbols visible to the body of the Script.
     * 
     * @since 2.2
     */

    private Map _symbols;

    /**
     * Injected.
     * 
     * @since 4.0
     */

    public abstract IScriptSource getScriptSource();
    
    /**
     * Constructs the symbols {@link Map}. This starts with the contents of the
     * symbols parameter (if specified) to which is added any informal
     * parameters. If both a symbols parameter and informal parameters are
     * bound, then a copy of the symbols parameter's value is made (that is, the
     * {@link Map}provided by the symbols parameter is read, but not modified).
     */

    private Map getInputSymbols()
    {
        Map result = new HashMap();

        Map baseSymbols = getBaseSymbols();

        if (baseSymbols != null) result.putAll(baseSymbols);

        // Now, iterate through all the binding names (which includes both
        // formal and informal parmeters). Skip the formal ones and
        // access the informal ones.

        Iterator i = getBindingNames().iterator();
        while(i.hasNext())
        {
            String bindingName = (String) i.next();

            // Skip formal parameters

            if (getSpecification().getParameter(bindingName) != null) continue;

            IBinding binding = getBinding(bindingName);

            Object value = binding.getObject();

            result.put(bindingName, value);
        }

        return result;
    }

    /**
     * Gets the {@link IScript}for the correct script.
     */

    private IScript getParsedScript()
    {
        IAsset scriptAsset = getScriptAsset();
        String scriptPath = getScriptPath();

        // only one of the two is allowed
        if (scriptAsset != null && scriptPath != null)
            throw new ApplicationRuntimeException(HTMLMessages
                    .multiAssetParameterError(getBinding("scriptAsset"),
                            getBinding("script")));

        if (scriptPath == null && scriptAsset == null)
            throw new ApplicationRuntimeException(HTMLMessages
                    .noScriptPathError());

        IScriptSource source = getScriptSource();

        Resource scriptLocation = null;
        if (scriptPath != null)
        {

            // If the script path is relative, it should be relative to the
            // Script component's
            // container (i.e., relative to a page in the application).

            Resource rootLocation = getContainer().getSpecification()
                    .getSpecificationLocation();
            scriptLocation = rootLocation.getRelativeResource(scriptPath);
        }
        else scriptLocation = scriptAsset.getResourceLocation();

        try
        {
            return source.getScript(scriptLocation);
        }
        catch (RuntimeException ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), this,
                    getBinding("script").getLocation(), ex);
        }

    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding())
        {
            PageRenderSupport pageRenderSupport = TapestryUtils
                    .getPageRenderSupport(cycle, this);
            
            _symbols = getInputSymbols();
            
            getParsedScript().execute(this, cycle, pageRenderSupport, _symbols);
        }

        // Render the body of the Script;
        renderBody(writer, cycle);
    }

    public abstract String getScriptPath();

    public abstract IAsset getScriptAsset();

    // Parameter

    public abstract Map getBaseSymbols();

    /**
     * Returns the complete set of symbols (input and output) from the script
     * execution. This is visible to the body of the Script, but is cleared
     * after the Script finishes rendering.
     * 
     * @since 2.2
     */

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
