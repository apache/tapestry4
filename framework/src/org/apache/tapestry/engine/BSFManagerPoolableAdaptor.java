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

package org.apache.tapestry.engine;

import org.apache.bsf.BSFManager;
import org.apache.tapestry.util.pool.IPoolableAdaptor;

/**
 *  Allows a {@link org.apache.tapestry.util.pool.Pool} to
 *  properly terminate a {@link org.apache.bsf.BSFManager}
 *  when it is discarded.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class BSFManagerPoolableAdaptor implements IPoolableAdaptor
{
    /**
     *  Does nothing.
     * 
     **/
    
    public void resetForPool(Object object)
    {
    }

    /**
     *  Invokes {@link org.apache.bsf.BSFManager#terminate()}.
     * 
     **/
    
    public void discardFromPool(Object object)
    {
        BSFManager manager = (BSFManager)object;
        
        manager.terminate();
    }

}
