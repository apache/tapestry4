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

package org.apache.tapestry.javascript;

import java.util.List;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IPage;

/**
 * Outputs the main js packages and the tapestry js 
 * that are defined by the {@link JavascriptManager} service.
 */
public class SimpleAjaxShellDelegate implements IRender {

    private static final String SYSTEM_NEWLINE= (String)java.security.AccessController.doPrivileged(
      new sun.security.action.GetPropertyAction("line.separator"));

    private JavascriptManager _javascriptManager;

    public SimpleAjaxShellDelegate(JavascriptManager javascriptManager) {
        _javascriptManager = javascriptManager;
    }

    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        IPage page = cycle.getPage();
        StringBuffer str = new StringBuffer();
        // include all the main js packages
        appendAssetsAsJavascript(str, _javascriptManager.getJsAssets());
        if (page.hasFormComponents())
        {
            appendAssetsAsJavascript(str, _javascriptManager.getJsFormAssets());
        }
        if (page.hasWidgets())
        {
            appendAssetsAsJavascript(str, _javascriptManager.getJsWidgetAssets());
        }
        // include the tapestry js
        IAsset tapestryAsset = _javascriptManager.getJsTapestryAsset();
        if (tapestryAsset!=null)
        {
            str.append("<script type=\"text/javascript\" src=\"")
                .append(tapestryAsset.buildURL()).append("\"></script>").append(SYSTEM_NEWLINE);
        }

        writer.printRaw(str.toString());
        writer.println();
    }

    private void appendAssetsAsJavascript(StringBuffer str, List jsAssets) {
        for (int i = 0; i < jsAssets.size(); i++)
        {
            IAsset asset = (IAsset) jsAssets.get(i);
            str.append("<script type=\"text/javascript\" src=\"")
                .append(asset.buildURL()).append("\"></script>").append(SYSTEM_NEWLINE);
        }
    }
}
