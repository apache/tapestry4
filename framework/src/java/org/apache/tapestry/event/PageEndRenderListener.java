// Copyright 2005 The Apache Software Foundation
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
 * Counterpart to {@link org.apache.tapestry.event.PageBeginRenderListener}, for the (typically
 * less-interesting) end of page render event.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PageEndRenderListener extends EventListener
{
    /**
     * Invoked after a successful render of the page. Allows objects to release any resources they
     * needed during the the render.
     * 
     * @see org.apache.tapestry.AbstractComponent#pageEndRender(PageEvent)
     */

    public void pageEndRender(PageEvent event);
}