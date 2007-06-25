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
package org.apache.tapestry.enhance;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassLoader used to properly instantiate newly created classes.
 * 
 * @author Howard Lewis Ship / Essl Christian
 * @see org.apache.hivemind.service.impl.CtClassSource
 */
class ClassFactoryClassLoader extends ClassLoader
{
    private List _loaders = new ArrayList();

    /**
     * Adds a delegate class loader to the list of delegate class loaders.
     */
    public synchronized void addDelegateLoader(ClassLoader loader)
    {
        _loaders.add(loader);
    }

    /**
     * Searches each of the delegate class loaders for the given class.
     */
    protected synchronized Class findClass(String name) throws ClassNotFoundException
    {
        ClassNotFoundException cnfex = null;
        
        try
        {
            return super.findClass(name);
        }
        catch (ClassNotFoundException ex)
        {
            cnfex = ex;
        }

        int count = _loaders.size();
        for (int i = 0; i < count; i++)
        {
            ClassLoader l = (ClassLoader) _loaders.get(i);

            try
            {
                return l.loadClass(name);
            }
            catch (ClassNotFoundException ex)
            {
                //
            }
        }
        
        // Not found .. throw the first exception

        throw cnfex;
    }
}
