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

import net.sf.cglib.proxy.LazyLoader;


/**
 * Implementation of {@link LazyLoader} interface for {@link CglibProxiedPropertyChangeObserverImpl}.
 */
public class LazyProxyDelegate implements LazyLoader
{

    Object _target;
    
    /**
     * Creates a new lazily loaded proxy delegate object used to create efficient
     * pass through proxied calls to a specific property instance.
     * 
     * @param target
     *          The object being proxied.
     */
    public LazyProxyDelegate(Object target)
    {
        _target = target;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object loadObject()
        throws Exception
    {
        return _target;
    }

}
