// Copyright 2004, 2008 The Apache Software Foundation
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

package org.apache.tapestry.workbench.ajax;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.annotations.EventListener;

/**
 * Page-class demonstrating ajaxy RadioGroups
 * 
 */
public abstract class AsyncForm extends BasePage
{
    public abstract String getChoice();

    @EventListener( targets = "choose", events = "onChange")
    public void onRadioButtonPressed( BrowserEvent b )
    {
        String msg = "You've chosen option " + b.getMethodArguments().get(0) +" -> " + getChoice();
        System.out.println( msg );

        getRequestCycle().getResponseBuilder().updateComponent("ajaxResponse");
    }
}
