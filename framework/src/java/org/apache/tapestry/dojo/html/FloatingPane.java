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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
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
public abstract class FloatingPane extends BaseComponent implements IWidget {

    /** Title of the FloatingPane. */
    public abstract String getTitle();

    /** Min height of the FloatingPane. */
    public abstract int getMinWidth();

    /** Min width of the FloatingPane. */
    public abstract int getMinHeight();

    /** Has Shadow. */
    public abstract boolean getHasShadow();

    /** Has Tool bar. */
    public abstract boolean getHasToolbar();

    /** Allow resize. */
    public abstract boolean isResizable();

    /** Should persist position (with cookies). */
    public abstract boolean getPersistPosition();

    /** Should contrain to container. */
    public abstract boolean getConstrainToContainer();

    /** style. */
    public abstract String getStyle();

    /** id. */
    public abstract String getIdParameter();

    /** The taskBar this pane is connected to. */
    public abstract Object getTaskBar();

    /** Is this pane a taskBar? */
    public abstract boolean getIsTaskBar();

    /** More js options - JSON style. */
    public abstract String getOptions();

    /** Injected script. */
    public abstract IScript getScript();
    
    /** Get the id of the connected taskBar. */
    public String getTaskBarId()
    {
        Object obj = getTaskBar();
        if (obj == null)
            return null;
        else if (obj instanceof String)
            return (String)obj;
        else if (obj instanceof FloatingPane)
            return ((FloatingPane)obj).getIdParameter();
        else
            throw new ApplicationRuntimeException("Parameter taskBar should either be a String or a FloatingPane");
    }
    
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
        super.renderComponent(writer, cycle);
        
        JSONObject obj = DojoUtils.parseJSONParameter(this, "options");

        obj.put("title", getTitle());
        obj.put("widgetId", getId());
        obj.put("toggle", "fade");
        obj.put("constrainToContainer", getConstrainToContainer());
        obj.put("displayMaximizeAction", true);
        obj.put("displayMinimizeAction", true);
        obj.put("displayCloseAction", true);
        obj.put("hasShadow", getHasShadow());
        obj.put("resizable", isResizable());
        obj.put("taskBarId", getTaskBarId());
        //obj.put("contentNodeId", getId() + "ContentId");
        //obj.put("persistenceWidgetPosition", getPersistPosition());        

        //Setup our script includes
        Map scriptParms = new HashMap();
        scriptParms.put("id", getIdParameter());
        scriptParms.put("props", obj.toString());
        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(cycle, pageRenderSupport, scriptParms);
    }
}
