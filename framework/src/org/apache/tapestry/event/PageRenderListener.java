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
 *  An object that listens to page events.  The {@link org.apache.tapestry.IPage page} generates
 *  events before and after rendering a response.  These events also occur before and
 *  after a form rewinds.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public interface PageRenderListener extends EventListener
{
    /**
     *  Invoked before just before the page renders a response.  This provides
     *  listeners with a last chance to initialize themselves for the render.
     *  This initialization can include modifying peristent page properties.
     *
     *
     **/

    public void pageBeginRender(PageEvent event);

    /**
     *  Invoked after a successful render of the page.
     *  Allows objects to release any resources they needed during the
     *  the render.
     * 
     *  @see org.apache.tapestry.AbstractComponent#pageEndRender(PageEvent)
     *
     **/

    public void pageEndRender(PageEvent event);
}