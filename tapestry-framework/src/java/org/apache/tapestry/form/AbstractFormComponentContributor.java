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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;

/**
 * Abstract {@link FormComponentContributor} implementation that adds an optional static javscript
 * method reference to the page.
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
    public String defaultScript()
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
     * @see org.apache.tapestry.form.FormComponentContributor#renderContribution(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle, FormComponentContributorContext,
     *      org.apache.tapestry.form.IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        if (_script != null)
            context.includeClasspathScript(_script);
    }
    
    /**
     * Utility used to append onto an existing property represented as an
     * object array. 
     * @param profile
     * @param key
     * @param value
     */
    public void accumulateProperty(JSONObject profile, String key, Object value)
    {
        if (!profile.has(key))
            profile.put(key, new JSONArray());
        
        profile.accumulate(key, value);
    }
    
    /**
     * Utility method to store a field specific profile property which can later
     * be used by client side validation. 
     * 
     * @param field
     *          The field to store the property for, will key off of {@link IFormComponent#getClientId()}.
     * @param profile
     *          The profile for the form.
     * @param key
     *          The property key to store.
     * @param property
     *          The property to store.
     */
    public void setProfileProperty(IFormComponent field, JSONObject profile, 
            String key, Object property)
    {
        if (!profile.has(field.getClientId())) 
            profile.put(field.getClientId(), new JSONObject());
        
        JSONObject fieldProps = profile.getJSONObject(field.getClientId());
        fieldProps.put(key, property);
    }
}
