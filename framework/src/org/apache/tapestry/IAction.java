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
 *  A particular type of component usuable with the
 *  action service.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.1
 */

public interface IAction extends IComponent
{
    /**
     *  Returns true if the component requires 
     *  an existing, not new, {@link javax.servlet.http.HttpSession}
     *  to operate.  Components who are not dependant on page state
     *  (or the visit object) are non-stateful and can return false.
     *
     **/

    public boolean getRequiresSession();
}