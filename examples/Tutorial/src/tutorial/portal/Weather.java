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

package tutorial.portal;

import com.primix.tapestry.*;

/**
 *  This is much simpler than the others, and mostly is here to demonstrate
 *  how to include {@link com.primix.tapestry.link.Direct} links in a Portlet
 *  content.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Weather extends BasePage
{
	private boolean weekend = false;

	public void detach()
	{
		weekend = false;

		super.detach();
	}

	public void setWeekend(boolean value)
	{
		weekend = value;
		fireObservedChange("weekend", value);
	}

	public boolean isWeekend()
	{
		return weekend;
	}

	public String getForecast()
	{
		if (weekend)
			return "Dismal, pelting rain, 47 - 52.  Enjoy your time off.";

		return "Sunny, bright, 76 - 82.  Now, get back to your cube.";
	}

	public void selectWeekend(IRequestCycle cycle)
	{
		setWeekend(true);
	}

	public void selectWeekday(IRequestCycle cycle)
	{
		setWeekend(false);
	}

	public String getWeekdayClass()
	{
		return weekend ? null : "selected";
	}

	public String getWeekendClass()
	{
		return weekend ? "selected" : null;
	}
}