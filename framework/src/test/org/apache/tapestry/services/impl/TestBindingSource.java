// Copyright 2004, 2005 The Apache Software Foundation
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

import java.util.Collections;

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingConstants;
import org.apache.tapestry.binding.BindingFactory;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.BindingSourceImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestBindingSource extends HiveMindTestCase
{
    public void testNoPrefix()
    {
        IComponent component = (IComponent) newMock(IComponent.class);
        IBinding binding = (IBinding) newMock(IBinding.class);

        MockControl factoryControl = newControl(BindingFactory.class);
        BindingFactory factory = (BindingFactory) factoryControl.getMock();

        Location l = fabricateLocation(99);

        // Training

        factory.createBinding(component, "foo", "a literal value without a prefix", l);
        factoryControl.setReturnValue(binding);

        replayControls();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setLiteralBindingFactory(factory);

        IBinding actual = bs.createBinding(
                component,
                "foo",
                "a literal value without a prefix",
                BindingConstants.LITERAL_PREFIX,
                l);

        assertSame(binding, actual);

        verifyControls();
    }

    public void testNoPrefixWithDefault()
    {
        IComponent component = (IComponent) newMock(IComponent.class);
        IBinding binding = (IBinding) newMock(IBinding.class);

        MockControl factoryControl = newControl(BindingFactory.class);
        BindingFactory factory = (BindingFactory) factoryControl.getMock();

        Location l = fabricateLocation(99);

        // Training

        factory.createBinding(component, "foo", "an-expression", l);
        factoryControl.setReturnValue(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix(BindingConstants.OGNL_PREFIX);
        c.setFactory(factory);

        replayControls();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setContributions(Collections.singletonList(c));
        bs.initializeService();

        IBinding actual = bs.createBinding(
                component,
                "foo",
                "an-expression",
                BindingConstants.OGNL_PREFIX,
                l);

        assertSame(binding, actual);

        verifyControls();
    }

    public void testKnownPrefix()
    {
        IComponent component = (IComponent) newMock(IComponent.class);
        IBinding binding = (IBinding) newMock(IBinding.class);

        MockControl factoryControl = newControl(BindingFactory.class);
        BindingFactory factory = (BindingFactory) factoryControl.getMock();

        Location l = fabricateLocation(99);

        // Training

        factory.createBinding(component, "bar", "path part of locator", l);
        factoryControl.setReturnValue(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix("prefix");
        c.setFactory(factory);

        replayControls();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setContributions(Collections.singletonList(c));

        bs.initializeService();

        IBinding actual = bs.createBinding(
                component,
                "bar",
                "prefix:path part of locator",
                BindingConstants.LITERAL_PREFIX,
                l);

        assertSame(binding, actual);

        verifyControls();
    }

    public void testPrefixNoMatch()
    {
        IComponent component = (IComponent) newMock(IComponent.class);
        IBinding binding = (IBinding) newMock(IBinding.class);

        MockControl factoryControl = newControl(BindingFactory.class);
        BindingFactory literalFactory = (BindingFactory) factoryControl.getMock();

        BindingFactory factory = (BindingFactory) newMock(BindingFactory.class);

        Location l = fabricateLocation(99);

        // Training

        literalFactory.createBinding(component, "zip", "unknown:path part of locator", l);
        factoryControl.setReturnValue(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix("prefix");
        c.setFactory(factory);

        replayControls();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setContributions(Collections.singletonList(c));
        bs.setLiteralBindingFactory(literalFactory);

        bs.initializeService();

        IBinding actual = bs.createBinding(
                component,
                "zip",
                "unknown:path part of locator",
                BindingConstants.LITERAL_PREFIX,
                l);

        assertSame(binding, actual);

        verifyControls();
    }
}