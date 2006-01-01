// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import javax.servlet.http.HttpSession;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * @author mb
 * @since 4.0
 */
public abstract class Timeout extends BaseComponent {
	public abstract int getWarningTime();
	public abstract int getAutoProlongTime();
	
	public abstract String getWarningMessage();
	public abstract String getExpirationMessage();

	public abstract boolean getDisableWarning();
	public abstract boolean getDisableAutoProlong();
	
	public abstract String getExpirationFunction();
	
	protected HttpSession getSession()
	{
		return getPage().getRequestCycle().getRequestContext().getSession();
	}
	
	protected int getSessionTime()
	{
		return getSession().getMaxInactiveInterval();
	}
	
	public boolean isInSession()
	{
		HttpSession session = getSession();
		return session != null;
	}
	
	public Map getScriptSymbols()
	{
		int nSessionTime = getSessionTime();
		int nTimeToMessage = nSessionTime - getWarningTime();
		if (nTimeToMessage < 0)
			nTimeToMessage = 0;
		int nRemainingTime = nSessionTime - nTimeToMessage;
		int nAutoProlongTime = nSessionTime - getAutoProlongTime();
		
		Map mapSymbols = new HashMap();
		mapSymbols.put("confirmTimeout", new Integer(nTimeToMessage*1000));
		mapSymbols.put("expirationTimeout", new Integer(nRemainingTime*1000));
		mapSymbols.put("prolongSessionPeriod", new Integer(nAutoProlongTime*1000));
		mapSymbols.put("confirmMessage", getWarningMessage());
		mapSymbols.put("expirationMessage", getExpirationMessage());
		mapSymbols.put("disableWarning", new Boolean(getDisableWarning()));
		mapSymbols.put("disableAutoProlong", new Boolean(getDisableAutoProlong()));
		mapSymbols.put("expirationFunction", getExpirationFunction());
		return mapSymbols;
	}
	
	public void renewSession(IRequestCycle cycle)
	{
		// calling this method via the XTile service will automatically renew the session
		// System.out.println("Prolonging session...");
	}
}
