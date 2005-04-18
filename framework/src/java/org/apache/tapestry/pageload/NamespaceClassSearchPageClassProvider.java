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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.INamespace;

/**
 * Searches for a class with a name matching the page name. Searches in the default Java package,
 * and possibly additional packages defined as meta-data within the namespace.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class NamespaceClassSearchPageClassProvider implements PageClassProvider
{
    private ClassResolver _classResolver;

    /**
     * Property, defined as meta data of the containing namespace, that defines a comma-seperated
     * list of packages to search for page classes within.
     */

    public static final String PACKAGES_NAME = "org.apache.tapestry.page-class-packages";

    public String providePageClassName(PageClassProviderContext context)
    {
        List packages = buildPackageSearchList(context.getNamespace());
        String pageClassName = context.getPageName().replace('/', '.');

        Iterator i = packages.iterator();
        while (i.hasNext())
        {
            String packagePrefix = (String) i.next();
            String className = packagePrefix + pageClassName;

            Class clazz = _classResolver.checkForClass(className);

            if (clazz != null)
                return className;
        }

        return null;
    }

    /**
     * Returns a List of String -- prefixes to apply to the page name to form a fully qualified
     * class name.
     */

    List buildPackageSearchList(INamespace namespace)
    {
        List result = new ArrayList();

        String packages = namespace.getPropertyValue(PACKAGES_NAME);

        if (packages != null)
        {
            StringTokenizer tokenizer = new StringTokenizer(packages, ", ");

            while (tokenizer.hasMoreTokens())
            {
                String packageName = tokenizer.nextToken();

                result.add(packageName + ".");
            }
        }

        // Search in the default package as well.

        result.add("");

        return result;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}