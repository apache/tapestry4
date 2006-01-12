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
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.test.Creator;

/**
 * Tests a few new features of {@link org.apache.tapestry.AbstractComponent}&nbsp;added
 * in release 4.0.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAbstractComponent extends BaseComponentTestCase
{

    /** Test fixture. */
    private static class ConcreteComponent extends AbstractComponent
    {

        protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        {
        }

    }

    public void testUnimplementedMethods()
    {
        IComponent component = new ConcreteComponent();

        try
        {
            component.getMessages();
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(
                    "Method getMessages() is not implemented. An implementation of this method should be provided via runtime class enhancement.",
                    ex.getMessage());
        }

        try
        {
            component.getSpecification();
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(TapestryMessages.providedByEnhancement("getSpecification"), ex.getMessage());
        }
    }

    public void testContainedComponent()
    {
        Creator creator = new Creator();

        IContainedComponent cc = newContainedComponent();

        replayControls();

        IComponent component = (IComponent)creator.newInstance(BaseComponent.class);

        component.setContainedComponent(cc);

        assertSame(cc, component.getContainedComponent());

        verifyControls();
    }

    public void testContainedComponentConflict()
    {
        Creator creator = new Creator();

        IContainedComponent cc1 = newContainedComponent();
        IContainedComponent cc2 = newContainedComponent();

        IPage page = newPage("Fred");

        trainGetIdPath(page, null);

        replayControls();

        IComponent component = (IComponent)creator.newInstance(BaseComponent.class, new Object[] { "page", page,
                "container", page, "id", "barney" });

        component.setContainedComponent(cc1);

        try
        {
            component.setContainedComponent(cc2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Attempt to change containedComponent property of component Fred/barney, which is not allowed.", ex
                            .getMessage());
        }

        verifyControls();
    }

    private IContainedComponent newContainedComponent()
    {
        return (IContainedComponent)newMock(IContainedComponent.class);
    }
}
