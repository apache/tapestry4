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

package net.sf.tapestry.multipart;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  Allows an input stream to be read as both individual lines
 *  (ala {@link LineInputReader} and as binary.
 * 
 *  <p>This is not nearly as good as the one inside Jetty; that
 *  will have to come over time.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class LineInput extends FilterInputStream
{
    public static final int EOF = -1;
    public static final int CR = 13;
    public static final int LF = 10;

    private static class ByteBuffer
    {
        private int length = 0;
        private byte[] buffer = new byte[100];

        public int getLength()
        {
            return length;
        }

        public byte[] getBuffer()
        {
            return buffer;
        }

        public void add(byte b)
        {
            if (length == buffer.length)
            {
                byte[] newBuffer = new byte[length * 2];
                System.arraycopy(buffer, 0, newBuffer, 0, length);
                buffer = newBuffer;
            }

            buffer[length++] = b;
        }

        public String getString()
        {
            return new String(buffer, 0, length);
        }
    }

    protected LineInput(InputStream in)
    {
        super(in);
    }

    public String readLine() throws IOException
    {
        ByteBuffer buffer = new ByteBuffer();

        while (true)
        {
            int b = read();

            if (b == EOF)
                break;

            if (b == CR)
            {
                // Skip the LF that follows
                read();
                break;
            }

            if (b == LF)
                break;

            buffer.add((byte) b);
        }

        return buffer.getString();
    }

}