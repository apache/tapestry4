//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//


package net.sf.tapestry.vlib.ejb;

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