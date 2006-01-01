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

package org.apache.tapestry.pageload;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ParameterSpecification;

/**
 * Additional tests for {@link org.apache.tapestry.pageload.PageLoader}. Ultimately, testing this
 * beast without the mock unit test suites is going to take a lot of work and refactoring.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PageLoaderTest extends BaseComponentTestCase
{

    public void testAddDuplicateBindingFails()
    {
        IComponent component = newComponent();
        Location l1 = newLocation();
        Location l2 = newLocation();
        IBinding oldBinding = newBinding(l1);
        IBinding newBinding = newBinding(l2);

        trainGetBinding(component, "dupe", oldBinding);

        replayControls();

        try
        {
            PageLoader.addBindingToComponent(component, "dupe", newBinding);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "A binding for parameter dupe conflicts with a previous binding (at classpath:/org/apache/tapestry/pageload/PageLoaderTest, line 1).",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l2, ex.getLocation());
        }
    }

    public void testBindAlias()
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

        log
                .warn("Parameter barney (for component FredComponent, at classpath:/org/apache/tapestry/pageload/PageLoaderTest, line 1) was bound; this parameter has been deprecated, bind parameter fred instead.");

        trainCreateBinding(
                source,
                container,
                "parameter barney",
                "an-expression",
                "ognl",
                l,
                binding);

        trainGetBinding(component, "fred", null);

        component.setBinding("fred", binding);

        replayControls();

        PageLoader loader = new PageLoader();
        loader.setLog(log);
        loader.setBindingSource(source);

        loader.bind(container, component, contained, "ognl");

        verifyControls();
    }

    private void trainCreateBinding(BindingSource source, IComponent container, String description,
            String expression, String defaultBindingPrefix, Location l, IBinding binding)
    {
        source.createBinding(container, description, expression, defaultBindingPrefix, l);
        setReturnValue(source, binding);
    }

    protected BindingSource newBindingSource()
    {
        return (BindingSource) newMock(BindingSource.class);
    }

    public void testBindDeprecated()
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

        log
                .warn("Parameter fred (at classpath:/org/apache/tapestry/pageload/PageLoaderTest, line 1) has been deprecated, "
                        + "and may be removed in a future release. Consult the documentation for component FredComponent to "
                        + "determine an appropriate replacement.");

        trainCreateBinding(source, container, "parameter fred", "an-expression", "ognl", l, binding);

        trainGetBinding(component, "fred", null);

        component.setBinding("fred", binding);

        replayControls();

        PageLoader loader = new PageLoader();
        loader.setLog(log);
        loader.setBindingSource(source);

        loader.bind(container, component, contained, "ognl");

        verifyControls();
    }

    protected ComponentPropertySource newPropertySource()
    {
        return (ComponentPropertySource) newMock(ComponentPropertySource.class);
    }
}
