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

package net.sf.tapestry;

/**
 *  A runtime exception thrown when an {@link IComponent} is asked for a contained
 *  component that does not exist.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class NoSuchComponentException extends RuntimeException
{
	private String componentId;
	private transient IComponent container;

	public NoSuchComponentException(String componentId, IComponent container)
	{
		super(
			Tapestry.getString(
				"NoSuchComponentException.message",
				container.getExtendedId(),
				componentId));

		this.componentId = componentId;
		this.container = container;
	}

	public IComponent getContainer()
	{
		return container;
	}

	public String getComponentId()
	{
		return componentId;
	}
}