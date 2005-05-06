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

package org.apache.tapestry.valid;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.lib.BeanFactory;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingFactory;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Uses the tapestry.valid.ValidatorBeanFactory service to obtain configuration IValidator
 * instances.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.valid.ValidatorBinding
 */
public class ValidatorBindingFactory implements BindingFactory
{
    private BeanFactory _validatorBeanFactory;

    private ValueConverter _valueConverter;

    public void setValidatorBeanFactory(BeanFactory validatorBeanFactory)
    {
        _validatorBeanFactory = validatorBeanFactory;
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }

    /**
     * Creates and returns a {@link ValidatorBinding}. Interprets the path as a bean initializer,
     * used to locate a particular type of validator and a particular configuration of its
     * properties.
     */

    public IBinding createBinding(IComponent root, String bindingDescription, String path,
            Location location)
    {
        try
        {
            IValidator validator = (IValidator) _validatorBeanFactory.get(path);

            return new ValidatorBinding(bindingDescription, _valueConverter, location, validator);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), location, ex);
        }
    }

}