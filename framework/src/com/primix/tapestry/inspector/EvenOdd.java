/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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


package com.primix.tapestry.inspector;

import com.primix.tapestry.util.pool.*;

/**
 *  Used to emit a stream of alteranting string values: "even", "odd", etc.  This
 *  is often used in the Inspector pages to make the class of a &lt;tr&gt; alternate
 *  for presentation reasons.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class EvenOdd implements IPoolable
{
	private boolean even;
	
	/**
	 *  Default constructor.  Configure to initially return "even".
	 *
	 */
	
	public EvenOdd()
	{
		even = true;
	}
	
	public EvenOdd(boolean initiallyEven)
	{
		even = initiallyEven;
	}
	
	/**
	 *  Returns "even" or "odd".  Whatever it returns on one invocation, it will
	 *  return the opposite on the next.
	 *
	 */
	
	public String getNext()
	{
		String result = even ? "even" : "odd";
		
		even = !even;
		
		return result;
	}
	
	public boolean isEven()
	{
		return even;
	}
	
	public void setEven(boolean value)
	{
		even = value;
	}
	
	/**
	 *  Resets the internal flag such that the next value from {@link #getNext()} will be "even".
	 *
	 */
	
	public void resetForPool()
	{
		even = true;
	}
}
