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

package org.apache.tapestry.junit.mock.c13;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.html.BasePage;

/**
 *  Page used to test internal and external redirects.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Home extends BasePage
{
	public void internalRedirect(IRequestCycle cycle)
	{
		throw new RedirectException("static/Warning.html");
	}
	
	public void externalRedirect(IRequestCycle cycle)
	{
		throw new RedirectException("http://jakarta.apache.org/tapestry");
	}
	
	public void nullRedirect(IRequestCycle cycle)
	{
		// Special value, stands in for any resource that can't be found
		// resulting in the ServletContext returning null for the
		// dispatcher.
		
		throw new RedirectException("NULL");
	}

	public void servletException(IRequestCycle cycle)
	{
		// Special value, forces a ServletException to be thrown
		// from within RequestDispatcher.forward()
				
		throw new RedirectException("FAIL_SERVLET");
	}
	
	public void missing(IRequestCycle cycle)
	{
		// Specifying a missing file is an easy way to
		// force an IOException ... note that the
		// real Servlet API will return a null
		// RequestDispatcher for a resource that
		// doesn't exist.
		
		throw new RedirectException("Missing.html");
	}
	
	public void index(IRequestCycle cycle)
	{
		throw new RedirectException(null);
	}
	
	public void failExternal(IRequestCycle cycle)
	{
		throw new RedirectException("http://somehost/FAIL_IO");
	}
}
