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

package org.apache.tapestry.contrib.ajax;

import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.Any;

/**
 * @since 4.1.2
 */
public abstract class AjaxStatus extends Any
{
    public abstract IScript getScript();
    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // render as if an Any component
        super.renderComponent(writer, cycle);
        // then add the script
        if(!cycle.isRewinding()) {
            PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
            Map symbols = new HashMap();
            symbols.put("id", getClientId());
            getScript().execute(this, cycle, pageRenderSupport, symbols);
        }
    }
}
