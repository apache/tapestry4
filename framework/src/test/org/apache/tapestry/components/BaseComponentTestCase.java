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

package org.apache.tapestry.components;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BaseComponentTestCase extends HiveMindTestCase
{
    private Creator _creator;

    protected Creator getCreator()
    {
        if (_creator == null)
            _creator = new Creator();

        return _creator;
    }

    protected Object newInstance(Class componentClass)
    {
        return newInstance(componentClass, null);
    }

    protected Object newInstance(Class componentClass, Object[] properties)
    {
        return getCreator().newInstance(componentClass, properties);
    }

    protected IRequestCycle newRequestCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected IRequestCycle newRequestCycle(boolean rewinding)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(rewinding);

        return cycle;
    }

    protected IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    protected IBinding newBinding(Object value)
    {
        MockControl control = newControl(IBinding.class);
        IBinding binding = (IBinding) control.getMock();
    
        binding.getObject();
        control.setReturnValue(value);
    
        return binding;
    }

    protected IComponentSpecification newSpec(String parameterName, IParameterSpecification pspec)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();
    
        spec.getParameter(parameterName);
        control.setReturnValue(pspec);
    
        return spec;
    }

}