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
 *  Exception thrown when an {@link IComponent} is unable to render.  Often, there
 *  is an underlying exception.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public class RequestCycleException extends Exception
{
	private transient IComponent component;
	private Throwable rootCause;

	public RequestCycleException()
	{
		super();
	}

	public RequestCycleException(String message)
	{
		super(message);
	}

	public RequestCycleException(String message, IComponent component)
	{
		this(message, component, null);
	}

	public RequestCycleException(
		String message,
		IComponent component,
		Throwable rootCause)
	{
		super(message);

		this.component = component;

		this.rootCause = rootCause;
	}

	/**
	 *  @since 0.2.9
	 *
	 **/

	public RequestCycleException(IComponent component, Throwable rootCause)
	{
		this(rootCause.getMessage(), component, rootCause);
	}

	public IComponent getComponent()
	{
		return component;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}