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
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import org.easymock.MockControl;

/**
 * Additional tests for {@link org.apache.tapestry.pageload.PageLoader}. Ultimately, testing this
 * beast without the mock unit test suites is going to take a lot of work and refactoring.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPageLoader extends HiveMindTestCase
{
    private IComponentSpecification newSpec(boolean allowInformalParameters)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getAllowInformalParameters();
        control.setReturnValue(allowInformalParameters);

        return spec;
    }

    public IComponent newComponent(IComponentSpecification spec)
    {
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        component.getSpecification();
        control.setReturnValue(spec);

        return component;
    }

    private IBinding newBinding()
    {
        return (IBinding) newMock(IBinding.class);
    }

    private IBinding newBinding(Location l)
    {
        MockControl control = newControl(IBinding.class);
        IBinding binding = (IBinding) control.getMock();

        binding.getLocation();
        control.setReturnValue(l);

        return binding;
    }

    public void testaddDuplicateBindingFails()
    {
        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        Location l1 = newLocation();
        Location l2 = newLocation();

        IBinding oldBinding = newBinding(l1);
        IBinding newBinding = newBinding(l2);

        component.getBinding("dupe");
        componentc.setReturnValue(oldBinding);

        replayControls();

        try
        {
            PageLoader.addBindingToComponent(component, "dupe", newBinding);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "A binding for parameter dupe conflicts with a previous binding (at classpath:/org/apache/tapestry/pageload/TestPageLoader, line 1).",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l2, ex.getLocation());
        }
    }

    public void testBindAlias()
    {
        MockControl containerc = newControl(IComponent.class);
        IComponent container = (IComponent) containerc.getMock();

        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

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

        component.getSpecification();
        componentc.setReturnValue(spec);

        Log log = (Log) newMock(Log.class);

        log
                .error("Parameter barney (for component FredComponent, at classpath:/org/apache/tapestry/pageload/TestPageLoader, line 1) was bound; this parameter has been deprecated, bind parameter fred instead.");

        IBinding binding = newBinding();
        MockControl sourcec = newControl(BindingSource.class);
        BindingSource source = (BindingSource) sourcec.getMock();

        source.createBinding(container, "parameter barney", "an-expression", "ognl", l);
        sourcec.setReturnValue(binding);

        component.getBinding("fred");
        componentc.setReturnValue(null);

        component.setBinding("fred", binding);

        replayControls();

        PageLoader loader = new PageLoader();
        loader.setLog(log);
        loader.setBindingSource(source);

        loader.bind(container, component, contained);

        verifyControls();
    }
}
