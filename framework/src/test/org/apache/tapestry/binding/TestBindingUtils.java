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

package org.apache.tapestry.binding;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.BindingUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestBindingUtils extends HiveMindTestCase
{
    private IComponentSpecification newSpec(String name, IParameterSpecification pspec)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getParameter(name);
        control.setReturnValue(pspec);

        return spec;
    }

    private IParameterSpecification newParameterSpec(String defaultBindingType)
    {
        MockControl control = newControl(IParameterSpecification.class);
        IParameterSpecification pspec = (IParameterSpecification) control.getMock();

        pspec.getDefaultBindingType();
        control.setReturnValue(defaultBindingType);

        return pspec;
    }

    public void testInformalParameter()
    {
        IComponentSpecification spec = newSpec("informal", null);

        replayControls();

        assertEquals("fred", BindingUtils.getDefaultBindingType(spec, "informal", "fred"));

        verifyControls();
    }

    public void testFormalParameterWithDefault()
    {
        IParameterSpecification pspec = newParameterSpec("barney");
        IComponentSpecification spec = newSpec("formal", pspec);

        replayControls();

        assertEquals("barney", BindingUtils.getDefaultBindingType(spec, "formal", "fred"));

        verifyControls();
    }

    public void testFormalParameterWithoutDefault()
    {
        IParameterSpecification pspec = newParameterSpec(null);
        IComponentSpecification spec = newSpec("formal", pspec);

        replayControls();

        assertEquals("fred", BindingUtils.getDefaultBindingType(spec, "formal", "fred"));

        verifyControls();
    }
}