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
 * Event listener for determining when a page is about to start rendering. Rendering includes the
 * replay of a form to handle form submissions.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface PageBeginRenderListener extends EventListener
{
    /**
     * Invoked before just before the page renders a response. This provides listeners with a last
     * chance to initialize themselves for the render. This initialization can include modifying
     * peristent page properties.
     */

    public void pageBeginRender(PageEvent event);
}