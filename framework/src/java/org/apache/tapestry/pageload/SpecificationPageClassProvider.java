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

package org.apache.tapestry.pageload;

/**
 * Implementation of {@link org.apache.tapestry.pageload.PageClassProvider}&nbsp;that returns the
 * value from the specification.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SpecificationPageClassProvider implements PageClassProvider
{

    public String providePageClassName(PageClassProviderContext context)
    {
        // May return null when the spec doesn't indicate the class name.

        return context.getPageSpecification().getComponentClassName();
    }

}