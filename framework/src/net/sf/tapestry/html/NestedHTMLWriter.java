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

package net.sf.tapestry.html;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import net.sf.tapestry.IMarkupWriter;

/**
 *  Subclass of {@link HTMLResponseWriter} that is nested.  A nested writer
 *  buffers its output, then inserts it into its parent writer when it is
 *  closed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class NestedHTMLWriter extends HTMLResponseWriter
{
	private IMarkupWriter parent;
	private CharArrayWriter internalBuffer;

	public NestedHTMLWriter(IMarkupWriter parent)
	{
		super(parent.getContentType());

		this.parent = parent;

		internalBuffer = new CharArrayWriter();

		writer = new PrintWriter(internalBuffer);
	}

	/**
	*  Invokes the {@link HTMLResponseWriter#close() super-class
	*  implementation}, then gets the data accumulated in the
	*  internal buffer and provides it to the containing writer using
	*  {@link IMarkupWriter#printRaw(char[], int, int)}.
	*
	*/

	public void close()
	{
		super.close();

		char[] data = internalBuffer.toCharArray();

		parent.printRaw(data, 0, data.length);

		internalBuffer = null;
		writer = null;
		parent = null;
	}
}