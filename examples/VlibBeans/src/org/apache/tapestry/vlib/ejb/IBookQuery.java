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
 *  Remote interface for the BookQuery stateless session bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IBookQuery extends EJBObject
{
    /**
     *  Returns the total number of results rows in the query.
     *
     **/

    public int getResultCount() throws RemoteException;

    /**
     *  Returns a selected subset of the results.
     *
     **/

    public Book[] get(int offset, int length) throws RemoteException;

    /**
     *  Performs a query of books with the matching title and (optionally) publisher.
     *
     *  @param parameters defines subset of books to return.
     *  @param sortOrdering order of items in result set.
     *
     **/

    public int masterQuery(MasterQueryParameters parameters, SortOrdering sortOrdering) throws RemoteException;

    /**
     *  Queries on books owned by a given person.
     *
     **/

    public int ownerQuery(Integer ownerPK, SortOrdering sortOrdering) throws RemoteException;

    /**
     *  Queries on books held by a given person.
     *
     **/

    public int holderQuery(Integer holderPK, SortOrdering sortOrdering) throws RemoteException;

    /**
     *  Queries the list of books held by the borrower but not owned by the borrower.
     *
     **/

    public int borrowerQuery(Integer borrowerPK, SortOrdering sortOrdering) throws RemoteException;
}