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

package org.apache.tapestry.pageload;

import java.util.Iterator;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 *  For all parameters in the examined component that have default values, but are not bound,
 *  automatically add an ExpressionBinding with the default value.
 *   
 *  @author mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class EstablishDefaultParameterValuesVisitor implements IComponentVisitor
{
    private IResourceResolver _resolver;
    
    public EstablishDefaultParameterValuesVisitor(IResourceResolver resolver)
    {
        _resolver = resolver;
    }
    
    /**
     * @see org.apache.tapestry.pageload.IComponentVisitor#visitComponent(org.apache.tapestry.IComponent)
     */
    public void visitComponent(IComponent component)
    {
        IComponentSpecification spec = component.getSpecification();

        Iterator i = spec.getParameterNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            IParameterSpecification parameterSpec = spec.getParameter(name);

            String defaultValue = parameterSpec.getDefaultValue();
            if (defaultValue == null)
                continue;
            
            // the parameter has a default value, so it must not be required
            if (parameterSpec.isRequired())
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "EstablishDefaultParameterValuesVisitor.parameter-must-have-no-default-value",
                    component.getExtendedId(),
                    name),
                component,
                parameterSpec.getLocation(),
                null);
            
            // if there is no binding for this parameter, bind it to the default value
            if (component.getBinding(name) == null) {
                IBinding binding = new ExpressionBinding(_resolver, component, defaultValue, parameterSpec.getLocation());
                component.setBinding(name, binding);
            }
                
        }
    }

}
