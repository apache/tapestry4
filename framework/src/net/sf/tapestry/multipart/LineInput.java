package net.sf.tapestry.multipart;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  Allows an input stream to be read as both individual lines
 *  (ala {@link java.io.LineNumberReader}) and as binary.
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