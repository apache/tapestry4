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

package org.apache.tapestry.test;

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhanceUtils;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Forces the creation of a property, overrriding an existing implementation. Allows test code to
 * set the specification and messages properties of components.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class CreatePropertyWorker implements EnhancementWorker
{
    private final String _propertyName;

    private final Location _location;

    public CreatePropertyWorker(String propertyName, Location location)
    {
        _propertyName = propertyName;
        _location = location;
    }

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Class propertyType = EnhanceUtils.extractPropertyType(op, _propertyName, null);

        op.claimProperty(_propertyName);

        String field = "_$" + _propertyName;

        op.addField(field, propertyType);

        EnhanceUtils.createSimpleAccessor(op, field, _propertyName, propertyType, _location);

        EnhanceUtils.createSimpleMutator(op, field, _propertyName, propertyType, _location);
    }
}