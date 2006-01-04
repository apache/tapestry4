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

package org.apache.tapestry.workbench;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.contrib.link.PopupLinkRenderer;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.link.ILinkRenderer;

/**
 * Page used to raise a popup window containing the ErrorFest page, which has errors.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class ExceptionTab extends BasePage
{
    @Bean(PopupLinkRenderer.class)
    public abstract ILinkRenderer getLinkRenderer();

    @Component(type = "PageLink", bindings =
    { "page=literal:ErrorFest", "renderer=bean:linkRenderer" })
    public abstract IComponent getLink();
}
