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

package org.apache.tapestry.link;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;

/**
 * Tests for {@link org.apache.tapestry.link.DefaultLinkRenderer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultLinkRendererTest extends BaseComponentTestCase
{
    class RendererFixture extends DefaultLinkRenderer
    {
        private IMarkupWriter _writer;

        private IRequestCycle _cycle;

        private ILinkComponent _component;

        private String _element;

        private boolean _hasBody;

        private String _targetAttribute;

        private String _urlAttribute;

        public RendererFixture(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent component,
                String element, boolean body, String targetAttribute, String urlAttribute)
        {
            _writer = writer;
            _cycle = cycle;
            _component = component;
            _element = element;
            _hasBody = body;
            _targetAttribute = targetAttribute;
            _urlAttribute = urlAttribute;
        }

        @Override
        protected void afterBodyRender(IMarkupWriter writer, IRequestCycle cycle,
                ILinkComponent link)
        {
            assertSame(_writer, writer);
            assertSame(_cycle, cycle);
            assertSame(_component, link);

            writer.print("AFTER-BODY-RENDER");
        }

        @Override
        protected void beforeBodyRender(IMarkupWriter writer, IRequestCycle cycle,
                ILinkComponent link)
        {
            assertSame(_writer, writer);
            assertSame(_cycle, cycle);
            assertSame(_component, link);

            writer.print("BEFORE-BODY-RENDER");
        }

        @Override
        protected String getElement()
        {
            return _element;
        }

        @Override
        protected boolean getHasBody()
        {
            // TODO Auto-generated method stub
            return _hasBody;
        }

        @Override
        protected String getTargetAttribute()
        {

            return _targetAttribute;
        }

        @Override
        protected String getUrlAttribute()
        {
            return _urlAttribute;
        }

    }

    protected ILinkComponent newComponent()
    {
        return (ILinkComponent) newMock(ILinkComponent.class);
    }

    public void testNoNesting()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent existing = newComponent();
        ILinkComponent active = newComponent();
        Location l = newLocation();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, existing);

        trainGetLocation(active, l);

        replay();

        try
        {
            new DefaultLinkRenderer().renderLink(writer, cycle, active);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(LinkMessages.noNesting(), ex.getMessage());
            assertSame(active, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testStandardNotDisabled()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();
        ILink link = newLink();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, false);
        trainIsRewinding(cycle, false);

        writer.begin("a");

        trainGetLink(component, cycle, link);

        trainGetScheme(component, null);
        trainGetPort(component, null);
        trainGetAnchor(component, null);

        trainGetURL(link, null, null, "/foo/bar.baz");

        writer.attribute("href", "/foo/bar.baz");

        trainGetTarget(component, null);

        trainGetNestedWriter(writer, nested);

        component.renderBody(nested, cycle);

        component.renderAdditionalAttributes(writer, cycle);

        nested.close();

        writer.end();

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new DefaultLinkRenderer().renderLink(writer, cycle, component);

        verify();
    }

    protected void trainGetScheme(ILinkComponent component, String scheme)
    {
        expect(component.getScheme()).andReturn(scheme);
    }
    
    protected void trainGetPort(ILinkComponent component, Integer port)
    {
        expect(component.getPort()).andReturn(port);
    }
    
    public void testStandardWithSchemaAnchorAndTarget()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();
        ILink link = newLink();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, false);
        trainIsRewinding(cycle, false);

        writer.begin("a");

        trainGetLink(component, cycle, link);

        trainGetScheme(component, "https");

        trainGetPort(component, null);
        
        trainGetAnchor(component, "my-anchor");

        trainGetURL(link, "https", "my-anchor", "http://zap.com/foo/bar.baz#my-anchor");

        writer.attribute("href", "http://zap.com/foo/bar.baz#my-anchor");

        trainGetTarget(component, "some-target");

        writer.attribute("target", "some-target");

        trainGetNestedWriter(writer, nested);

        component.renderBody(nested, cycle);

        component.renderAdditionalAttributes(writer, cycle);
        
        nested.close();
        
        writer.end();
        
        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new DefaultLinkRenderer().renderLink(writer, cycle, component);

        verify();
    }

    public void testDisabled()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, true);

        component.renderBody(writer, cycle);

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new DefaultLinkRenderer().renderLink(writer, cycle, component);

        verify();
    }

    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, false);
        trainIsRewinding(cycle, true);

        component.renderBody(writer, cycle);

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new DefaultLinkRenderer().renderLink(writer, cycle, component);

        verify();
    }

    public void testWithSubclass()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();
        ILink link = newLink();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, false);
        trainIsRewinding(cycle, false);

        writer.begin("xlink");

        trainGetLink(component, cycle, link);

        trainGetScheme(component, null);

        trainGetPort(component, null);
        
        trainGetAnchor(component, "my-anchor");

        trainGetURL(link, null, "my-anchor", "/foo/bar.baz#my-anchor");

        writer.attribute("xurl", "/foo/bar.baz#my-anchor");

        trainGetTarget(component, "some-target");

        writer.attribute("xtarget", "some-target");

        writer.print("BEFORE-BODY-RENDER");

        trainGetNestedWriter(writer, nested);

        component.renderBody(nested, cycle);

        writer.print("AFTER-BODY-RENDER");

        component.renderAdditionalAttributes(writer, cycle);

        nested.close();

        writer.end();

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new RendererFixture(writer, cycle, component, "xlink", true, "xtarget", "xurl").renderLink(
                writer,
                cycle,
                component);

        verify();
    }

    public void testWithSubclassNoBody()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();

        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();
        ILink link = newLink();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, false);
        trainIsRewinding(cycle, false);

        writer.beginEmpty("xlink");

        trainGetLink(component, cycle, link);

        trainGetScheme(component, null);

        trainGetPort(component, null);
        
        trainGetAnchor(component, "my-anchor");

        trainGetURL(link, null, "my-anchor", "/foo/bar.baz#my-anchor");

        writer.attribute("xurl", "/foo/bar.baz#my-anchor");

        trainGetTarget(component, "some-target");

        writer.attribute("xtarget", "some-target");

        writer.print("BEFORE-BODY-RENDER");

        trainGetNestedWriter(writer, nested);

        writer.print("AFTER-BODY-RENDER");

        writer.closeTag();

        component.renderAdditionalAttributes(writer, cycle);

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new RendererFixture(writer, cycle, component, "xlink", false, "xtarget", "xurl")
                .renderLink(writer, cycle, component);

        verify();
    }

    public void testWithSubclassDisabled()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, true);

        component.renderBody(writer, cycle);

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new RendererFixture(writer, cycle, component, "xlink", true, "xtarget", "xurl").renderLink(
                writer,
                cycle,
                component);

        verify();
    }

    public void testWithSubclassDisabledNoBody()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        ILinkComponent component = newComponent();

        trainGetAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null);
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        trainIsDisabled(component, true);

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        replay();

        new RendererFixture(writer, cycle, component, "xlink", false, "xtarget", "xurl")
                .renderLink(writer, cycle, component);

        verify();
    }

    protected void trainGetAnchor(ILinkComponent component, String anchor)
    {
        expect(component.getAnchor()).andReturn(anchor);
    }

    protected void trainGetTarget(ILinkComponent component, String target)
    {
        expect(component.getTarget()).andReturn(target);
    }

    protected void trainIsDisabled(ILinkComponent component, boolean isDisabled)
    {
        expect(component.isDisabled()).andReturn(isDisabled);
    }
}
