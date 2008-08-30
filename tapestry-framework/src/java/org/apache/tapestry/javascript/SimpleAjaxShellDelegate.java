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
    private JavascriptManager _javascriptManager;

    public SimpleAjaxShellDelegate(JavascriptManager javascriptManager)
    {
        _javascriptManager = javascriptManager;
    }
    
    protected JavascriptManager getJavascriptManager() {
    	return _javascriptManager;
    }

    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {   
        IPage page = cycle.getPage();
        
        processPath(writer, cycle, _javascriptManager.getPath());
        
        _javascriptManager.renderLibraryResources(writer, cycle, page.hasFormComponents(), page.hasWidgets());
        
        processTapestryPath(writer, cycle, _javascriptManager.getTapestryPath());
        
        _javascriptManager.renderLibraryAdaptor(writer, cycle);
    }
    
    /**
     * Called before including any javascript. It does nothing by default, but allows
     * subclasses to change this behavior.
     * @param writer   
     * @param cycle
     * @param path The base path to the javascript files. May be null.
     */
    protected void processPath(IMarkupWriter writer, IRequestCycle cycle, IAsset path) 
    {
    }  
    
    /**
     * Called before including tapestry's base javascript. It does nothing by default, 
     * but allows subclasses to change this behavior.
     * @param writer   
     * @param cycle
     * @param path The base path to the tapestry javascript file. May be null.
     */    
    protected void processTapestryPath(IMarkupWriter writer, IRequestCycle cycle, IAsset path) 
    {
    }
}
