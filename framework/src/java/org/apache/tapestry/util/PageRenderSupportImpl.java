// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.asset.PrivateAsset;
import org.apache.tapestry.engine.IEngineService;

/**
 * Implementation of {@link org.apache.tapestry.PageRenderSupport}. The
 * {@link org.apache.tapestry.html.Body}&nbsp;component uses an instance of this class.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PageRenderSupportImpl implements Locatable, PageRenderSupport
{
    private final IEngineService _assetService;

    private final Location _location;

    // Lines that belong inside the onLoad event handler for the <body> tag.
    private StringBuffer _initializationScript;

    // Any other scripting desired

    private StringBuffer _bodyScript;

    // Contains text lines related to image initializations

    private StringBuffer _imageInitializations;

    /**
     * Map of URLs to Strings (preloaded image references).
     */

    private Map _imageMap;

    /**
     * List of included scripts. Values are Strings.
     * 
     * @since 1.0.5
     */

    private List _externalScripts;

    private IdAllocator _idAllocator;

    public PageRenderSupportImpl(IEngineService assetService, Location location)
    {
        Defense.notNull(assetService, "assetService");

        _assetService = assetService;
        _location = location;
    }

    /**
     * Returns the location, which may be used in error messages. In practical terms, this is the
     * location of the {@link org.apache.tapestry.html.Body}&nbsp;component.
     */

    public Location getLocation()
    {
        return _location;
    }

    public String getPreloadedImageReference(String URL)
    {
        if (_imageMap == null)
            _imageMap = new HashMap();

        String reference = (String) _imageMap.get(URL);

        if (reference == null)
        {
            int count = _imageMap.size();
            String varName = "tapestry_preload[" + count + "]";
            reference = varName + ".src";

            if (_imageInitializations == null)
                _imageInitializations = new StringBuffer();

            _imageInitializations.append("  ");
            _imageInitializations.append(varName);
            _imageInitializations.append(" = new Image();\n");
            _imageInitializations.append("  ");
            _imageInitializations.append(reference);
            _imageInitializations.append(" = \"");
            _imageInitializations.append(URL);
            _imageInitializations.append("\";\n");

            _imageMap.put(URL, reference);
        }

        return reference;
    }

    public void addBodyScript(String script)
    {
        if (_bodyScript == null)
            _bodyScript = new StringBuffer(script.length());

        _bodyScript.append(script);
    }

    public void addInitializationScript(String script)
    {
        if (_initializationScript == null)
            _initializationScript = new StringBuffer(script.length() + 1);

        _initializationScript.append(script);
        _initializationScript.append('\n');
    }

    public void addExternalScript(Resource scriptLocation)
    {
        if (_externalScripts == null)
            _externalScripts = new ArrayList();

        if (_externalScripts.contains(scriptLocation))
            return;

        // Alas, this won't give a good Location for the actual problem.

        if (!(scriptLocation instanceof ClasspathResource))
            throw new ApplicationRuntimeException(Tapestry.format(
                    "Body.include-classpath-script-only",
                    scriptLocation), this, null, null);

        // Record the URL so we don't include it twice.

        _externalScripts.add(scriptLocation);

    }

    public String getUniqueString(String baseValue)
    {
        if (_idAllocator == null)
            _idAllocator = new IdAllocator();

        return _idAllocator.allocateId(baseValue);
    }

    private void writeExternalScripts(IMarkupWriter writer, IRequestCycle cycle)
    {
        int count = Tapestry.size(_externalScripts);
        for (int i = 0; i < count; i++)
        {
            ClasspathResource scriptLocation = (ClasspathResource) _externalScripts.get(i);

            // This is still very awkward! Should move the code inside PrivateAsset somewhere
            // else, so that an asset does not have to be created to to build the URL.
            PrivateAsset asset = new PrivateAsset(scriptLocation, _assetService, null);
            String url = asset.buildURL(cycle);

            // Note: important to use begin(), not beginEmpty(), because browser don't
            // interpret <script .../> properly.

            writer.begin("script");
            writer.attribute("language", "JavaScript");
            writer.attribute("type", "text/javascript");
            writer.attribute("src", url);
            writer.end();
            writer.println();
        }
    }

    /**
     * Writes a single large JavaScript block containing:
     * <ul>
     * <li>Any image initializations (via {@link #getPreloadedImageReference(String)})
     * <li>Any included scripts (via {@link #addExternalScript(Resource)})
     * <li>Any contributions (via {@link #addBodyScript(String)})
     * </ul>
     * 
     * @see #writeInitializationScript(IMarkupWriter)
     */

    public void writeBodyScript(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!Tapestry.isEmpty(_externalScripts))
            writeExternalScripts(writer, cycle);

        if (!(any(_bodyScript) || any(_imageInitializations)))
            return;

        writer.begin("script");
        writer.attribute("language", "JavaScript");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--");

        if (any(_imageInitializations))
        {
            writer.printRaw("\n\nvar tapestry_preload = new Array();\n");
            writer.printRaw("if (document.images)\n");
            writer.printRaw("{\n");
            writer.printRaw(_imageInitializations.toString());
            writer.printRaw("}\n");
        }

        if (any(_bodyScript))
        {
            writer.printRaw("\n\n");
            writer.printRaw(_bodyScript.toString());
        }

        writer.printRaw("\n\n// -->");
        writer.end();
    }

    /**
     * Writes any image initializations; this should be invoked at the end of the render, after all
     * the related HTML will have already been streamed to the client and parsed by the web browser.
     * Earlier versions of Tapestry uses a <code>window.onload</code> event handler.
     */

    public void writeInitializationScript(IMarkupWriter writer)
    {
        if (!any(_initializationScript))
            return;

        writer.begin("script");
        writer.attribute("language", "JavaScript");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--\n");

        writer.printRaw(_initializationScript.toString());

        writer.printRaw("\n// -->");
        writer.end();
    }

    private boolean any(StringBuffer buffer)
    {
        return buffer != null && buffer.length() > 0;
    }
}