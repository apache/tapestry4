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
package org.apache.tapestry.dojo.html;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;


/**
 * This component that can be used <em>instead of</em> the {@link Shell}, mainly for 
 * situations where you either don't use the {@link Shell} component or can't because
 * you are writing portlets.
 *
 * @author jkuhnert
 */
public abstract class ScriptIncludes extends AbstractComponent
{
    public abstract IRender getAjaxDelegate();
    
    public abstract IRender getDelegate();
    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;
        
        IRender delegate = getDelegate();
        
        if (delegate != null)
            delegate.render(writer, cycle);
        
        IRender ajaxDelegate = getAjaxDelegate();
        
        if (ajaxDelegate != null)
            ajaxDelegate.render(writer, cycle);
    }
}
