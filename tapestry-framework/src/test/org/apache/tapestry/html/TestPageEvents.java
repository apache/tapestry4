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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.test.Creator;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPageEvents extends BaseComponentTestCase
{
    private IPage createPage()
    {
        Creator creator = new Creator();

        return (IPage) creator.newInstance(BasePage.class);
    }

    public void testPageDetached()
    {
        IPage page = createPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.detach();

        page.addPageDetachListener(l);

        page.detach();

        assertEquals("pageDetached", l.getMethod());

        l.reset();

        page.removePageDetachListener(l);

        page.detach();

        assertNull(l.getMethod());
    }

    // Cookie-cutter for the remaining listener interfaces

    public void testPageAttached()
    {
        IPage page = createPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.attach(null, null);

        page.addPageAttachListener(l);

        page.attach(null, null);

        assertEquals(l.getMethod(), null);

        page.firePageAttached();

        assertEquals(l.getMethod(), "pageAttached");

        l.reset();

        page.removePageAttachListener(l);

        page.attach(null, null);

        assertNull(l.getMethod());
    }

    public void testPageBeginRender()
    {
        IPage page = createPage();
        ListenerFixture l = new ListenerFixture();
        
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        
        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();
        
        // Code path: no listener list

        page.renderPage(builder, cycle);
        
        verify();

        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();

        page.addPageBeginRenderListener(l);

        page.renderPage(builder, cycle);

        assertEquals("pageBeginRender", l.getMethod());

        l.reset();

        page.removePageBeginRenderListener(l);

        verify();

        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();

        page.renderPage(builder, cycle);

        assertNull(l.getMethod());

        verify();
    }

    public void testPageEndRender()
    {
        IPage page = createPage();
        ListenerFixture l = new ListenerFixture();
        
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        
        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();

        // Code path: no listener list

        page.renderPage(builder, cycle);

        verify();

        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();

        page.addPageEndRenderListener(l);

        page.renderPage(builder, cycle);

        assertEquals("pageEndRender", l.getMethod());

        l.reset();

        page.removePageEndRenderListener(l);

        verify();

        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        builder.render(NullWriter.getSharedInstance(), page, cycle);
        
        replay();

        page.renderPage(builder, cycle);

        assertNull(l.getMethod());

        verify();
    }

    public void testPageValidate()
    {
        IPage page = createPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.validate(null);

        page.addPageValidateListener(l);

        page.validate(null);

        assertEquals("pageValidate", l.getMethod());

        l.reset();

        page.removePageValidateListener(l);

        page.validate(null);

        assertNull(l.getMethod());
    }

}