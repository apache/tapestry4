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

package org.apache.tapestry.form;

import static org.easymock.EasyMock.expect;

import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.valid.IValidationDelegate;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.LinkSubmit}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class LinkSubmitTest extends BaseComponentTestCase
{
    private class ScriptFixture implements IScript
    {

        public void execute(IRequestCycle cycle, IScriptProcessor processor, Map symbols)
        {
            assertNotNull(cycle);
            assertNotNull(processor);
            assertNotNull(symbols);

            symbols.put("href", "HREF");
        }

        public void execute(IComponent target, IRequestCycle cycle, IScriptProcessor processor, Map symbols)
        {
            assertNotNull(cycle);
            assertNotNull(processor);
            assertNotNull(symbols);

            symbols.put("href", "HREF");
        }
        
        public Resource getScriptResource()
        {
            return null;
        }

    }

    public void testRenderNormal()
    {
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();
        IScript script = new ScriptFixture();
        PageRenderSupport support = newPageRenderSupport();

        IForm form = newForm();

        LinkSubmit linkSubmit = newInstance(LinkSubmit.class, new Object[]
        { "form", form, "name", "fred_1", "script", script, 
            "id", "fred_id", "clientId", "fred_1", "submitType", "submit" });
        
        linkSubmit.addBody(newBody());

        trainGetSupport(cycle, support);
        
        trainResponseBuilder(cycle, writer);
        
        replay();

        linkSubmit.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<a href=\"HREF\" id=\"fred_1\">BODY</a>");
    }

    public void testRenderDisabled()
    {
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        IForm form = newForm();

        LinkSubmit linkSubmit = newInstance(LinkSubmit.class, new Object[]
        { "disabled", Boolean.TRUE, "form", form, "name", "fred_1", "idParameter", "fred_id" });
        linkSubmit.addBody(newBody());

        trainResponseBuilder(cycle, writer);
        
        replay();

        linkSubmit.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("BODY");
    }

    public void testPrepareNormal()
    {
        IRequestCycle cycle = newCycle();

        trainGetAttribute(cycle, LinkSubmit.ATTRIBUTE_NAME, null);

        LinkSubmit linkSubmit = (LinkSubmit) newInstance(LinkSubmit.class);

        cycle.setAttribute(LinkSubmit.ATTRIBUTE_NAME, linkSubmit);

        replay();

        linkSubmit.prepareForRender(cycle);

        verify();
    }

    public void testPrepareConflict()
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage("MyPage");
        Location bloc = newLocation();
        Location floc = newLocation();
        IComponent existing = newComponent("MyPage/barney", bloc);

        trainGetAttribute(cycle, LinkSubmit.ATTRIBUTE_NAME, existing);

        trainGetIdPath(page, null);

        LinkSubmit linkSubmit = newInstance(LinkSubmit.class, new Object[]
        { "id", "fred", "page", page, "container", page, "location", floc });

        replay();

        try
        {
            linkSubmit.prepareForRender(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assert ex.getMessage()
            .indexOf("LinkSubmit MyPage/fred may not be enclosed by another LinkSubmit ") > -1;
            
            assertSame(linkSubmit, ex.getComponent());
            assertSame(floc, ex.getLocation());
        }

        verify();
    }

    public void testCleanupAfterRender()
    {
        IRequestCycle cycle = newCycle();

        cycle.removeAttribute(LinkSubmit.ATTRIBUTE_NAME);

        replay();

        LinkSubmit linkSubmit = (LinkSubmit) newInstance(LinkSubmit.class);

        linkSubmit.cleanupAfterRender(cycle);

        verify();
    }

    public void testIsClicked()
    {
        IRequestCycle cycle = newCycle();

        trainGetParameter(cycle, FormConstants.SUBMIT_NAME_PARAMETER, "fred");

        replay();

        LinkSubmit linkSubmit = (LinkSubmit) newInstance(LinkSubmit.class);

        assertEquals(true, linkSubmit.isClicked(cycle, "fred"));

        verify();
    }

    public void testIsNotClicked()
    {
        IRequestCycle cycle = newCycle();

        trainGetParameter(cycle, FormConstants.SUBMIT_NAME_PARAMETER, null);

        replay();

        LinkSubmit linkSubmit = (LinkSubmit) newInstance(LinkSubmit.class);

        assertEquals(false, linkSubmit.isClicked(cycle, "fred"));

        verify();
    }

    public void testRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IRender body = newRender();
        IForm form = newForm();
        IValidationDelegate delegate = newDelegate();

        LinkSubmit linkSubmit = newInstance(LinkSubmit.class, new Object[]
        { "name", "fred", "form", form });
        linkSubmit.addBody(body);

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, linkSubmit, false);

        trainGetDelegate(form, delegate);

        delegate.setFormComponent(linkSubmit);
        
        trainGetElementId(form, linkSubmit, "fred");
        
        trainIsRewinding(form, true);
        
        // Finally, code inside LinkSubmit ...

        trainGetParameter(cycle, FormConstants.SUBMIT_NAME_PARAMETER, null);

        // ... and back to AbstractFormComponent
        
        trainResponseBuilder(cycle, writer);
        
        body.render(writer, cycle);

        replay();

        linkSubmit.renderComponent(writer, cycle);

        verify();
    }

    private void trainIsRewinding(IForm form, boolean isRewindind)
    {
        expect(form.isRewinding()).andReturn(isRewindind);
    }

    protected void trainGetElementId(IForm form, IFormComponent field, String name)
    {
        expect(form.getElementId(field)).andReturn(name);
    }

    protected void trainGetDelegate(IForm form, IValidationDelegate delegate)
    {
        expect(form.getDelegate()).andReturn(delegate);
    }

    protected void trainWasPrerendered(IForm form, IMarkupWriter writer, IFormComponent field,
            boolean wasPrendered)
    {
        expect(form.wasPrerendered(writer, field)).andReturn(wasPrendered);
    }

    protected void trainGetForm(IRequestCycle cycle, IForm form)
    {
        trainGetAttribute(cycle, TapestryUtils.FORM_ATTRIBUTE, form);
    }

    protected IValidationDelegate newDelegate()
    {
        return newMock(IValidationDelegate.class);
    }

}
