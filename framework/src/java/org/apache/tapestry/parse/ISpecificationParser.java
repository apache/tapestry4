// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import org.apache.hivemind.Resource;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Service interface for a wrapper around the class
 * {@link org.apache.tapestry.parse.SpecificationParser}.
 * Because the implementation of {@link org.apache.tapestry.parse.SpecificationParser}
 * is not threadsafe, the implementation of this
 * service uses the pooled service model.
 *
 * <p>
 * Note: had to use the 'I' prefix, so that {@link org.apache.tapestry.parse.SpecificationParser}
 * could keep its name. Otherwise, it makes Spindle support really, really ugly.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ISpecificationParser
{
    /**
     * Parses an application specification from the provided Resource.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException on any error.
     */
    public IApplicationSpecification parseApplicationSpecification(Resource resource);

    public IComponentSpecification parseComponentSpecification(Resource resource);

    public ILibrarySpecification parseLibrarySpecification(Resource resource);

    public IComponentSpecification parsePageSpecification(Resource resource);
}
