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

package org.apache.tapestry.services.impl;

import org.apache.commons.logging.Log;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ITemplateComponent;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.services.ComponentTemplateLoader;
import org.apache.tapestry.services.TemplateSource;

/**
 * Utility service, <code>tapestry.page.ComponentTemplateLoader</code>, which will process the
 * component's {@link org.apache.tapestry.parse.ComponentTemplate template}, which involves working
 * through the nested structure of the template and hooking the various static template blocks and
 * components together using {@link org.apache.tapestry.IComponent#addBody(IRender)}and
 * {@link org.apache.tapestry.ITemplateComponent#addOuter(IRender)}.
 * <p>
 * Because this service must be reentrant, it acts as a factory for a
 * {@link org.apache.tapestry.services.impl.ComponentTemplateLoaderLogic}that is created (and
 * discarded) for each component whose template is loaded.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class ComponentTemplateLoaderImpl implements ComponentTemplateLoader
{
    private Log _log;

    private IPageLoader _pageLoader;

    private TemplateSource _templateSource;

    /** @since 4.0 */

    private BindingSource _bindingSource;

    public void loadTemplate(IRequestCycle requestCycle, ITemplateComponent loadComponent)
    {
        ComponentTemplate template = _templateSource.getTemplate(requestCycle, loadComponent);

        ComponentTemplateLoaderLogic logic = new ComponentTemplateLoaderLogic(_log, _pageLoader,
                _bindingSource);

        logic.loadTemplate(requestCycle, loadComponent, template);
    }

    /** @since 4.0 */

    public void setPageLoader(IPageLoader pageLoader)
    {
        _pageLoader = pageLoader;
    }

    /** @since 4.0 */

    public void setLog(Log log)
    {
        _log = log;
    }

    /**
     * @since 4.0
     */

    public void setTemplateSource(TemplateSource templateSource)
    {
        _templateSource = templateSource;
    }

    /**
     * @since 4.0
     */

    public void setBindingSource(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }
}