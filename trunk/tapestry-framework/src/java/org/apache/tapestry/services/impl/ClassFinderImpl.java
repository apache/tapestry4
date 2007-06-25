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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.ClassFinder;

/**
 * Implemenation of service tapestry.ClassFinder.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ClassFinderImpl implements ClassFinder
{
    private ClassResolver _classResolver;

    public Class findClass(String packageList, String className)
    {
        String[] packages = TapestryUtils.split(packageList);

        for (int i = 0; i < packages.length; i++)
        {
            String fullName = packages[i].trim() + "." + className;

            Class result = _classResolver.checkForClass(fullName);

            if (result != null)
                return result;
        }

        return _classResolver.checkForClass(className);
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

}
