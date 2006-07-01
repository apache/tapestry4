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

package org.apache.tapestry.form;

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.json.JSONObject;

/**
 * Implementation of {@link org.apache.tapestry.form.FormComponentContributorContext}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class FormComponentContributorContextImpl extends ValidationMessagesImpl implements
        FormComponentContributorContext
{
    private final ClassResolver _resolver;

    private final PageRenderSupport _pageRenderSupport;

    private final IFormComponent _field;

    private final IForm _form;

    private final String _formId;
    
    /**
     * Used only for testing.
     */

    FormComponentContributorContextImpl(IFormComponent field)
    {
        super(field, Locale.ENGLISH);

        _field = field;
        _resolver = null;
        _formId = null;
        _pageRenderSupport = null;
        _form = null;
    }

    public FormComponentContributorContextImpl(Locale locale, IRequestCycle cycle,
            IFormComponent field)
    {
        super(field, locale);

        _field = field;
        _form = field.getForm();
        _formId = _form.getName();

        _resolver = cycle.getInfrastructure().getClassResolver();

        _pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, field);
    }

    public void includeClasspathScript(String path)
    {
        Resource resource = new ClasspathResource(_resolver, path);

        _pageRenderSupport.addExternalScript(resource);
    }

    public void addSubmitHandler(String submitListener)
    {
        _pageRenderSupport.addInitializationScript("Tapestry.onsubmit('" + _formId + "', "
                + submitListener + ");");
    }
    
    public void addInitializationScript(IComponent target, String script)
    {
        _pageRenderSupport.addInitializationScript(target, script);
    }
    
    public void registerForFocus(int priority)
    {
        _form.registerForFocus(_field, priority);
    }

    public JSONObject getProfile()
    {
        return _form.getProfile();
    }
}
