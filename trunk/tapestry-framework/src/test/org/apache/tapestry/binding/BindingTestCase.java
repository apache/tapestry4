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

package org.apache.tapestry.binding;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Base class for building tests for {@link org.apache.tapestry.IBinding}implementations.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BindingTestCase extends BaseComponentTestCase
{
    protected IComponent newComponent(String extendedId)
    {
        IComponent component = newMock(IComponent.class);

        expect(component.getExtendedId()).andReturn(extendedId);

        return component;
    }

    protected ValueConverter newValueConverter()
    {
        return newMock(ValueConverter.class);
    }

}
