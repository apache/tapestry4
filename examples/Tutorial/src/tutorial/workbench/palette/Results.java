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

package tutorial.workbench.palette;

import com.primix.tapestry.*;

import net.sf.tapestry.*;
import net.sf.tapestry.html.*;

import java.util.*;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 */

public class Results extends BasePage
{
	private List selectedColors;

	public void detach()
	{
		selectedColors = null;

		super.detach();
	}

	public void setSelectedColors(List value)
	{
		selectedColors = value;
	}

	public List getSelectedColors()
	{
		return selectedColors;
	}

}