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

package org.apache.tapestry.junit.mock.c14;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 *  Tests the use of the listener and index parameters
 *  of {@link org.apache.tapestry.form.ListEdit}, as well
 *  as using an array (not a list) as a ListEdit source.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class ListEditForm extends BaseComponent
implements PageDetachListener
{
	private static int _syncCount;
	
	public int getSyncCount()
	{
		return _syncCount;
	}
	
	public void synchronizeItem(IRequestCycle cycle)
	{
		_syncCount++;
	}
	
    public void pageDetached(PageEvent event)
    {
    	_syncCount = 0;
    }

}
