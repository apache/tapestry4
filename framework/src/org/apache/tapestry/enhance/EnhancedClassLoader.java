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

package org.apache.tapestry.enhance;

import java.security.ProtectionDomain;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

/**
 *  A class loader that can be used to create new classes 
 *  as needed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class EnhancedClassLoader extends ClassLoader
{

    public EnhancedClassLoader(ClassLoader parentClassLoader)
    {
        super(parentClassLoader);
    }

    /**
     *  Defines the new class.
     * 
     *  @throws ApplicationRuntimeException if defining the class fails.
     * 
     **/

    public Class defineClass(String enhancedClassName, byte[] byteCode, ProtectionDomain domain)
    {
        try
        {
            return defineClass(enhancedClassName, byteCode, 0, byteCode.length, domain);
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "EnhancedClassLoader.unable-to-define-class",
                    enhancedClassName,
                    ex.getMessage()),
                ex);
        }
    }
}
