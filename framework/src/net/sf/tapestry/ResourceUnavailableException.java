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
 *  Exception thrown when a resource is not available, because it is not
 *  found, or could not be used.  The latter case occurs with files which
 *  must be parsed or processed once located, such as component specification
 *  files.
 *
 *  @deprecated To be removed in 2.3.  No longer used; uses converted
 *  to {@link net.sf.tapestry.ApplicationRuntimeException}.
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ResourceUnavailableException extends Exception
{
	private Throwable rootCause;

	/**
	*  Constructor when no underlying exception is known.
	*
	*  @param message Describes the resource and the reason it is unavailable.
	*
	*/

	public ResourceUnavailableException(String message)
	{
		super(message);

	}

	/**
	*  Standard constructor
	*
	*  @param message Describes the resource and the reason it is unavailable.
	*  @param rootCause Exception which made the resource unavailable.
	*/

	public ResourceUnavailableException(String message, Throwable rootCause)
	{
		super(message);
		this.rootCause = rootCause;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}