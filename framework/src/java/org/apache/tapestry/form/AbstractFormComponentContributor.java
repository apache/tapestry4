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

import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;

/**
 * Abstract {@link FormComponentContributor} implementation that adds an optional static 
 * javscript method reference to the page.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public abstract class AbstractFormComponentContributor implements FormComponentContributor
{
    private String _script = defaultScript();
    
    public AbstractFormComponentContributor()
    {        
    }
    
    // Needed until HIVEMIND-134 fix is available
    public AbstractFormComponentContributor(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }
    
    /**
     * Defines the default JavaScript file used by this contributor. Overriden by most subclasses
     * that use JavaScript.
     */
    protected String defaultScript()
    {
        return null;
    }
    
    public String getScript()
    {
        return _script;
    }
    
    public void setScript(String script)
    {
        _script = script;
    }
    
    /**
     * @see org.apache.tapestry.form.FormComponentContributor#renderContribution(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle, FormComponentContributorContext, org.apache.tapestry.form.IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle, FormComponentContributorContext context, IFormComponent field)
    {
        if (_script != null)
        {
            // TODO:  cycle.getInfrastructure().getClassResolver()
            
            Resource script = new ClasspathResource(cycle.getEngine().getClassResolver(), _script);
            
            TapestryUtils.getPageRenderSupport(cycle, field).addExternalScript(script);
        }
    }
    
    /**
     * Helper method that adds the specified submit handler to to the specified form.
     */
    protected void addSubmitHandler(IForm form, String handler)
    {
        form.addEventHandler(FormEventType.SUBMIT, handler);
    }
}
