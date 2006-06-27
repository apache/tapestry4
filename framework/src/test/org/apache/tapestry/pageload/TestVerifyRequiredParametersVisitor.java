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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ParameterSpecification;

/**
 * Tests for {@link org.apache.tapestry.pageload.VerifyRequiredParametersVisitor}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestVerifyRequiredParametersVisitor extends BaseComponentTestCase
{
    private IComponent newComponent(IComponentSpecification spec)
    {
        IComponent component = newComponent();

        expect(component.getSpecification()).andReturn(spec);

        return component;
    }

    public void testNotRequired()
    {
        ParameterSpecification pspec = new ParameterSpecification();
        pspec.setParameterName("fred");

        ComponentSpecification cspec = new ComponentSpecification();
        cspec.addParameter(pspec);

        IComponent component = newComponent(cspec);

        replay();

        VerifyRequiredParametersVisitor visitor = new VerifyRequiredParametersVisitor();

        visitor.visitComponent(component);

        verify();
    }

    public void testRequiredWithAlias()
    {
        ParameterSpecification pspec = new ParameterSpecification();
        pspec.setParameterName("fred");
        pspec.setAliases("barney");
        pspec.setRequired(true);

        ComponentSpecification cspec = new ComponentSpecification();
        cspec.addParameter(pspec);

        IBinding fredBinding = newBinding();
        IComponent component = newComponent(cspec);

        // Notice that we don't ever check for "barney", just
        // "fred"

        expect(component.getBinding("fred")).andReturn(fredBinding);

        replay();

        VerifyRequiredParametersVisitor visitor = new VerifyRequiredParametersVisitor();

        visitor.visitComponent(component);

        verify();
    }

    public void testRequiredNotBound()
    {
        ParameterSpecification pspec = new ParameterSpecification();
        pspec.setParameterName("fred");
        pspec.setRequired(true);

        ComponentSpecification cspec = new ComponentSpecification();
        cspec.addParameter(pspec);

        Location l = newLocation();
        
        IComponent component = newComponent(cspec);

        expect(component.getBinding("fred")).andReturn(null);

        expect(component.getExtendedId()).andReturn("Fred/flintstone");

        expect(component.getLocation()).andReturn(l);

        replay();

        VerifyRequiredParametersVisitor visitor = new VerifyRequiredParametersVisitor();

        try
        {
            visitor.visitComponent(component);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Required parameter fred of component Fred/flintstone is not bound.", ex
                    .getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}
