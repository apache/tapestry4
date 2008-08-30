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
import org.apache.tapestry.IRequestCycle;

/**
 * Manages javascript files of 3rd party libraries.
 */
public interface JavascriptManager
{
    /**
     * The javascript files that should always be included.
     *
     * @return A not-null (but possibly empty) list of {@link IAsset}s.
     */
    List getAssets();

    IAsset getFirstAsset();

    /**
     * The javascript files that provide form-related functionality.
     * They're dynamically included when the page contains forms.
     *
     * @return A not-null (but possibly empty) list of {@link IAsset}s.
     */
    List getFormAssets();
    
    IAsset getFirstFormAsset();

    /**
     * The javascript files that provide widget-related functionality.
     * They're dynamically included when the page contains widgets.
     *
     * @return A not-null (but possibly empty) list of {@link IAsset}s.
     */
    List getWidgetAssets();

    IAsset getFirstWidgetAsset();

    /**
     * The base path to the javascript files.
     *
     * @return if null, it is left unused.
     */
    IAsset getPath();

    /**
     * The tapestry js file.
     *
     * @return if null then no tapestry file is included.
     */
    IAsset getTapestryAsset();

    /**
     * The base path to the tapestry js files.
     * 
     * @return if null, it is left unused.
     */
    IAsset getTapestryPath();
    
    /**
     * Output the resources (could be .js, .css, e.t.c. files) needed for the current javascript library.
     * 
     * @param writer
     * @param cycle
     * @param hasForm true if current page includes forms.
     * @param hasWidget true if current page includes widgets.
     */
    void renderLibraryResources(IMarkupWriter writer, IRequestCycle cycle, boolean hasForm, boolean hasWidget);
    
    /**
     * Output the resources needed for tapestry in order to use the current 
     * javascript library.
     * 
     * @param writer
     * @param cycle
     */
    void renderLibraryAdaptor(IMarkupWriter writer, IRequestCycle cycle);
}
