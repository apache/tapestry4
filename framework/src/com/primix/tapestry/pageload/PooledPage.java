package com.primix.tapestry.pageload;

import com.primix.tapestry.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Holds a reference to a previously loaded page that was returned to the
 *  page pool.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


class PooledPage
{
	/**
	*  The time at which this node was created.  This is useful for removing
	*  excess pages from the pool.
	*
	*/

	long created;

	/**
	*  The pooled page.
	*
	*/

	IPage page;

	/**
	*  The next pooled page in the list.
	*
	*/

	PooledPage next;

	PooledPage(IPage page, PooledPage next)
	{
		created = System.currentTimeMillis();

		this.page = page;
		this.next = next;
	}

	public long getCreated()
	{
		return created;
	}

	public PooledPage getNext()
	{
		return next;
	}

	public IPage getPage()
	{
		return page;
	}
}

