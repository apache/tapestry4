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
package org.apache.tapestry.dojo.html;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.DojoUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.json.JSONObject;

/**
 * Implementation of dojo's FloatingPane.
 *
 * @author andyhot
 * @since 4.1
 */
public abstract class TitlePane extends AbstractComponent implements IWidget, IDojoTitlePane 
{
    /** id. */
    public abstract String getIdParameter();

    /** More js options - JSON style. */
    public abstract String getOptions();

    /** Injected script. */
    public abstract IScript getScript();
    
    /**
     * {@inheritDoc}
     */
    public void renderWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderComponent(writer, cycle);
    }    

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("div");
        writer.attribute("id", getIdParameter());
        
        renderInformalParameters(writer, cycle);
        renderBody(writer, cycle);
        
        writer.end();
        
        JSONObject obj = DojoUtils.parseJSONParameter(this, "options");

        obj.put("labelNode", getLabelNode());
        obj.put("labelNodeClass", getLabelNodeClass());
        obj.put("label", getLabel());
        obj.put("containerNodeClass", getContainerNodeClass());      

        //Setup our script includes
        Map scriptParms = new HashMap();
        scriptParms.put("id", getIdParameter());
        scriptParms.put("props", obj.toString());
        
        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
        
        getScript().execute(this, cycle, pageRenderSupport, scriptParms);
    }
}
