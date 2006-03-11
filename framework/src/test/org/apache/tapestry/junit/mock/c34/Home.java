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

package org.apache.tapestry.junit.mock.c34;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.html.BasePage;

/**
 * Used to test the For and If components.
 * 
 * @author Mindbridge
 */
public abstract class Home extends BasePage
{
	public abstract List getRewindedIndexes();
	public abstract List getRewindedValues();
	
	public void setIndex(int index) {
		if (getRequestCycle().isRewinding())
			getRewindedIndexes().add(new Integer(index));
	}
	
	public void setValue(Object value) {
		if (getRequestCycle().isRewinding())
			getRewindedValues().add(value);
	}
	
	public List getItems() {
		List items = new ArrayList();
		items.add(new Item("id1", "value1"));
		items.add(new Item("id2", "value2"));
		return items;
	}
	
	public List getAllItems() {
		List items = new ArrayList();
		items.add(new Item("id1", "value1"));
		items.add(new Item("id2", "value2"));
		items.add(new Item("id3", "value3"));
		items.add(new Item("id4", "value4"));
		items.add(new Item("id5", "value5"));
		return items;
	}
}