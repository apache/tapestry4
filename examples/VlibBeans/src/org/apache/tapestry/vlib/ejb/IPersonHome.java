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
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 *  Home interface for the {@link IPerson} entity bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPersonHome extends EJBHome
{
    public IPerson create(Map attributes) throws CreateException, RemoteException;

    public IPerson findByPrimaryKey(Integer key) throws FinderException, RemoteException;

    /**
     *  Finds by exact match on email (which is how users are identified for
     *  login purposes).  Note:  need to figure out how to do a caseless
     *  search instead.
     *
     **/

    public IPerson findByEmail(String email) throws FinderException, RemoteException;
}