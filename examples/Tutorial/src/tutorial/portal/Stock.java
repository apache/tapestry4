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

import java.io.*;
import com.primix.tapestry.util.prop.*;

/**
 *  Stores information about a single Stock.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class Stock implements Serializable, IPublicBean
{
	public String tickerId;
	public double price;
	public double change;

	public Stock()
	{
	}

	public Stock(String tickerId, double price, double change)
	{
		this.tickerId = tickerId;
		this.price = price;
		this.change = change;
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("Stock[");
		buffer.append(tickerId);
		buffer.append(' ');
		buffer.append(price);
		buffer.append(' ');
		buffer.append(change);
		buffer.append(']');

		return buffer.toString();
	}
}