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

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
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
     * @see DefaultLinkRenderer#constructURL(org.apache.tapestry.engine.ILink, String,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected String constructURL(ILink link, String anchor, IRequestCycle cycle)
    {
        String url = link.getURL(anchor, true);

        PageRenderSupport support = (PageRenderSupport) cycle
                .getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);

        // TODO: Error if no Body!

        String functionName = support.getUniqueString("popup_window");

        BodyBuilder builder = new BodyBuilder();

        builder.addln("function {0}()", functionName);
        builder.begin();
        builder.addln(
                "var newWindow = window.open({0}, {1}, {2});",
                normalizeString(url),
                normalizeString(getWindowName()),
                normalizeString(getFeatures()));
        builder.addln("newWindow.focus();");
        builder.end();

        support.addBodyScript(builder.toString());

        return "javascript:" + functionName + "();";
    }

    private static final String QUOTE = "\"";

    static String normalizeString(String str)
    {
        if (HiveMind.isBlank(str))
            return QUOTE + QUOTE;

        int length = str.length();

        // Doing this char by char is inefficient but Good Enough

        StringBuffer buffer = new StringBuffer(length + 2);

        buffer.append(QUOTE);

        for (int i = 0; i < length; i++)
        {
            char ch = str.charAt(i);

            // Embedded quotes and backslashes are escaped with a backslash.

            if (ch == '"' || ch == '\\')
                buffer.append('\\');

            buffer.append(ch);
        }

        buffer.append(QUOTE);

        return buffer.toString();
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
