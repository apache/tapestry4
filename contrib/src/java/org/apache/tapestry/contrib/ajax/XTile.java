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

package org.apache.tapestry.contrib.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * @author mindbridge
 * @author Paul Green
 * @since 4.0
 */
public abstract class XTile extends BaseComponent implements IXTile
{
    public abstract IActionListener getListener();

    public abstract String getSendName();

    public abstract String getReceiveName();

    public abstract String getErrorName();

    public abstract boolean getDisableCaching();

    // Injected

    public abstract IEngineService getService();

    public void trigger(IRequestCycle cycle)
    {
        IActionListener listener = getListener();

        if (listener == null)
            throw Tapestry.createRequiredParameterException(this, "listener");

        listener.actionTriggered(this, cycle);
    }

    public Map getScriptSymbols()
    {
        ILink link = getService().getLink(false, this);

        Map result = new HashMap();

        result.put("sendFunctionName", getSendName());
        result.put("receiveFunctionName", getReceiveName());
        result.put("errorFunctionName", getErrorName());
        result.put("disableCaching", getDisableCaching() ? "true" : null);
        result.put("url", link.getURL());

        return result;
    }

}
