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

import org.apache.commons.lang.StringUtils;
import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.services.ResponseBuilder;

/**
 * Implementation of {@link org.apache.tapestry.PageRenderSupport}. The
 * {@link org.apache.tapestry.html.Body}&nbsp;component uses an instance of this class.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PageRenderSupportImpl implements Locatable, PageRenderSupport
{
    private final AssetFactory _assetFactory;

    private final Location _location;

    private final ResponseBuilder _builder;
    
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

    private final IdAllocator _idAllocator;

    private final String _preloadName;
    
    private final Map _requires = new HashMap();
    
    public PageRenderSupportImpl(AssetFactory assetFactory, String namespace, 
            Location location, ResponseBuilder builder)
    {
        Defense.notNull(assetFactory, "assetService");
        
        _assetFactory = assetFactory;
        _location = location;
        _idAllocator = new IdAllocator(namespace);
        _builder = builder;
        
        _preloadName = (namespace.equals("") ? "tapestry." : namespace) + "preload";
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
        return getPreloadedImageReference(null, URL);
    }
    
    public String getPreloadedImageReference(IComponent target, IAsset source)
    {
        return getPreloadedImageReference(target, source.buildURL());
    }
    
    public String getPreloadedImageReference(IComponent target, String URL)
    {
        if (target != null 
                && !_builder.isImageInitializationAllowed(target))
            return URL;
        
        if (_imageMap == null)
            _imageMap = new HashMap();
        
        String reference = (String) _imageMap.get(URL);

        if (reference == null)
        {
            int count = _imageMap.size();
            String varName = _preloadName + "[" + count + "]";
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
        addBodyScript(null, script);
    }
    
    public void addBodyScript(IComponent target, String script)
    {
        if (!_builder.isBodyScriptAllowed(target)) 
            return;
        
        String val = stripDuplicateIncludes(script);
        
        if (_bodyScript == null)
            _bodyScript = new StringBuffer(val.length());
        
        _bodyScript.append("\n").append(val);
    }
    
    public void addInitializationScript(String script)
    {
        addInitializationScript(null, script);
    }

    public void addInitializationScript(IComponent target, String script)
    {
        if (!_builder.isInitializationScriptAllowed(target)) 
            return;
        
        String val = stripDuplicateIncludes(script);
        
        if (_initializationScript == null)
            _initializationScript = new StringBuffer(val.length() + 1);
        
        _initializationScript.append("\n").append(val);
    }
    
    /**
     * Provides a mechanism to strip out duplicate dojo.require calls made in script
     * templates in order to reduce amount of redundant javascript written to client.
     * 
     * @param input The incoming script string to check for requires.
     * @return The input string stripped of all known dojo.require calls, if any.
     */
    String stripDuplicateIncludes(String input)
    {
        String[] lines = StringUtils.splitPreserveAllTokens(input, ';');
        
        if (lines == null || lines.length < 1)
            return input;
        
        String ret = input;
        
        for (int i=0; i < lines.length; i++) {
            if (lines[i].indexOf("dojo.require") < 0)
                continue;
            
            String line = StringUtils.stripToEmpty(lines[i]);
            
            if (_requires.containsKey(line)) {
                ret = StringUtils.replaceOnce(ret, line+";", "");
            } else {
                _requires.put(line, "t");
            }
        }
        
        return StringUtils.stripToEmpty(ret.trim());
    }
    
    public void addExternalScript(Resource scriptLocation)
    {
        addExternalScript(null, scriptLocation);
    }
    
    public void addExternalScript(IComponent target, Resource scriptLocation)
    {
        if (!_builder.isExternalScriptAllowed(target)) 
            return;
        
        if (_externalScripts == null)
            _externalScripts = new ArrayList();
        
        if (_externalScripts.contains(scriptLocation))
            return;

        // Record the Resource so we don't include it twice.

        _externalScripts.add(scriptLocation);
    }

    public String getUniqueString(String baseValue)
    {
        return _idAllocator.allocateId(baseValue);
    }
    
    private void writeExternalScripts(IMarkupWriter writer, IRequestCycle cycle)
    {
        int count = Tapestry.size(_externalScripts);
        for (int i = 0; i < count; i++)
        {
            Resource scriptLocation = (Resource) _externalScripts.get(i);
            
            IAsset asset = _assetFactory.createAsset(scriptLocation, null);
            
            String url = asset.buildURL();
            
            // Note: important to use begin(), not beginEmpty(), because browser don't
            // interpret <script .../> properly.
            
            _builder.writeExternalScript(writer, url, cycle);
        }
    }
    
    /**
     * Writes a single large JavaScript block containing:
     * <ul>
     * <li>Any image initializations (via {@link #getPreloadedImageReference(IComponent, String)}).
     * <li>Any included scripts (via {@link #addExternalScript(Resource)}).
     * <li>Any contributions (via {@link #addBodyScript(String)}).
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
        
        _builder.beginBodyScript(writer, cycle);
        
        if (any(_imageInitializations))
        {
            _builder.writeImageInitializations(writer, StringUtils.stripToEmpty(_imageInitializations.toString())
                    , _preloadName, cycle);
        }
        
        if (any(_bodyScript))
        {
            _builder.writeBodyScript(writer, StringUtils.stripToEmpty(_bodyScript.toString())
                    , 
                    cycle);
        }
        
        _builder.endBodyScript(writer, cycle);
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
        
        _builder.writeInitializationScript(writer, StringUtils.stripToEmpty(_initializationScript.toString())
                );
    }

    private boolean any(StringBuffer buffer)
    {
        return buffer != null && buffer.length() > 0;
    }
}
