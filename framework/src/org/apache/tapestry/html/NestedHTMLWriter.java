//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.html;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.tapestry.IMarkupWriter;

/**
 *  Subclass of {@link HTMLWriter} that is nested.  A nested writer
 *  buffers its output, then inserts it into its parent writer when it is
 *  closed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class NestedHTMLWriter extends HTMLWriter
{
	private IMarkupWriter _parent;
	private CharArrayWriter _internalBuffer;

	public NestedHTMLWriter(IMarkupWriter parent)
	{
		super(parent.getContentType());

		_parent = parent;

		_internalBuffer = new CharArrayWriter();

		setWriter(new PrintWriter(_internalBuffer));
	}

	/**
	*  Invokes the {@link HTMLWriter#close() super-class
	*  implementation}, then gets the data accumulated in the
	*  internal buffer and provides it to the containing writer using
	*  {@link IMarkupWriter#printRaw(char[], int, int)}.
	*
	*/

	public void close()
	{
		super.close();

		char[] data = _internalBuffer.toCharArray();

		_parent.printRaw(data, 0, data.length);

		_internalBuffer = null;
		_parent = null;
	}
}