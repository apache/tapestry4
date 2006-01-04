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

import java.util.List;

import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Binding used to hold a list of {@link org.apache.tapestry.form.validator.Validator}s.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ValidatorsBinding extends AbstractBinding
{
    private final List _validators;

    public ValidatorsBinding(String description, ValueConverter valueConverter, Location location,
            List validators)
    {
        super(description, valueConverter, location);

        Defense.notNull(validators, "validator");

        _validators = validators;
    }

    public Object getObject()
    {
        return _validators;
    }

}
