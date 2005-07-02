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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.ParameterSpecification;

/**
 * Generates a {@link org.apache.tapestry.spec.IParameterSpecification} from a
 * {@link org.apache.tapestry.annotations.Parameter} annotation and adds it to the
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ParameterAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        Parameter parameter = method.getAnnotation(Parameter.class);

        String propertyName = AnnotationUtils.getPropertyName(method);

        boolean deprecated = method.isAnnotationPresent(Deprecated.class);

        IParameterSpecification ps = new ParameterSpecification();

        String parameterName = parameter.name();

        if (HiveMind.isBlank(parameterName))
            parameterName = propertyName;

        Class propertyType = op.getPropertyType(propertyName);

        ps.setAliases(parameter.aliases());
        ps.setCache(parameter.cache());

        if (HiveMind.isNonBlank(parameter.defaultBinding()))
            ps.setDefaultBindingType(parameter.defaultBinding());

        if (HiveMind.isNonBlank(parameter.defaultValue()))
            ps.setDefaultValue(parameter.defaultValue());

        ps.setDeprecated(deprecated);
        ps.setParameterName(parameterName);
        ps.setPropertyName(propertyName);
        ps.setRequired(parameter.required());
        ps.setType(propertyType.getName());
        ps.setLocation(location);

        spec.addParameter(ps);
    }
}
