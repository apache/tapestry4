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

package org.apache.tapestry.junit.mock.c22;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.resolver.ISpecificationResolverDelegate;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Part of the test harness for testing 
 *  {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class DummySpecDelegate implements ISpecificationResolverDelegate
{

    public IComponentSpecification findPageSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String simplePageName)
    {
     	cycle.getRequestContext().getSession().setAttribute("page", simplePageName);
     	
     	return null;
    }

    public IComponentSpecification findComponentSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String type)
    {
      	cycle.getRequestContext().getSession().setAttribute("type", type);
      	
      	return null;
    }

}
