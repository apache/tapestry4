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

package org.apache.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

/**
 *  Implementation of the Publisher entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see org.apache.tapestry.vlib.ejb.IPublisher
 *  @see org.apache.tapestry.vlib.ejb.IPublisherHome
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class PublisherBean extends AbstractEntityBean
{
    public abstract void setPublisherId(Integer value);
    
    public abstract Integer getPublisherId();
    
	public abstract String getName();
    
	public abstract void setName(String value);

	protected String[] getAttributePropertyNames()
	{
		return new String[] { "name" };
	}

	public Integer ejbCreate(String name) throws CreateException, RemoteException
	{
		setPublisherId(allocateKey());
		setName(name);
		
		return null;
	}

	public void ejbPostCreate(String name)
	{
		// Do nothing
	}
}