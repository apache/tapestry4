// Copyright Oct 16, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.dojo.html;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.AbstractWidget;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.ResponseBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Implementation of dojo Dialog widget.
 * 
 */
public abstract class Dialog extends AbstractWidget
{
    public abstract boolean isHidden();
    public abstract void setHidden(boolean hidden);
    
    public abstract String getBackgroundColor();
    
    public abstract float getOpacity();
    
    public abstract boolean getFollowScroll();
    
    public abstract boolean getCloseOnBackgroundClick();
    
    public abstract int getBlockDuration();
    
    public abstract int getLifeTime();
    
    public abstract String getToggle();
    
    public abstract int getToggleDuration();

    /**
     * If called will execute the client side widget checkSize() function to re-size
     * and re-center the widget.  This is useful in circumstances where you are updating
     * content within the dialog itself.
     */
    public void resize()
    {
        setResizing(true);
    }

    public void show()
    {
        setHidden(false);
    }

    public void hide()
    {
        setHidden(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding())
        {
            writer.begin(getTemplateTagName()); // use element specified
            renderIdAttribute(writer, cycle); // render id="" client id
            renderInformalParameters(writer, cycle);
        }
        
        renderBody(writer, cycle);
        
        if (!cycle.isRewinding())
            writer.end();
        
        if (!cycle.isRewinding())
        {
            JSONObject json = new JSONObject();
            json.put("bgColor", getBackgroundColor());
            json.put("bgOpacity", getOpacity());
            json.put("followScroll", getFollowScroll());
            json.put("closeOnBackgroundClick", getCloseOnBackgroundClick());
            json.put("blockDuration", getBlockDuration());
            json.put("lifeTime", getLifeTime());
            json.put("toggle", getToggle());
            json.put("toggleDuration", getToggleDuration());

            Map parms = new HashMap();
            parms.put("component", this);
            parms.put("props", json.toString());
            
            getScript().execute(this, cycle, TapestryUtils.getPageRenderSupport(cycle, this), parms);

            if (isResizing())
            {
                if (!getResponseBuilder().isInitializationScriptAllowed(this))
                {
                    getResponseBuilder().updateComponent(getId());
                }

                getResponseBuilder().addScriptAfterInitialization(this, "dojo.widget.byId('" + getClientId() + "').checkSize();");
            }
        }
    }

    public abstract ResponseBuilder getResponseBuilder();

    public abstract boolean isResizing();

    public abstract void setResizing(boolean value);

    /** injected. */
    public abstract IScript getScript();
}
