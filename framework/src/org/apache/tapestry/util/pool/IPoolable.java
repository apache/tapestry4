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

package org.apache.tapestry.util.pool;

/**
 *  Marks an object as being aware that is to be stored into a {@link Pool}.
 *  This gives the object a last chance to reset any state.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public interface IPoolable
{
    /**
     *  Invoked by a {@link org.apache.tapestry.util.pool.Pool} 
     *  just before the object is added to the pool.
     *  The object should return its state to how it was when freshly instantiated
     *  (or at least, its state should be indistinguishable from a freshly
     *  instantiated instance).
     *
     **/

    public void resetForPool();
    
    /**
     *  Invoked just as a Pool discards an object (for lack of use).
     *  This allows a last chance to perform final cleanup
     *  on the object while it is still referencable.
     * 
     **/
    
    public void discardFromPool();
}