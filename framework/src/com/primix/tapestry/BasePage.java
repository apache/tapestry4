/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry;

import com.primix.tapestry.event.ChangeObserver;
import com.primix.tapestry.spec.*;
import java.util.*;
import java.io.OutputStream;
import javax.servlet.http.*;
import com.primix.tapestry.util.*;

/**
 * Concrete class for HTML pages. Most pages
 * should be able to simply subclass this, adding new properties and
 * methods.  An unlikely exception would be a page that was not based
 * on a template.
 *
 * @version $Id$
 * @author Howard Ship
 */

public class BasePage extends AbstractPage
{
	/**
	*  Returns a new {@link HTMLResponseWriter}.
	*
	*/

	public IResponseWriter getResponseWriter(OutputStream out)
	{
		return new HTMLResponseWriter(out);
	}
}