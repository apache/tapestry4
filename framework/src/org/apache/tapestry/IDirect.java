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

package org.apache.tapestry;

/**
 *  Interface that defines classes that may be messaged by the direct
 *  service.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IDirect extends IComponent
{
    /**
     *  Invoked by the direct service to have the component peform
     *  the appropriate action.  The {@link org.apache.tapestry.link.DirectLink} component will
     *  notify its listener.
     *
     **/

    public void trigger(IRequestCycle cycle);

    /**
     *  Invoked by the direct service to query the component as to
     *  whether it is stateful.  If stateful and no 
     *  {@link javax.servlet.http.HttpSession} is active, then a
     *  {@link org.apache.tapestry.StaleSessionException} is
     *  thrown by the service.
     * 
     *  @since 2.3
     * 
     **/

    public boolean isStateful();

}