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

package org.apache.tapestry;

/**
 *  Exception thrown to force a redirection to an arbitrary location.
 *  This is used when, after processing a request (such as a form
 *  submission or a link being clicked), it is desirable to go
 *  to some arbitrary new location.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 **/

public class RedirectException extends ApplicationRuntimeException
{
	private String _redirectLocation;

	public RedirectException(String redirectLocation)
	{
		this(null, redirectLocation);
	}

	/** 
	 *  @param message A message describing why the redirection is taking place.
	 *  @param redirectLocation The location to redirect to, may be a relative path (relative
	 *  to the {@link javax.servlet.ServletContext}).
	 *
	 *  @see javax.servlet.http.HttpServletResponse#sendRedirect(String)
	 *  @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)
	 *
	 **/

	public RedirectException(String message, String redirectLocation)
	{
		super(message);

		_redirectLocation = redirectLocation;
	}

	public String getRedirectLocation()
	{
		return _redirectLocation;
	}
}