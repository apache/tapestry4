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

package org.apache.tapestry.resolver;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Stand-in class used when the application fails to specify an actual delegate.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class NullSpecificationResolverDelegate implements ISpecificationResolverDelegate
{
    private static NullSpecificationResolverDelegate _shared;

    public static NullSpecificationResolverDelegate getSharedInstance()
    {
        if (_shared == null)
            _shared = new NullSpecificationResolverDelegate();

        return _shared;
    }

    /**
     *  Returns null.
     * 
     **/

    public IComponentSpecification findPageSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String simplePageName)
    {
        return null;
    }

    /**
     *  Returns null.
     * 
     **/

    public IComponentSpecification findComponentSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String type)
    {
        return null;
    }

}
