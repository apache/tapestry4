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

package org.apache.tapestry.form.validator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;

/**
 * Implementation of {@link org.apache.tapestry.form.validator.Validator} that delegates to a
 * managed bean obtained from a component.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class BeanValidatorWrapper extends AbstractValidatorWrapper
{
    private final IComponent _component;

    private final String _name;

    public BeanValidatorWrapper(IComponent component, String name)
    {
        Defense.notNull(component, "component");
        Defense.notNull(name, "name");

        _component = component;
        _name = name;
    }

    protected Validator getDelegate()
    {
        Object bean = _component.getBeans().getBean(_name);

        if (bean instanceof Validator)
            return (Validator) bean;

        throw new ApplicationRuntimeException(ValidatorMessages.beanNotValidator(_name), bean,
                null, null);
    }

}
