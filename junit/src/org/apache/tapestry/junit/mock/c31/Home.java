//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock.c31;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 *  Tests for the Hidden component.
 *
 *  @author Tsvetelin Saykov
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class Home extends BasePage
{
	public abstract String getValue();
	public abstract void setValue(String value);
	
	public abstract String getHiddenID();
	public abstract void setHiddenID(String hiddenID);
	
	public abstract String getValueWithID();
	public abstract void setValueWithID(String valueWithID);

	public abstract String getValueWithEncoding();
	public abstract void setValueWithEncoding(String valueWithID);

	public abstract Boolean getBooleanValue();
	public abstract void setBooleanValue(Boolean value);

	protected void finishLoad()
	{
		setValue("1234567890");
		setHiddenID("0987654321");
		setValueWithID("1234567890-with id");
		setValueWithEncoding("1234567890-with encoding");
		setBooleanValue(Boolean.FALSE);
	}
	
	public void formSubmit(IRequestCycle cycle)
	{
		Two page = (Two)cycle.getPage("Two");
		
		page.setValue(getValue());
		page.setHiddenID(getHiddenID());
		page.setValueWithEncoding(getValueWithEncoding());
		
		cycle.activate(page);
	}
	
	public void hiddenListener(IRequestCycle cycle)
	{
		//just a test for listener
	}

	public boolean getEncode(){
		return false;
	}
}
