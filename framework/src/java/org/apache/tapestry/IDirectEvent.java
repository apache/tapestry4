// Copyright May 20, 2006 The Apache Software Foundation
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
package org.apache.tapestry;

import org.apache.tapestry.event.BrowserEvent;


/**
 * Interface that defines classes that may be invoked directly by
 * the event service. 
 * 
 * @author jkuhnert
 */
public interface IDirectEvent extends IComponent
{

    /**
     *  Invoked by the event service to have the component peform
     *  the appropriate action. 
     **/
    
    void triggerEvent(IRequestCycle cycle, BrowserEvent event);

    /**
     *  Invoked by the event service to query the component as to
     *  whether it is stateful.  If stateful and no 
     *  {@link javax.servlet.http.HttpSession} is active, then a
     *  {@link org.apache.tapestry.StaleSessionException} is
     *  thrown by the service.
     * 
     **/

    boolean isStateful();
}
