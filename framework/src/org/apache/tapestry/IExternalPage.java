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
 *  Defines a page which may be referenced externally via a URL using the 
 *  {@link org.apache.tapestry.engine.ExternalService}. External pages may be bookmarked 
 *  via their URL for latter display. See the 
 *  {@link org.apache.tapestry.link.ExternalLink} for details on how to invoke
 *  <tt>IExternalPage</tt>s.
 * 
 *  @see org.apache.tapestry.callback.ExternalCallback
 *  @see org.apache.tapestry.engine.ExternalService
 *
 *  @author Howard Lewis Ship
 *  @author Malcolm Edgar
 *  @version $Id$
 *  @since 2.2
 **/

public interface IExternalPage extends IPage
{
    /**
     *  Initialize the external page with the given array of parameters and
     *  request cycle.
     *  <p>
     *  This method is invoked after {@link IPage#validate(IRequestCycle)}.
     *
     *  @param parameters the array of page parameters
     *  @param cycle current request cycle
     * 
     **/

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle);
}
