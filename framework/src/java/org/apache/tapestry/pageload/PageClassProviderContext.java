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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Contains information needed when trying to determine the name of a page class.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PageClassProviderContext
{

    private INamespace _namespace;

    private String _pageName;

    private IComponentSpecification _pageSpecification;

    public PageClassProviderContext(String pageName, IComponentSpecification pageSpecification,
            INamespace namespace)
    {
        Defense.notNull(pageName, "pageName");
        Defense.notNull(pageSpecification, "pageSpecification");
        Defense.notNull(namespace, "namespace");

        _pageName = pageName;
        _pageSpecification = pageSpecification;
        _namespace = namespace;
    }

    /**
     * Returns the simple, unqualifed name of the page (that is, with no namespace prefix).
     */

    public String getPageName()
    {
        return _pageName;
    }

    /**
     * Returns the namespace containing the page.
     */

    public INamespace getNamespace()
    {
        return _namespace;
    }

    /**
     * Returns the specification defining the page.
     */
    public IComponentSpecification getPageSpecification()
    {
        return _pageSpecification;
    }

}