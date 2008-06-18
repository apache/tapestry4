// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.javascript;

import java.util.List;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Outputs the main js packages and the tapestry js 
 * that are defined by the {@link JavascriptManager} service.
 */
public class SimpleAjaxShellDelegate implements IRender
{
    private static final String SYSTEM_NEWLINE = "\n";

    protected JavascriptManager _javascriptManager;

    public SimpleAjaxShellDelegate(JavascriptManager javascriptManager)
    {
        _javascriptManager = javascriptManager;
    }

    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {        
        StringBuffer str = new StringBuffer(300);
        
        processPath(str, cycle, _javascriptManager.getPath());

        // include all the main js packages
        appendAssetsAsJavascript(str, cycle, _javascriptManager.getAssets());
        
        IPage page = cycle.getPage();
        if (page.hasFormComponents())
        {
            appendAssetsAsJavascript(str, cycle, _javascriptManager.getFormAssets());
        }
        if (page.hasWidgets())
        {
            appendAssetsAsJavascript(str, cycle, _javascriptManager.getWidgetAssets());
        }
        
        processTapestryPath(str, cycle, _javascriptManager.getTapestryPath());
        
        // include the tapestry js
        IAsset tapestryAsset = _javascriptManager.getTapestryAsset();
        if (tapestryAsset!=null)
        {
            appendAssetAsJavascript(str, cycle, tapestryAsset);
        }

        writer.printRaw(str.toString());
        writer.println();
    }
    
    /**
     * Called before including any javascript. It does nothing by default, but allows
     * subclasses to change this behavior.  
     * @param cycle
     * @param str 
     * @param path The base path to the javascript files. May be null.
     */
    protected void processPath(StringBuffer str, IRequestCycle cycle, IAsset path) 
    {
    }  
    
    /**
     * Called before including tapestry's base javascript. It does nothing by default, 
     * but allows subclasses to change this behavior.  
     * @param cycle
     * @param str 
     * @param path The base path to the tapestry javascript file. May be null.
     */    
    protected void processTapestryPath(StringBuffer str, IRequestCycle cycle, IAsset path) 
    {
    }      
    
    /**
     * Appends a script tag to include the given asset. 
     * @param str
     * @param cycle
     * @param asset
     */
    protected void appendAssetAsJavascript(StringBuffer str, IRequestCycle cycle, IAsset asset)
    {
        final String url = asset.buildURL();
        str.append("<script type=\"text/javascript\" src=\"").append(url)
                .append("\"></script>").append(SYSTEM_NEWLINE);
        
    }        

    private void appendAssetsAsJavascript(StringBuffer str, IRequestCycle cycle, List jsAssets)
    {
        for (int i = 0; i < jsAssets.size(); i++)
        {
            appendAssetAsJavascript(str, cycle, (IAsset) jsAssets.get(i));
        }
    }
}
