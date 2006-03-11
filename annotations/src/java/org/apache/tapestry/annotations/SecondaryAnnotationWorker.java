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

import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * A chain-of-command interface for secondary annotation workers; workers that must execute
 * <em>after</em> other workers.
 * 
 * @author Howard M. Lewis Ship
 */
public interface SecondaryAnnotationWorker
{
    /**
     * Sees if the particular worker can process the method (because it handles an annotation
     * associated with the method).
     * 
     * @param method
     *            to check
     * @return true if the worker should be invoked, false otherwise
     */
    public boolean canEnhance(Method method);

    /**
     * Invoked an <em>all</em> workers in the command chain, if <em>any</em> worker returns true
     * from {@link #canEnhance(Method)}.
     * 
     * @param op
     *            enhancement operation
     * @param spec
     *            specification for the component being enhanced
     * @param method
     *            the method
     * @param classResource
     *            a resource representing the class; combined with the method a
     *            {@link org.apache.hivemind.Location} can be created
     */
    public void peformEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Resource classResource);
}
