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

import javax.ejb.EJBObject;

/** 
 *  Defines the remove interface for an entity which can download and upload
 *  a subset of its properties as a {@link Map}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IEntityBean extends EJBObject
{
	/**
	 *  Returns the simple attributes of the entity as a Map.
	 *
	 **/

	public Map getEntityAttributes() throws RemoteException;

	/**
	 *  Updates some or all of the properties of the entity.
	 *
	 **/

	public void updateEntityAttributes(Map data) throws RemoteException;
}