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

package org.apache.tapestry.request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.Tapestry;

/**
 *  A special output stream works with a {@link HttpServletResponse}, buffering
 *  data so as to defer opening the response's output stream.
 *
 *  <p>The buffering is pretty simple because the code
 *  between {@link org.apache.tapestry.IMarkupWriter} and this shows lots of buffering
 *  after the <code>PrintWriter</code> and inside the <code>OutputStreamWriter</code> that
 *  can't be configured.
 *
 *  <p>This class performs some buffering, but it is not all that
 *  useful because the 
 *  {@link org.apache.tapestry.html.Body} component (which will
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
     *  {@link org.apache.tapestry.IMarkupWriter#close()} ... see {@link #flush()}.
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
     *  that's because when an {@link org.apache.tapestry.IMarkupWriter} is closed (for instance, because
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
            throw new IOException(Tapestry.getMessage("ResponseOutputStream.content-type-not-set"));

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