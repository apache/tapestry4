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

package org.apache.tapestry.pageload;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.*;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

/**
 * Additional tests for {@link org.apache.tapestry.pageload.PageLoader}. Ultimately, testing this
 * beast without the mock unit test suites is going to take a lot of work and refactoring.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class PageLoaderTest extends BaseComponentTestCase
{

    public void test_Add_Duplicate_Binding_Fails()
    {
        IComponent component = newComponent();
        Location l1 = newLocation();
        Location l2 = newLocation();
        IBinding oldBinding = newBinding(l1);
        IBinding newBinding = newBinding(l2);

        trainGetBinding(component, "dupe", oldBinding);

        replay();

        try
        {
            PageLoader.addBindingToComponent(component, "dupe", newBinding);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assert ex.getMessage()
            .indexOf("A binding for parameter dupe conflicts with a previous binding") > -1;
            assertSame(component, ex.getComponent());
            assertSame(l2, ex.getLocation());
        }
    }

    public void test_Bind_Alias()
    {
        IComponent container = newComponent();
        IComponent component = newComponent();
        Log log = newLog();
        IBinding binding = newBinding();
        BindingSource source = newBindingSource();

        ParameterSpecification pspec = new ParameterSpecification();
        pspec.setParameterName("fred");
        pspec.setAliases("barney");

        Location l = newLocation();

        BindingSpecification bspec = new BindingSpecification();
        bspec.setType(BindingType.PREFIXED);
        bspec.setValue("an-expression");
        bspec.setLocation(l);

        ContainedComponent contained = new ContainedComponent();
        contained.setBinding("barney", bspec);
        contained.setType("FredComponent");

        IComponentSpecification spec = new ComponentSpecification();
        spec.addParameter(pspec);

        trainGetSpecification(component, spec);

        log.warn(startsWith("Parameter barney (for component FredComponent, at "));

        trainCreateBinding(
                source,
                container,
                pspec,
                "parameter barney",
                "an-expression",
                "ognl",
                l,
                binding);

        trainGetBinding(component, "fred", null);

        component.setBinding("fred", binding);

        replay();

        PageLoader loader = new PageLoader();
        loader.setLog(log);
        loader.setBindingSource(source);

        loader.bind(container, component, contained, "ognl");

        verify();
    }

    private void trainCreateBinding(BindingSource source, IComponent container, IParameterSpecification ps, String description,
            String expression, String defaultBindingPrefix, Location l, IBinding binding)
    {
        expect(source.createBinding(container, ps, description, expression, defaultBindingPrefix, l)).andReturn(binding);
    }

    protected BindingSource newBindingSource()
    {
        return newMock(BindingSource.class);
    }

    public void test_Bind_Deprecated()
    {
        IComponent container = newComponent();
        IComponent component = newComponent();
        IBinding binding = newBinding();
        BindingSource source = newBindingSource();
        Log log = newLog();

        ParameterSpecification pspec = new ParameterSpecification();
        pspec.setParameterName("fred");
        pspec.setDeprecated(true);

        Location l = newLocation();

        BindingSpecification bspec = new BindingSpecification();
        bspec.setType(BindingType.PREFIXED);
        bspec.setValue("an-expression");
        bspec.setLocation(l);

        ContainedComponent contained = new ContainedComponent();
        contained.setBinding("fred", bspec);
        contained.setType("FredComponent");

        IComponentSpecification spec = new ComponentSpecification();
        spec.addParameter(pspec);

        trainGetSpecification(component, spec);

        log.warn(endsWith("has been deprecated, "
                        + "and may be removed in a future release. Consult the documentation for component FredComponent to "
                        + "determine an appropriate replacement."));

        trainCreateBinding(source, container, pspec, "parameter fred", "an-expression", "ognl", l, binding);

        trainGetBinding(component, "fred", null);

        component.setBinding("fred", binding);

        replay();

        PageLoader loader = new PageLoader();
        loader.setLog(log);
        loader.setBindingSource(source);

        loader.bind(container, component, contained, "ognl");

        verify();
    }

    protected ComponentPropertySource newPropertySource()
    {
        return newMock(ComponentPropertySource.class);
    }
}
