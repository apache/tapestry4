//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Obtains meta-data properties from the
 * {@link org.apache.tapestry.spec.IApplicationSpecification}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ApplicationSpecificationPropertySource implements IPropertySource
{
    private IApplicationSpecification _specification;

    /**
     * Invokes {@link org.apache.tapestry.util.IPropertyHolder#getProperty(String)}
     * on the specification.
     */
    public String getPropertyValue(String propertyName)
    {
        return _specification.getProperty(propertyName);
    }

    public void setSpecification(IApplicationSpecification specification)
    {
        _specification = specification;
    }

}
