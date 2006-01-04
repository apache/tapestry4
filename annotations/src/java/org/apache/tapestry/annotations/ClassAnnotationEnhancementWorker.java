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

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Defines workers that perform annotation enhancements at the class level.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface ClassAnnotationEnhancementWorker
{
    /**
     * Performs a particular enhancement based on a registered annotation. Exception reporting is
     * the responsibility of the caller.
     * 
     * @param op
     *            the enhancement operaration
     * @param spec
     *            the specification of the component for which a class is being enhanced
     * @param baseClass
     *            the class containing the annotation
     * @param location
     *            the location associated with the annotation
     */

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Class baseClass, Location location);

}
