package net.sf.tapestry.html;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import net.sf.tapestry.IMarkupWriter;

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