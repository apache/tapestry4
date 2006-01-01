// Copyright 2005, 2006 The Apache Software Foundation
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
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.TapestryUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TapestryUtilsTest extends BaseComponentTestCase
{

    private IRequestCycle newCycle(String key, Object attribute)
    {
        IRequestCycle cycle = newCycle();

        trainGetAttribute(cycle, key, attribute);

        return cycle;
    }

    public void testStoreUniqueAttributeSuccess()
    {
        Object newInstance = new Object();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        String key = "foo.bar.Baz";

        cycle.getAttribute(key);
        control.setReturnValue(null);

        cycle.setAttribute(key, newInstance);

        replayControls();

        TapestryUtils.storeUniqueAttribute(cycle, key, newInstance);

        verifyControls();
    }

    public void testStoreUniqueAttributeFailure()
    {
        Object existing = "*EXISTING*";
        Object newInstance = "*NEW*";

        String key = "foo.bar.Baz";

        IRequestCycle cycle = newCycle(key, existing);

        replayControls();

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

        verifyControls();
    }

    public void testGetPageRenderSupportSuccess()
    {
        IComponent component = newComponent();
        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, support);

        replayControls();

        PageRenderSupport actual = TapestryUtils.getPageRenderSupport(cycle, component);

        assertSame(support, actual);

        verifyControls();
    }

    public void testRemovePageRenderSupport()
    {
        IRequestCycle cycle = newCycle();

        cycle.removeAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);

        replayControls();

        TapestryUtils.removePageRenderSupport(cycle);

        verifyControls();
    }

    public void testRemoveForm()
    {
        IRequestCycle cycle = newCycle();

        cycle.removeAttribute(TapestryUtils.FORM_ATTRIBUTE);

        replayControls();

        TapestryUtils.removeForm(cycle);

        verifyControls();
    }

    public void testGetFormSuccess()
    {
        IComponent component = newComponent();
        IForm form = newForm();
        IRequestCycle cycle = newCycle(TapestryUtils.FORM_ATTRIBUTE, form);

        replayControls();

        IForm actual = TapestryUtils.getForm(cycle, component);

        assertSame(form, actual);

        verifyControls();
    }

    public void testGetPageRenderSupportFailure()
    {
        Location l = newLocation();
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        component.getExtendedId();
        control.setReturnValue("Foo/bar", 1);

        component.getLocation();
        control.setReturnValue(l);

        component.getExtendedId();
        control.setReturnValue("Foo/bar", 1);

        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, null);

        replayControls();

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

        verifyControls();
    }

    public void testGetFormFailure()
    {
        Location l = newLocation();
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        component.getExtendedId();
        control.setReturnValue("Foo/bar", 1);

        component.getLocation();
        control.setReturnValue(l);

        component.getExtendedId();
        control.setReturnValue("Foo/bar", 1);

        IRequestCycle cycle = newCycle(TapestryUtils.FORM_ATTRIBUTE, null);

        replayControls();

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

        verifyControls();
    }

    public void testSplitBlank()
    {
        assertListsEqual(new String[0], TapestryUtils.split(null));
        assertListsEqual(new String[0], TapestryUtils.split(""));
    }

    public void testSplitWithDelimiter()
    {
        assertListsEqual(new String[]
        { "fred", "barney" }, TapestryUtils.split("fred|barney", '|'));
    }

    public void testSplitNormal()
    {
        assertListsEqual(new String[]
        { "fred", "barney" }, TapestryUtils.split("fred,barney"));
    }

    public void testSplitNoDelimiter()
    {
        assertListsEqual(new String[]
        { "no-delimiter" }, TapestryUtils.split("no-delimiter"));
    }

    public void testTrailingDelimiter()
    {
        assertListsEqual(new String[]
        { "fred", "barney", "" }, TapestryUtils.split("fred,barney,"));
    }

    public void testEveryDelimiterCounts()
    {
        assertListsEqual(new String[]
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

        replayControls();

        assertSame(containee, TapestryUtils.getComponent(container, "fred", IComponent.class, null));

        verifyControls();
    }

    public void testGetComponentWrongType()
    {
        IComponent container = newComponent();
        IComponent containee = newComponent();
        Location l = newLocation();

        trainGetComponent(container, "fred", containee);
        trainGetExtendedId(containee, "Flintstone/fred");

        replayControls();

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

        verifyControls();

    }

    public void testGetComponentDoesNotExist()
    {
        IComponent container = newComponent();
        Location l = newLocation();

        Throwable t = new RuntimeException("Poof!");

        container.getComponent("fred");
        setThrowable(container, t);

        replayControls();

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

        verifyControls();
    }
}