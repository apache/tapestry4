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

package org.apache.tapestry.event;

import java.util.EventListener;

/**
 *  An interface for objects that want to take part in the validation of the page.
 *
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 **/

public interface PageValidateListener extends EventListener
{
    /**
     *  Invoked by the page from its
     *  {@link org.apache.tapestry.IPage#validate(org.apache.tapestry.IRequestCycle)} method.
     *
     *  <p>May throw a {@link org.apache.tapestry.PageRedirectException}, to redirect the user
     *  to an appropriate part of the system (such as, a login page).
     **/

    public void pageValidate(PageEvent event);
}
