//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A wrapper around {@link String} that allows the String to
 *  be renderred.  This is primarily used to present
 *  error messages.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class RenderString implements IRender
{
	private String string;
	private boolean raw = false;
	
	public RenderString(String string)
	{
		this.string = string;
	}
	
	/**
	 *  @param string the string to render
	 *  @param raw if true, the String is rendered as-is, with no filtering.
	 *  If false (the default), the String is filtered.
	 *
	 **/
	
	public RenderString(String string, boolean raw)
	{
		this.string = string;
		this.raw = raw;
	}

	/**
	 *  Renders the String to the writer.  Does nothing if the string is null.
	 *  If raw is true, uses {@link IMarkupWriter#printRaw(String)}, otherwise
	 *  {@link IMarkupWriter#print(String)}.
	 * 
	 *
	 **/
	
	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		if (string == null)
			return;
			
		if (raw)
			writer.printRaw(string);
		else
			writer.print(string);
	}
	
	public String getString()
	{
		return string;
	}
	
	public boolean isRaw()
	{
		return raw;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("RenderString@");
		
		buffer.append(Integer.toHexString(hashCode()));
		buffer.append('[');
		buffer.append(string);
		
		if (raw)
		buffer.append(" (raw)");
		
		buffer.append(']');
		
		return buffer.toString();
	}
		

}
