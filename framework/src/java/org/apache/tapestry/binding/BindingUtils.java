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

package org.apache.tapestry.binding;

import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BindingUtils
{
    /**
     * Determines the correct default parameter type (to pass to
     * {@link org.apache.tapestry.binding.BindingSource#createBinding(IComponent, String, String, String, Location)}
     * for a component specification and parameter name.
     * 
     * @param specification
     *            the specification of the component for which a binding is being created
     * @param parameterName
     *            the name of the parameter to bind which may be either the name of a formal or
     *            informal parameter
     * @returns the default binding type defined by the matching {@link IParameterSpecification},
     *          or {@link BindingConstants.LITERAL_PREFIX}&nbsp;for informal parameters.
     */
    public static String getDefaultBindingType(IComponentSpecification specification,
            String parameterName, String metaDefaultBindingType)
    {
        String result = null;

        IParameterSpecification ps = specification.getParameter(parameterName);

        if (ps != null)
            result = ps.getDefaultBindingType();

        if (result == null)
            result = metaDefaultBindingType;

        return result;
    }
}