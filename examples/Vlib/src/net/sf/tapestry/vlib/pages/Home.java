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

package net.sf.tapestry.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;

import net.sf.tapestry.*;

import net.sf.tapestry.vlib.ejb.*;
import net.sf.tapestry.html.*;
import net.sf.tapestry.vlib.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;

/**
 *  The home page for the application, it's primary purpose is
 *  to provide a book search form.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class Home extends BasePage implements IErrorProperty
{
	private String searchTitle;
	private Object searchPublisherPK;
	private String error;
	private String message;
	private String searchAuthor;

	public void detach()
	{
		searchTitle = null;
		searchPublisherPK = null;
		error = null;
		message = null;
		searchAuthor = null;

		super.detach();
	}

	public String getSearchTitle()
	{
		return searchTitle;
	}

	public void setSearchTitle(String value)
	{
		searchTitle = value;
	}

	public String getSearchAuthor()
	{
		return searchAuthor;
	}

	public void setSearchAuthor(String value)
	{
		searchAuthor = value;
	}

	public Object getSearchPublisherPK()
	{
		return searchPublisherPK;
	}

	public void setSearchPublisherPK(Object value)
	{
		searchPublisherPK = value;
	}

	public void setError(String value)
	{
		error = value;
	}

	public String getError()
	{
		return error;
	}

	public void setMessage(String value)
	{
		message = value;
	}

	public String getMessage()
	{
		return message;
	}

	/**
	 *  Invokes {@link Matches#performQuery(String,String,Object,IRequestCycle)}.
	 *
	 */

	public void search(IRequestCycle cycle)
	{
		Matches matches;

		matches = (Matches) cycle.getPage("Matches");

		matches.performQuery(searchTitle, searchAuthor, searchPublisherPK, cycle);
	}

}