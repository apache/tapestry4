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

package org.apache.tapestry.engine;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.parse.ComponentTemplate;

/**
 * A source of localized HTML templates for components.  
 * The cache is the means of access for components to load thier templates,
 * which they need not do until just before rendering.
 *
 * <p>The template cache must be able to locate and parse templates as needed.
 * It may maintain templates in memory.
 *
 * @author Howard Ship
 * @version $Id$
 * 
 **/

public interface ITemplateSource
{
    /**
     *  Name of an {@link org.apache.tapestry.IAsset} of a component that provides the template
     *  for the asset.  This overrides the default (that the template is in
     *  the same directory as the specification).  This allows
     *  pages or component templates to be located properly, relative to static
     *  assets (such as images and stylesheets).
     * 
     *  @since 2.2
     * 
     **/
    
    public static final String TEMPLATE_ASSET_NAME = "$template";

    /**
     *  Name of the component parameter that will be automatically bound to
     *  the HTML tag that is used to insert the component in the parent template.
     *  If the parent component does not have a template (i.e. it extends 
     *  AbstractComponent, not BaseComponent), then this parameter is bound to null.
     * 
     *  @since 3.0
     * 
     **/
    
    public static final String TEMPLATE_TAG_PARAMETER_NAME = "templateTag";
    
    /**
     *  Locates the template for the component.
     * 
     *  @param cycle The request cycle loading the template; this is required
     *  in some cases when the template is loaded from an {@link org.apache.tapestry.IAsset}.
     *  @param component The component for which a template should be loaded.
     *
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the resource cannot be located or loaded.
     * 
     **/

    public ComponentTemplate getTemplate(IRequestCycle cycle, IComponent component);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();
}