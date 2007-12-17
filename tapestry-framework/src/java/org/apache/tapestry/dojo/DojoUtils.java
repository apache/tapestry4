// Copyright May 16, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo;

import java.text.ParseException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.json.JSONObject;

/**
 * @author andyhot
 * @since 4.1
 */
public final class DojoUtils
{
    /* defeat instantiation */
    private DojoUtils() { }
    
    /**
     * Converts a parameter of an {@link IComponent} to an instance of {@link JSONObject}.
     * 
     * @param component
     * @param parameterName
     * @return The parameter parsed into a json structure.
     */
    public static JSONObject parseJSONParameter(IComponent component, String parameterName)
    {
        IBinding binding = component.getBinding(parameterName);
        if (binding == null || binding.getObject() == null)
            return new JSONObject();
        
        try
        {
            return new JSONObject((String) binding.getObject(String.class));
        }
        catch (ParseException ex)
        {
            throw new ApplicationRuntimeException( DojoMessages.mustUseValidJsonInParameter(parameterName), 
                    binding.getLocation() , ex);
        }
    }
}
