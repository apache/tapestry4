/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship 
 *
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
package tutorial.workbench.chart;

import java.io.Serializable;

/**
 *  An single point of data in the plot.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 *
 */

public class PlotValue implements Serializable
{
	private String name;
	private int value;
	
	public PlotValue()
	{
	}
	
	public PlotValue(String name, int value)
	{
		this.name = name;
		this.value = value;
	}
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("PlotValue@");
		buffer.append(Integer.toHexString(hashCode()));
		buffer.append('[');
		buffer.append(name);
		buffer.append(' ');
		buffer.append(value);
		buffer.append(']');
		
		return buffer.toString();
	}
}
