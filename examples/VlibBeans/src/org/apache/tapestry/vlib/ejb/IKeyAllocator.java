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

package org.apache.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 *  Remote interface to the KeyAllocator stateless
 *  session bean.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 */

public interface IKeyAllocator extends EJBObject
{
    /**
     *  Allocates a new key, possibling reserving it from
     *  the database.  The value returned is guarenteed to
     *  not have been previously returned by any instance.
     *
     */

    public Integer allocateKey() throws RemoteException;

    /**
     * Allocates several keys, as if invoking {@link #allocateKey}
     * multiple times.  No guarentees are made that the
     * values are sequential or in any order, just that they
     * are unique.
     *
     */

    public Integer[] allocateKeys(int count) throws RemoteException;
}