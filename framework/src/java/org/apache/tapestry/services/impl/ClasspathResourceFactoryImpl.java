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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.services.ClasspathResourceFactory;

/**
 * Implementation of {@link org.apache.tapestry.services.ClasspathResourceFactory}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ClasspathResourceFactoryImpl implements ClasspathResourceFactory
{
    private ClassResolver _classResolver;

    public ClasspathResource newResource(String path)
    {
        return new ClasspathResource(_classResolver, path);
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

}
