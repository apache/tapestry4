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

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests a few new features of {@link org.apache.tapestry.AbstractComponent}&nbsp;added in release
 * 3.1.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestAbstractComponent extends HiveMindTestCase
{
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
            assertEquals(TapestryMessages.providedByEnhancement("getSpecification"), ex
                    .getMessage());
        }
    }
}