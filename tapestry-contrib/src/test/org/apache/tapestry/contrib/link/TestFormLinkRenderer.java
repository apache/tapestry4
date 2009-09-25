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
import org.apache.tapestry.engine.ILink;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;

/**
 * Tests {@link FormLinkRenderer}.
 * @author Andreas Andreou
 */
public class TestFormLinkRenderer extends BaseLinkRendererTestCase {

    @Test
    public void testRenderer() {
        final ILinkRenderer linkRenderer = new FormLinkRenderer();
        final ILinkComponent linkComponent = newLinkComponent("my.site", false, null, null,
                "class", "info", "onclick", "alert('hi')");

        final ILink link = linkComponent.getLink(null);
        expect(link.getParameterNames()).andReturn(new String[]{"article", "format"});
        expect(link.getParameterValues("article")).andReturn(new String[]{"12"});
        expect(link.getParameterValues("format")).andReturn(new String[]{"rss"});

        assertLinkRenderer(linkRenderer, linkComponent,
                "<a href=\"javascript: document.LinkForm.submit();\" onclick=\"alert('hi')\" class=\"info\"></a>",
                "function prepareLinkForm() {\n" +
                "  var html = \"\";\n" +
                "  html += \"<div style='position: absolute'>\";\n" +
                "  html += \"<form name='LinkForm' method='post' action='my.site'>\";\n" +
                "  html += \"<input type='hidden' name='article' value='12'/>\";\n" +
                "  html += \"<input type='hidden' name='format' value='rss'/>\";\n" +
                "  html += \"<\" + \"/form>\";\n" +
                "  html += \"<\" + \"/div>\";\n" +
                "  document.write(html);\n" +
                "}\n" +
                "prepareLinkForm();\n\n");
    }
}