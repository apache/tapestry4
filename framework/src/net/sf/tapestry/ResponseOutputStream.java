/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  A special output stream works with a {@link HttpServletResponse}, buffering
 *  data so as to defer opening the response's output stream.
 *
 *  <p>The buffering is pretty simple because the code
 *  between {@link IMarkupWriter} and this shows lots of buffering
 *  after the <code>PrintWriter</code> and inside the <code>OutputStreamWriter</code> that
 *  can't be configured.
 *
 *  <p>This class performs some buffering, but it is not all that
 *  useful because the 
 *  {@link net.sf.tapestry.html.Body} component (which will
 *  be used on virtually all Tapestry pages), buffers its wrapped contents
 *  (that is, evertyhing inside the &lt;body&gt; tag in the generated HTML).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ResponseOutputStream extends OutputStream
{
    private static final Log LOG = LogFactory.getLog(ResponseOutputStream.class);

    /**
     *  Default size for the buffer (2000 bytes).
     *
     **/

    public static final int DEFAULT_SIZE = 2000;

    private int _pos;
    private int _maxSize;
    private byte[] _buffer;

    private String _contentType;
    private HttpServletResponse _response;
    private OutputStream _out;

    private boolean _discard = false;

    /**
     *  Creates the stream with the default maximum buffer size.
     *
     **/

    public ResponseOutputStream(HttpServletResponse response)
    {
        this(response, DEFAULT_SIZE);
    }

    /**
     *  Standard constructor.
     *
     **/

    public ResponseOutputStream(HttpServletResponse response, int maxSize)
    {
        _response = response;
        _maxSize = maxSize;
    }

    /**
     *  Does nothing.  This is because of chaining of <code>close()</code> from
     *  {@link IMarkupWriter#close()} ... see {@link #flush()}.
     * 
     **/

    public void close() throws IOException
    {
        // Does nothing.
    }

    /**
     *  Flushes the underlying output stream, if is has been opened.  
     *
     *  <p>This method explicitly <em>does not</em> flush the internal buffer ...
     *  that's because when an {@link IMarkupWriter} is closed (for instance, because
     *  an exception is thrown), that <code>close()</code> spawns <code>flush()</code>es
     *  and <code>close()</code>s throughout the output stream chain, eventually
     *  reaching this method.
     *
     *  @see #forceFlush()
     *
     **/

    public void flush() throws IOException
    {
        try
        {
            if (_out != null)
                _out.flush();
        }
        catch (SocketException ex)
        {
            LOG.debug("Socket exception.");
        }
    }

    /**
     *  Writes the internal buffer to the output stream, opening it if necessary, then
     *  flushes the output stream.  Future writes will go directly to the output stream.
     *
     **/

    public void forceFlush() throws IOException
    {
        if (_out == null)
        {

            // In certain cases (such as when the Tapestry service sends a redirect),
            // there is no output to send back (and no content type set).  In this
            // case, forceFlush() does nothing.

            if (_buffer == null)
                return;

            open();
        }

        try
        {
            _out.flush();
        }
        catch (SocketException ex)
        {
            LOG.debug("Socket exception.");
        }
    }

    public String getContentType()
    {
        return _contentType;
    }

    public boolean getDiscard()
    {
        return _discard;
    }

    /**
     *  Sets the response type to from the contentType property (which
     *  defaults to "text/html") and gets an output stream
     *  from the response, then writes the current buffer to it and
     *  releases the buffer.
     *
     *  @throws IOException if the content type has never been set.
     *
     **/

    private void open() throws IOException
    {
        if (_contentType == null)
            throw new IOException(Tapestry.getString("ResponseOutputStream.content-type-not-set"));

        _response.setContentType(_contentType);

        _out = _response.getOutputStream();

        innerWrite(_buffer, 0, _pos);

        _pos = 0;
        _buffer = null;
    }

    /**
     *  Discards all output in the buffer.  This is used after an error to
     *  restart the output (so that the error may be presented).
     *
     *  <p>Clears the discard flag.
     *
     **/

    public void reset() throws IOException
    {
        _pos = 0;
        _discard = false;
    }

    /**
     *  Changes the maximum buffer size.  If the new buffer size is smaller
     *  than the number of
     *  bytes already in the buffer, the buffer is immediately flushed.
     *
     **/

    public void setBufferSize(int value) throws IOException
    {
        if (value < _pos)
        {
            open();
            return;
        }

        _maxSize = value;
    }

    public void setContentType(String value)
    {
        _contentType = value;
    }

    /**
     *  Indicates whether the stream should ignore all data written to it.
     *
     **/

    public void setDiscard(boolean value)
    {
        _discard = value;
    }

    private void innerWrite(byte[] b, int off, int len) throws IOException
    {
        if (b == null || len == 0 || _discard)
            return;

        try
        {
            _out.write(b, off, len);
        }
        catch (SocketException ex)
        {
            LOG.debug("Socket exception.");
        }
    }

    public void write(byte b[], int off, int len) throws IOException
    {
        if (len == 0 || _discard)
            return;

        if (_out != null)
        {
            _out.write(b, off, len);
            return;
        }

        // If too large for the maximum size buffer, then open the output stream
        // write out and free the buffer, and write out the new stuff.

        if (_pos + len >= _maxSize)
        {
            open();
            innerWrite(b, off, len);
            return;
        }

        // Allocate the buffer when it is initially needed.

        if (_buffer == null)
            _buffer = new byte[_maxSize];

        // Copy the new bytes into the buffer and advance the position.

        System.arraycopy(b, off, _buffer, _pos, len);
        _pos += len;
    }

    public void write(int b) throws IOException
    {
        if (_discard)
            return;

        // This method is rarely called so this little inefficiency is better than
        // maintaining that ugly buffer expansion code in two places.

        byte[] tiny = new byte[] {(byte) b };

        write(tiny, 0, 1);
    }
}