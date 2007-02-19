// Copyright 2004, 2005 The Apache Software Foundation
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
package org.apache.tapestry.record;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackFilter;


/**
 * Acts as a filter for cglib property watching semantics used 
 * by {@link CglibProxiedPropertyChangeObserverImpl}.
 */
public class ObservableMethodFilter implements CallbackFilter
{

    /**
     * {@inheritDoc}
     */
    public int accept(Method method)
    {
        boolean hasParams = method.getParameterTypes() != null && method.getParameterTypes().length > 0;
        
        if (method.getReturnType() == void.class && hasParams
                || method.getReturnType() != boolean.class && hasParams) {
            
            return 1;
        }
        
        return 0;
    }
}
