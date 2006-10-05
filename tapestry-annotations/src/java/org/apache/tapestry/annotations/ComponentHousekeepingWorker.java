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

import java.util.Collection;
import java.util.Iterator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * An enhancement worker which performs several housekeeping tasks
 * relating to injected components.
 * Currently the worker ensures that copies of components contain
 * the correct bindings.
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
public class ComponentHousekeepingWorker implements EnhancementWorker
{
    public void performEnhancement( EnhancementOperation op, IComponentSpecification spec )
    {
        for ( Iterator i = spec.getComponentIds().iterator(); i.hasNext(); )
        {
            String id = ( String ) i.next();
            IContainedComponent cc = spec.getComponent(id);
            String copyOf = cc.getCopyOf();
            Collection bindingNames = cc.getBindingNames();
            if (HiveMind.isNonBlank(copyOf) && bindingNames.size() == 0)
            {
                IContainedComponent source = spec.getComponent(copyOf);
                if (source == null)
                    throw new ApplicationRuntimeException(AnnotationMessages.unableToCopy(copyOf));                
                AnnotationUtils.copyBindings(source, cc);
            }            
        }
    }
}
