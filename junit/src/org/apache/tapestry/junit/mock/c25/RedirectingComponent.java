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

package org.apache.tapestry.junit.mock.c25;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

/**
 * @author teo
 */
public class RedirectingComponent extends BaseComponent implements PageValidateListener
{

	/**
	 * @see org.apache.tapestry.event.PageValidateListener#pageValidate(org.apache.tapestry.event.PageEvent)
	 */
	public void pageValidate(PageEvent event)
	{
        String pageName = getBinding("page").getString();
        throw new PageRedirectException(pageName);
	}

}
