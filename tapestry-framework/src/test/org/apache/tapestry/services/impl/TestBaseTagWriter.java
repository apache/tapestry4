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

package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.*;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.BaseTagWriter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestBaseTagWriter extends BaseComponentTestCase
{
    private IMarkupWriter newWriter(String url)
    {
        IMarkupWriter writer = newMock(IMarkupWriter.class);

        writer.beginEmpty("base");
        writer.attribute("href", url);
        writer.printRaw("<!--[if IE]></base><![endif]-->");
        writer.println();

        return writer;
    }

    private INamespace newNamespace(String id)
    {
        INamespace ns = newMock(INamespace.class);
        checkOrder(ns, false);
        
        expect(ns.getId()).andReturn(id);

        return ns;
    }

    private IPage newPage(INamespace ns, String pageName)
    {
        IPage page = newPage();

        expect(page.getNamespace()).andReturn(ns);

        if (pageName != null)
        {
            expect(page.getPageName()).andReturn(pageName);
        }

        return page;
    }

    private IRequestCycle newRequestCycle(IPage page, String url)
    {
        IRequestCycle cycle = newCycle();

        expect(cycle.getPage()).andReturn(page);

        expect(cycle.getAbsoluteURL(url)).andReturn("http://foo.com/context" + url);

        return cycle;
    }

    private void run(IMarkupWriter writer, IRequestCycle cycle)
    {
        replay();
        
        new BaseTagWriter().render(writer, cycle);

        verify();
    }
    
    public void testNotApplicationNamespace()
    {
        INamespace ns = newNamespace("library");
        IPage page = newPage(ns, null);
        IRequestCycle cycle = newRequestCycle(page, "/");
        IMarkupWriter writer = newWriter("http://foo.com/context/");

        run(writer, cycle);
    }
    
    public void testInRoot()
    {
        INamespace ns = newNamespace("library");
        IPage page = newPage(ns, null);
        IRequestCycle cycle = newRequestCycle(page, "/");
        IMarkupWriter writer = newWriter("http://foo.com/context/");

        run(writer, cycle);
    }
    
    public void testInSubFolder()
    {
        INamespace ns = newNamespace(null);
        IPage page = newPage(ns, "admin/AdminMenu");
        IRequestCycle cycle = newRequestCycle(page, "/admin/");
        IMarkupWriter writer = newWriter("http://foo.com/context/admin/");
        
        run(writer, cycle);   
    }
}