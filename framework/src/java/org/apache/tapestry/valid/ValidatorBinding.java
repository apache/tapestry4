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

import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Implementation of {@link IBinding}that provides {@link org.apache.tapestry.valid.IValidator}
 * &nbsp;instances based on a validator bean descriptor. The descriptor is of the form
 * "type[,properties]". The types are values such as "string", "date" or "number" defined in the
 * tapestry.valid.Validators configuration point. The properties are a properties initialization
 * string.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.hivemind.util.PropertyUtils#configureProperties(java.lang.Object,
 *      java.lang.String)
 */
public class ValidatorBinding extends AbstractBinding
{
    private IValidator _validator;

    public ValidatorBinding(String description, ValueConverter valueConverter, Location location,
            IValidator validator)
    {
        super(description, valueConverter, location);

        Defense.notNull(validator, "validator");

        _validator = validator;
    }

    public Object getObject()
    {
        return _validator;
    }

}