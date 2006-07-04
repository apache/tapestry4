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

package org.apache.tapestry.form.validator;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.TranslatedField;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;

/**
 * Abstract implementation of {@link org.apache.tapestry.form.validator.Validator}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */

public abstract class BaseValidator implements Validator
{
    private String _message;

    public BaseValidator()
    {
    }

    public BaseValidator(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }
    
    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
    }

    /**
     * Returns false.
     */

    public boolean getAcceptsNull()
    {
        return false;
    }

    /**
     * Does nothing.
     */

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
    }

    /**
     * Returns false. Subclasses may override.
     */

    public boolean isRequired()
    {
        return false;
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
     * Used to grab the corresponding {@link Translator} for 
     * the field, if one exists.
     * @param field
     * @return The translator, or null if the required translator type 
     *          doesn't exist.
     */
    public Translator getFieldTranslator(IFormComponent field, Class clazz)
    {
        if (TranslatedField.class.isAssignableFrom(field.getClass())) {
            Translator trans = ((TranslatedField)field).getTranslator();
            if (clazz.isInstance(trans)) {
                return trans;
            }
        }
        
        return null;
    }
}
