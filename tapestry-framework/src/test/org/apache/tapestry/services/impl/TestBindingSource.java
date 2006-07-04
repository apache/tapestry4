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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertSame;

import java.util.Collections;

import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingConstants;
import org.apache.tapestry.binding.BindingFactory;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.BindingSourceImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestBindingSource extends BaseComponentTestCase
{
    public void testNoPrefix()
    {
        IComponent component = newComponent();
        IBinding binding = newBinding();
        BindingFactory factory = newFactory();
        Location l = newLocation();

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix(BindingConstants.LITERAL_PREFIX);
        c.setFactory(factory);

        // Training

        expect(factory.createBinding(component, "foo", "a literal value without a prefix", l)).andReturn(binding);

        replay();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setContributions(Collections.singletonList(c));

        bs.initializeService();

        IBinding actual = bs.createBinding(
                component,
                "foo",
                "a literal value without a prefix",
                BindingConstants.LITERAL_PREFIX,
                l);

        assertSame(binding, actual);

        verify();
    }

    public void testNoPrefixWithDefault()
    {
        IComponent component = newComponent();
        IBinding binding = newBinding();
        BindingFactory factory = newFactory();
        Location l = newLocation();

        // Training

        expect(factory.createBinding(component, "foo", "an-expression", l)).andReturn(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix(BindingConstants.OGNL_PREFIX);
        c.setFactory(factory);

        replay();

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

        verify();
    }

    public void testKnownPrefix()
    {
        IComponent component = newComponent();
        IBinding binding = newBinding();
        BindingFactory factory = newFactory();
        Location l = newLocation();

        // Training

        expect(factory.createBinding(component, "bar", "path part of locator", l)).andReturn(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix("prefix");
        c.setFactory(factory);

        replay();

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

        verify();
    }

    public void testPrefixNoMatch()
    {
        IComponent component = newComponent();
        IBinding binding = newBinding();
        BindingFactory factory = newFactory();
        Location l = newLocation();

        // Training

        expect(factory.createBinding(component, "zip", "unknown:path part of locator", l)).andReturn(binding);

        BindingPrefixContribution c = new BindingPrefixContribution();
        c.setPrefix(BindingConstants.LITERAL_PREFIX);
        c.setFactory(factory);

        replay();

        BindingSourceImpl bs = new BindingSourceImpl();
        bs.setContributions(Collections.singletonList(c));

        bs.initializeService();

        IBinding actual = bs.createBinding(
                component,
                "zip",
                "unknown:path part of locator",
                BindingConstants.LITERAL_PREFIX,
                l);

        assertSame(binding, actual);

        verify();
    }

    protected BindingFactory newFactory()
    {
        return newMock(BindingFactory.class);
    }
}