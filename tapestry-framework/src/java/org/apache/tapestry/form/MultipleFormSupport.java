// Copyright 2006 The Apache Software Foundation
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

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * A {@link FormSupport} implementation that can work when a form is
 * included multiple times in a given page ( due to it being in a loop
 * or in a component that's included many times in the page).
 * <p/>
 * This is achieved by prefixing the ids of all form elements with the 
 * form's id.
 *
 * @since 4.1.1
 */
public class MultipleFormSupport extends FormSupportImpl
{   
    /** 
     * The prefix to use for the form elements. On render, this is the
     * clientId of the form. On rewind, it's computed from the posted data.
     */
    private String _prefix;
    
    public MultipleFormSupport(IMarkupWriter writer, IRequestCycle cycle, IForm form)
    {
        super(writer, cycle, form);
        _prefix = form.getClientId();
    }
    
    /**
     * Constructs a unique identifier (within the page). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     */

    public String getElementId(IFormComponent component, String baseId)
    {
        return super.getElementId(component, _prefix + ":" + baseId);        
    }
    
    public String rewind()
    {
        findIdPrefix();
        return super.rewind();
    }
    
    private void findIdPrefix()
    {
        String allocatedFormIds = _cycle.getParameter(FORM_IDS);
        int pos = allocatedFormIds.indexOf(':');
        if (pos>=0)
            _prefix = allocatedFormIds.substring(0, pos);
    }    
}
