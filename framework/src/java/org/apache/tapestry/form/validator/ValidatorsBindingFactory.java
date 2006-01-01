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

package org.apache.tapestry.form.validator;

import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.AbstractBindingFactory;

public class ValidatorsBindingFactory extends AbstractBindingFactory
{
    private ValidatorFactory _validatorFactory;

    public void setValidatorFactory(ValidatorFactory validatorFactory)
    {
        _validatorFactory = validatorFactory;
    }

    public IBinding createBinding(IComponent root, String bindingDescription, String expression,
            Location location)
    {
        try
        {
            List validators = _validatorFactory.constructValidatorList(root, expression);

            return new ValidatorsBinding(bindingDescription, getValueConverter(), location,
                    validators);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), null, location, ex);
        }
    }
}
