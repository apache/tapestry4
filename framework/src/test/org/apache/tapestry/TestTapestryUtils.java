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
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.TapestryUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestTapestryUtils extends HiveMindTestCase
{
    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private IRequestCycle newCycle(String key, Object attribute)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getAttribute(key);
        control.setReturnValue(attribute);

        return cycle;
    }

    private IForm newForm()
    {
        return (IForm) newMock(IForm.class);
    }

    private PageRenderSupport newPageRenderSupport()
    {
        return (PageRenderSupport) newMock(PageRenderSupport.class);
    }

    private IComponent newComponent()
    {
        return (IComponent) newMock(IComponent.class);
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
}