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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.AbstractWidget;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.json.JSONObject;


/**
 * Wraps a dojo InlineEditBox widget. 
 * 
 * <p>
 * Manages a single string value that when hovered over can be edited "inline" in the document
 * wherever it is referenced. Supports various modes of operation (ie disable/enabled), as well as 
 * textarea or single line style edits.
 * </p>
 *
 *
 * <p>
 * Some of the commonly used widget functions to listen to are:<br/>
 * <ul>
 *  <li><b>onSave - </b>When the save button is clicked. Default function listened to when updating
 *  server side managed value.
 *  </li>
 *  <li><b>onUndo - </b>When cancel button is clicked.</li>
 *  <li><b>onMouseOver - </b>Mouse moved over editable region.</li>
 *  <li><b>onMouseOut - </b>Mouse moved away from editable region.</li>
 * </ul>
 * </p>
 * 
 * @author Jesse Kuhnert
 */
public abstract class InlineEditBox extends AbstractWidget implements IDirect
{
    /** 
     * Default single line editing text mode. Use as one of two possible
     * parameters to the <code>mode</code> parameter.
     */
    public static final String TEXT_MODE = "text";
    
    /** 
     * Multi line editing text mode. Use as one of two possible
     * parameters to the <code>mode</code> parameter.
     */
    public static final String TEXT_AREA_MODE = "textarea";
    
    public abstract String getValue();
    public abstract void setValue(String value);
    
    public abstract String getMode();
    
    public abstract int getMinWidth();
    
    public abstract int getMinHeight();
    
    public abstract boolean getDoFade();
    
    public abstract boolean isDiabled();
    
    /**
     * {@inheritDoc}
     */
    public void renderWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding()) {
            
            writer.begin(getTemplateTagName()); // use whatever template tag they specified
            
            renderInformalParameters(writer, cycle);
            
            renderIdAttribute(writer, cycle);
        }
        
        renderBody(writer, cycle);
        
        if (!cycle.isRewinding()) {
            
            writer.end();
        }
        
        if(!TEXT_MODE.equals(getMode())
                && !TEXT_AREA_MODE.equals(getMode())) {
            throw new ApplicationRuntimeException(WidgetMessages.invalidTextMode(getMode()));
        }
        
        if (cycle.isRewinding()) {
            return;
        }
        
        JSONObject prop = new JSONObject();
        prop.put("widgetId", getClientId());
        prop.put("value", getValue());
        prop.put("mode", getMode());
        prop.put("minWidth", getMinWidth());
        prop.put("minHeight", getMinHeight());
        prop.put("doFade", getDoFade());
        
        Map parms = new HashMap();
        parms.put("component", this);
        parms.put("props", prop.toString());
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(this, cycle, prs, parms);
    }
    
    /**
     * Callback url used by client side widget to update server component.
     */
    public String getUpdateUrl()
    {
        DirectServiceParameter dsp =
            new DirectServiceParameter(this);
        
        return getEngine().getLink(true, dsp).getURL();
    }
    
    /**
     * {@inheritDoc}
     */
    public List getUpdateComponents()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAsync()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isJson()
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void trigger(IRequestCycle cycle)
    {
        String newValue = cycle.getParameter(getClientId());
        
        setValue(newValue);
    }
    
    /** Injected. */
    public abstract IEngineService getEngine();
    
    /** Injected. */
    public abstract IScript getScript();
}
