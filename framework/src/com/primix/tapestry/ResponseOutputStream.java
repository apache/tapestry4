package com.primix.tapestry;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.primix.tapestry.components.Body;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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

/**
 *  A special output stream works with a {@link HttpServletResponse}, buffering
 *  data so as to defer opening the response's output stream.
 *
 *  <p>A <code>ResponseOutputStream</code> must be closed specially, using
 *  {@link #forceClose()}.  Otherwise, it ignores {@link #close()}, because
 *  of unwanted chaining of <code>close()</code> from outer writers and streams.
 *
 *  <p>The buffering is pretty simple because the code
 *  between {@link IResponseWriter} and this shows lots of buffering
 *  after the <code>PrintWriter</code> and inside the <code>OutputStreamWriter</code> that
 *  can't be configured.
 *
 * <p>Possibly, this needs to be rethunk.  Perhaps we need a <code>ResponseWriter</code>
 * class instead that could trap the output characters before they get into the PrintWriter
 * and its crew.  This would save on the number of conversions between characters and
 * bytes.
 *
 * <p>In fact, this is now even less useful, because the {@link Body} component (which will
 * be used on virtually all Tapestry pages), buffers its wrapped contents.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ResponseOutputStream extends OutputStream
{
	/**
	*  Default size for the buffer (2000 bytes).
	*
	*/

	public static final int DEFAULT_SIZE = 2000;

	private int pos;
	private int maxSize;
	private byte[] buffer;

	private String contentType;
	private HttpServletResponse response;
	private OutputStream out;

	private byte tiny[];

	private boolean discard = false;

	/**
	*  Creates the stream with the default maximum buffer size.
	*
	*/

	public ResponseOutputStream(HttpServletResponse response)
	{
		this(response, DEFAULT_SIZE);
	}

	/**
	*  Standard constructor.
	*
	*/

	public ResponseOutputStream(HttpServletResponse response,int maxSize)
	{
		this.response = response;

		this.maxSize = maxSize;
	}

	/**
	*  Does nothing.  This is because of chaining of <code>close()</code> from
	*  {@link IResponseWriter#close()} ... see {@link #flush()}.
	*/

	public void close() throws IOException
	{
		// Does nothing.
	}

	/**
	*  Flushes the underlying output stream, if is has been opened.  
	*
	*  <p>This method explicitly <em>does not</em> flush the internal buffer ...
	*  that's because when an {@link IResponseWriter} is closed (for instance, because
	*  an exception is thrown), that <code>close()</code> spawns <code>flush()</code>es
	*  and <code>close()</code>s throughout the output stream chain, eventually
	*  reaching this method.
	*
	*  @see #forceFlush()
	*  @see #forceClose()
	*/

	public void flush() throws IOException
	{
		if (out != null)
			out.flush();
	}

	/**
	*  Flushes the internal buffer to the underlying output stream, then closes
	*  the underlying output stream.  If the output stream has not yet been opened
	*  (meaning the entire response page is in the internal buffer), then
	*  <code>setContentLength()</code> is invoked on the <code>HttpServletResponse</code>
    * ... this  allows the web server and client to use Keep-Alive connections.
    *
    * <p>In rare instances (such as sending a HTTP redirect), 
    * the <code>ReponseOutputStream</code> is closed
    * when no data has been written to it.  In that case, we never 
    * invoke <code>HttpServletResponse.getOutputStream()</code>.
	*
	*/

	public void forceClose()
	throws IOException
	{
		// Invoke open() now to write the current buffer to the output stream.

		if (out == null)
		{
            // If we never got any output to send, fold with a wimper.
            
			if (pos == 0)
                return;
            
            response.setContentLength(pos);

			open();
		}

		// And close it.

		out.close();
	}

	/**
	*  Writes the internal buffer to the output stream, opening it if necessary, then
	*  flushes the output stream.  Future writes will go directly to the output stream.
	*
	*/

	public void forceFlush()
	throws IOException
	{
		if (out == null)
			open();

		out.flush();
	}

	public String getContentType()
	{
		return contentType;
	}

	public boolean getDiscard()
	{
		return discard;
	}

	/**
	*  Sets the response type to from the contentType property (which
	*  defaults to "text/html") and gets an output stream
	*  from the response, then writes the current buffer to it and
	*  releases the buffer.
    *
    *  @throws IOException if the content type has never been set.
	*
	*/

	private void open()
	throws IOException
	{
		if (contentType == null)
			throw new IOException("Content type of response never set.");
			
		response.setContentType(contentType);

		out = response.getOutputStream();

		if (buffer != null && pos > 0)
			out.write(buffer, 0, pos);

		pos = 0;
		buffer = null;
	}

	/**
	*  Discards all output in the buffer.  This is used after an error to
	*  restart the output (so that the error may be presented).
	*
	*  <p>Clears the discard flag.
	*
	*/

	public void reset()
	throws IOException
	{
		pos = 0;

		discard = false;
	}
	/**
	*  Changes the maximum buffer size.  If the new buffer size is smaller \
	*  than the number of
	*  bytes already in the buffer, the buffer is immediately flushed.
	*
	*/

	public void setBufferSize(int value)
	throws IOException
	{
		if (value < pos)
		{
			open();
			return;
		}

		maxSize = value;
	}

	public void setContentType(String value)
	{
		contentType = value;
	}

	/**
	*  Indicates whether the stream should ignore all data written to it.
	*
	*/

	public void setDiscard(boolean value)
	{
		discard = value;
	}

	public void write(byte b[], int off, int len) throws IOException
	{
		int i;
		int newSize;
		byte[] newBuffer;

		if (len == 0 || discard)
			return;

		if (out != null)
		{	 
			out.write(b, off, len);
			return;
		}

		// If too large for the maximum size buffer, then open the output stream
		// write out and free the buffer, and write out the new stuff.

		if (pos + len >= maxSize)
		{
			open();
			out.write(b, off, len);
			return;
		}

		// Allocate the buffer when it is initially needed.

		if (buffer == null)
			buffer = new byte[maxSize];

		// Copy the new bytes into the buffer and advance the position.

		System.arraycopy(b, off, buffer, pos, len);
		pos += len;
	}

	public void write(int b) throws IOException
	{
		if (discard)
			return;

		// This method is rarely called so this little inefficiency is better than
		// maintaining that ugly buffer expansion code in two places.

		if (tiny == null)
			tiny = new byte[1];

			tiny[0] = (byte)b;

		write(tiny, 0, 1);
	}
}

