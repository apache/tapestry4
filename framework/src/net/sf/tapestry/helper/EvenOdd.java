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

package net.sf.tapestry.helper;

/**
 *  Used to emit a stream of alteranting string values: "even", "odd", etc.  This
 *  is often used in the Inspector pages to make the class of a &lt;tr&gt; alternate
 *  for presentation reasons.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class EvenOdd
{
    private boolean even = true;

    /**
     *  Returns "even" or "odd".  Whatever it returns on one invocation, it will
     *  return the opposite on the next.  By default, the first value
     *  returned is "even".
     *
     **/

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

	/**
	 *  Overrides the even flag.
	 * 
	 **/
	
    public void setEven(boolean value)
    {
        even = value;
    }
}