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

package tutorial.workbench;

import java.io.*;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 */

public class Visit implements Serializable
{
	/**
	 *  The name of the page for which the corresponding tab
	 *  should be visibly active.
	 *
	 */

	private String activeTabName = "Home";

	/**
	 *  If true, then a detailed report about the request is appended
	 *  to the bottom of each page.
	 *
	 */

	private boolean requestDebug;

	/**
	 *  Used by the Fields demo page.
	 *
	 */

	private Integer intValue;

	public String getActiveTabName()
	{
		return activeTabName;
	}

	public void setActiveTabName(String value)
	{
		activeTabName = value;
	}

	public void setRequestDebug(boolean value)
	{
		requestDebug = value;
	}

	public boolean getRequestDebug()
	{
		return requestDebug;
	}

	public void setIntValue(Integer value)
	{
		intValue = value;
	}

	public Integer getIntValue()
	{
		return intValue;
	}
}