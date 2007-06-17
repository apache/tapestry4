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

package org.apache.tapestry;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.TapestryUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TapestryUtilsTest extends BaseComponentTestCase
{

    private IRequestCycle newCycle(String key, Object attribute)
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);

        trainGetAttribute(cycle, key, attribute);

        return cycle;
    }

    public void testStoreUniqueAttributeSuccess()
    {
        Object newInstance = new Object();
        IRequestCycle cycle = newCycle();

        String key = "foo.bar.Baz";

        expect(cycle.getAttribute(key)).andReturn(null);

        cycle.setAttribute(key, newInstance);

        replay();

        TapestryUtils.storeUniqueAttribute(cycle, key, newInstance);

        verify();
    }

    public void testStoreUniqueAttributeFailure()
    {
        Object existing = "*EXISTING*";
        Object newInstance = "*NEW*";

        String key = "foo.bar.Baz";

        IRequestCycle cycle = newCycle(key, existing);

        replay();

        try
        {
            TapestryUtils.storeUniqueAttribute(cycle, key, newInstance);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(TapestryMessages.nonUniqueAttribute(newInstance, key, existing), ex
                    .getMessage());
        }

        verify();
    }

    public void testGetPageRenderSupportSuccess()
    {
        IComponent component = newComponent();
        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, support);

        replay();

        PageRenderSupport actual = TapestryUtils.getPageRenderSupport(cycle, component);

        assertSame(support, actual);

        verify();
    }

    public void testRemovePageRenderSupport()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);

        cycle.removeAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);

        replay();

        TapestryUtils.removePageRenderSupport(cycle);

        verify();
    }

    public void testRemoveForm()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);

        cycle.removeAttribute(TapestryUtils.FORM_ATTRIBUTE);

        replay();

        TapestryUtils.removeForm(cycle);

        verify();
    }

    public void testGetFormSuccess()
    {
        IComponent component = newComponent();
        IForm form = newForm();
        IRequestCycle cycle = newCycle(TapestryUtils.FORM_ATTRIBUTE, form);

        replay();

        IForm actual = TapestryUtils.getForm(cycle, component);

        assertSame(form, actual);

        verify();
    }

    public void testGetPageRenderSupportFailure()
    {
        IComponent component = newMock(IComponent.class);
        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, null);
        
        expect(component.getExtendedId()).andReturn("Foo/bar").anyTimes();
        
        Location l = newLocation();
        expect(component.getLocation()).andReturn(l);

        replay();

        try
        {
            TapestryUtils.getPageRenderSupport(cycle, component);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(TapestryMessages.noPageRenderSupport(component), ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testGetFormFailure()
    {
        Location l = newLocation();
        IComponent component = newMock(IComponent.class);
        
        IRequestCycle cycle = newCycle(TapestryUtils.FORM_ATTRIBUTE, null);
        
        expect(component.getExtendedId()).andReturn("Foo/bar").anyTimes();        
        expect(component.getLocation()).andReturn(l);

        replay();

        try
        {
            TapestryUtils.getForm(cycle, component);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(TapestryMessages.noForm(component), ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testSplitBlank()
    {
        assertListEquals(new String[0], TapestryUtils.split(null));
        assertListEquals(new String[0], TapestryUtils.split(""));
    }

    public void testSplitWithDelimiter()
    {
        assertListEquals(new String[]
        { "fred", "barney" }, TapestryUtils.split("fred|barney", '|'));
    }

    public void testSplitNormal()
    {
        assertListEquals(new String[]
        { "fred", "barney" }, TapestryUtils.split("fred,barney"));
    }

    public void testSplitNoDelimiter()
    {
        assertListEquals(new String[]
        { "no-delimiter" }, TapestryUtils.split("no-delimiter"));
    }

    public void testTrailingDelimiter()
    {
        assertListEquals(new String[]
        { "fred", "barney", "" }, TapestryUtils.split("fred,barney,"));
    }

    public void testEveryDelimiterCounts()
    {
        assertListEquals(new String[]
        { "", "fred", "", "barney", "", "" }, TapestryUtils.split(",fred,,barney,,"));
    }

    public void testEnquote()
    {
        assertEquals("'simple'", TapestryUtils.enquote("simple"));

        assertEquals("'this is a \\\\backslash\\\\'", TapestryUtils
                .enquote("this is a \\backslash\\"));

        assertEquals("'this is a \\'single quote\\''", TapestryUtils
                .enquote("this is a 'single quote'"));
    }

    public void testEnquoteNull()
    {
        assertEquals("''", TapestryUtils.enquote(null));
    }

    public void testConvertTapestryIdToNMToken()
    {
        assertEquals("abc", TapestryUtils.convertTapestryIdToNMToken("abc"));
        assertEquals("abc", TapestryUtils.convertTapestryIdToNMToken("$abc"));
        assertEquals("a_b_c", TapestryUtils.convertTapestryIdToNMToken("$a$b$c"));
    }

    public void testBuildClientElementReference()
    {
        assertEquals("document.getElementById('foo')", TapestryUtils
                .buildClientElementReference("foo"));
    }

    public void testGetComponent()
    {
        IComponent container = newComponent();
        IComponent containee = newComponent();

        trainGetComponent(container, "fred", containee);

        replay();

        assertSame(containee, TapestryUtils.getComponent(container, "fred", IComponent.class, null));

        verify();
    }

    public void testGetComponentWrongType()
    {
        IComponent container = newComponent();
        IComponent containee = newComponent();
        Location l = newLocation();

        trainGetComponent(container, "fred", containee);
        trainGetExtendedId(containee, "Flintstone/fred");

        replay();

        try
        {
            TapestryUtils.getComponent(container, "fred", String.class, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Component Flintstone/fred is not assignable to type java.lang.String.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();

    }

    public void testGetComponentDoesNotExist()
    {
        IComponent container = newComponent();
        Location l = newLocation();

        Throwable t = new RuntimeException("Poof!");

        expect(container.getComponent("fred")).andThrow(t);

        replay();

        try
        {
            TapestryUtils.getComponent(container, "fred", IComponent.class, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Poof!", ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(t, ex.getRootCause());
        }

        verify();
    }
}