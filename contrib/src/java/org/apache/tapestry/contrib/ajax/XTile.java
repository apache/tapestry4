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
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;

/**
 * @author mindbridge
 * @author Paul Green
 * @since 4.0
 */
public abstract class XTile extends BaseComponent implements IXTile
{
    public abstract LinkFactory getLinkFactory();
    
    public abstract IActionListener getListener();
    public abstract String getSendName();
    public abstract String getReceiveName();
    public abstract String getErrorName();
    public abstract boolean getDisableCaching();

	public void trigger(IRequestCycle cycle) {
        IActionListener listener = getListener();

        if (listener == null)
        	throw Tapestry.createRequiredParameterException(this, "listener");

        listener.actionTriggered(this, cycle);
	}
	
	public Map getScriptSymbols()
	{
		Map ret = new HashMap();
		ret.put("sendFunctionName", getSendName());
		ret.put("receiveFunctionName", getReceiveName());
		ret.put("errorFunctionName", getErrorName());
		ret.put("disableCaching", getDisableCaching() ? "true" : null);

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, XTileService.SERVICE_NAME);
        parameters.put(ServiceConstants.PAGE, getPage().getPageName());
        parameters.put(ServiceConstants.COMPONENT, getIdPath());
		
		ILink link = getLinkFactory().constructLink(false, 
				parameters, false);

		ret.put("url", link.getURL());
		
		return ret;
	}

}
