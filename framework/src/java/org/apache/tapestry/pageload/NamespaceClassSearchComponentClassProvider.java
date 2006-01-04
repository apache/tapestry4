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

import org.apache.tapestry.INamespace;
import org.apache.tapestry.services.ClassFinder;

/**
 * Searches for a class with a name matching the page name. Searches in the default Java package,
 * and possibly additional packages defined as meta-data within the namespace.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class NamespaceClassSearchComponentClassProvider implements ComponentClassProvider
{
    /**
     * Property, defined as meta data of the containing namespace, that defines a comma-seperated
     * list of packages to search for page or component classes within.
     */
    private String _packagesName;

    private ClassFinder _classFinder;

    public String provideComponentClassName(ComponentClassProviderContext context)
    {
        INamespace namespace = context.getNamespace();
        String packages = namespace.getPropertyValue(_packagesName);

        String componentClassName = context.getName().replace('/', '.');

        Class clazz = _classFinder.findClass(packages, componentClassName);

        return clazz == null ? null : clazz.getName();
    }

    public void setPackagesName(String packagesName)
    {
        _packagesName = packagesName;
    }

    public void setClassFinder(ClassFinder classFinder)
    {
        _classFinder = classFinder;
    }
}