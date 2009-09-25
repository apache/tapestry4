// Copyright 2009 The Apache Software Foundation
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
package org.apache.tapestry.contrib.link;

import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.ILinkRenderer;
import org.testng.annotations.Test;

/**
 * Tests {@link RawURLLinkRenderer}.
 * @author Andreas Andreou
 */
public class TestRawURLLinkRenderer extends BaseLinkRendererTestCase {

    @Test
    public void testRenderer() {
        final ILinkRenderer linkRenderer = new RawURLLinkRenderer();
        final ILinkComponent linkComponent = newLinkComponent("my.site", false, null, null,
                "class", "info", "onclick", "alert('hi')");

        assertLinkRenderer(linkRenderer, linkComponent,
                "my.site",
                "");
    }
}