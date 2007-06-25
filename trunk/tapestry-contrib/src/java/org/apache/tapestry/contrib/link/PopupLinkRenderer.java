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

package org.apache.tapestry.contrib.link;

import org.apache.hivemind.service.BodyBuilder;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.DefaultLinkRenderer;

/**
 * This renderer emits javascript to launch the link in a window.
 * 
 * @author David Solis
 * @since 3.0.1
 */
public class PopupLinkRenderer extends DefaultLinkRenderer
{

    private String _windowName;

    private String _features;

    public PopupLinkRenderer()
    {
    }

    /**
     * Initializes the name and features for javascript window.open function.
     * 
     * @param windowName
     *            the window name
     * @param features
     *            the window features
     */
    public PopupLinkRenderer(String windowName, String features)
    {
        _windowName = windowName;
        _features = features;
    }

    /**
     * @see DefaultLinkRenderer#constructURL(org.apache.tapestry.components.ILinkComponent,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected String constructURL(ILinkComponent component, IRequestCycle cycle)
    {
        if (cycle.isRewinding()) {
            return null;
        }
        
        String anchor = component.getAnchor();
        ILink link = component.getLink(cycle);

        String url = link.getURL(anchor, true);

        PageRenderSupport support = TapestryUtils.getPageRenderSupport(cycle, component);

        String functionName = support.getUniqueString("popup_window");

        BodyBuilder builder = new BodyBuilder();

        builder.addln("{0}=function()", functionName);
        builder.begin();
        builder.add(
                "var newWindow = window.open({0}, {1}, {2});",
                TapestryUtils.enquote(url),
                TapestryUtils.enquote(getWindowName()),
                TapestryUtils.enquote(getFeatures()));
        builder.add("newWindow.focus();");
        builder.end();
        builder.addln(";");

        support.addBodyScript(component, builder.toString());

        return "javascript:" + functionName + "();";
    }

    public String getWindowName()
    {
        return _windowName;
    }

    public void setWindowName(String windowName)
    {
        _windowName = windowName;
    }

    public String getFeatures()
    {
        return _features;
    }

    public void setFeatures(String features)
    {
        _features = features;
    }
}
