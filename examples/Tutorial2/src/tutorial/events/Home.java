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

package tutorial.events;

import java.text.DateFormat;
import java.util.Date;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;

/**
 * Example code for the event handling section of the tutorial
 * @author neil clayton
 */
public class Home extends BasePage {
	/**
	 * Called by both the "direct" and "action" components
	 */
	public void timeListener(IRequestCycle cycle) throws RequestCycleException {
		System.err.println("TIME LISTENER METHOD CALLED");
        pageTime = DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getPageTime() {
        return pageTime;
    }

    /**
     * @see net.sf.tapestry.AbstractPage#detach()
     */
    public void detach() {
        pageTime = null;
    }

    private String pageTime;
}
