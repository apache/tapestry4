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

package net.sf.tapestry.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *  A kind of super-formatter.  It is sent a stream of binary data and
 *  formats it in a human-readable dump format which is forwarded to
 *  its output stream.
 *
 * <p>Currently, output is in hex though options to change that may
 * be introduced.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class BinaryDumpOutputStream extends OutputStream
{
    private PrintWriter out;

    private boolean locked = false;

    private boolean showOffset = true;
    private int bytesPerLine = 16;
    private int spacingInterval = 4;
    private char substituteChar = '.';
    private String offsetSeperator = ": ";
    private int offset = 0;
    private int lineCount = 0;
    private int bytesSinceSpace = 0;
    private char[] ascii = null;
    private boolean showAscii = true;
    private String asciiBegin = "  |";
    private String asciiEnd = "|";

    private static final char[] HEX =
        {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f' };

    /**
     *  Creates a <code>PrintWriter</code> for <code>System.out</code>.
     *
     **/

    public BinaryDumpOutputStream()
    {
        this(new PrintWriter(System.out, true));
    }

    public BinaryDumpOutputStream(PrintWriter out)
    {
        this.out = out;
    }

    public BinaryDumpOutputStream(Writer out)
    {
        this.out = new PrintWriter(out);
    }

    public void close() throws IOException
    {
        if (out != null)
        {
            if (lineCount > 0)
                finishFinalLine();

            out.close();
        }

        out = null;
    }

    private void finishFinalLine()
    {
        // Since we only finish the final line after at least one byte has
        // been written to it, we don't need to worry about
        // the offset.

        while (lineCount < bytesPerLine)
        {
            // After every <n> bytes, emit a space.

            if (spacingInterval > 0 && bytesSinceSpace == spacingInterval)
            {
                out.print(' ');
                bytesSinceSpace = 0;
            }

            // Two spaces to substitute for the two hex digits.

            out.print("  ");

            if (showAscii)
                ascii[lineCount] = ' ';

            lineCount++;
            bytesSinceSpace++;
        }

        if (showAscii)
        {
            out.print(asciiBegin);
            out.print(ascii);
            out.print(asciiEnd);
        }

        out.println();
    }

    /**
     *  Forwards the <code>flush()</code> to the <code>PrintWriter</code>.
     *
     **/

    public void flush() throws IOException
    {
        out.flush();
    }

    public String getAsciiBegin()
    {
        return asciiBegin;
    }

    public String getAsciiEnd()
    {
        return asciiEnd;
    }

    public int getBytesPerLine()
    {
        return bytesPerLine;
    }

    public String getOffsetSeperator()
    {
        return offsetSeperator;
    }

    public boolean getShowAscii()
    {
        return showAscii;
    }

    public char getSubstituteChar()
    {
        return substituteChar;
    }

    public void setAsciiBegin(String value)
    {
        if (locked)
            throw new IllegalStateException();

        asciiBegin = value;
    }

    public void setAsciiEnd(String value)
    {
        if (locked)
            throw new IllegalStateException();

        asciiEnd = value;
    }

    public void setBytesPerLine(int value)
    {
        if (locked)
            throw new IllegalStateException();

        bytesPerLine = value;

        ascii = null;
    }

    public void setOffsetSeperator(String value)
    {
        if (locked)
            throw new IllegalStateException();

        offsetSeperator = value;
    }

    public void setShowAscii(boolean value)
    {
        if (locked)
            throw new IllegalStateException();

        showAscii = value;
    }

    /**
     *  Sets the character used in the ASCII dump that substitutes for characters
     *  outside the range of 32..126.
     *
     **/

    public void setSubstituteChar(char value)
    {
        if (locked)
            throw new IllegalStateException();

        substituteChar = value;
    }

    public void write(int b) throws IOException
    {
        char letter;

        if (showAscii && ascii == null)
            ascii = new char[bytesPerLine];

        // Prevent further customization after output starts being written.

        locked = true;

        if (lineCount == bytesPerLine)
        {
            if (showAscii)
            {
                out.print(asciiBegin);
                out.print(ascii);
                out.print(asciiEnd);
            }

            out.println();

            bytesSinceSpace = 0;
            lineCount = 0;
            offset += bytesPerLine;
        }

        if (lineCount == 0 && showOffset)
        {
            writeHex(offset, 4);
            out.print(offsetSeperator);
        }

        // After every <n> bytes, emit a space.

        if (spacingInterval > 0 && bytesSinceSpace == spacingInterval)
        {
            out.print(' ');
            bytesSinceSpace = 0;
        }

        writeHex(b, 2);

        if (showAscii)
        {
            if (b < 32 | b > 127)
                letter = substituteChar;
            else
                letter = (char) b;

            ascii[lineCount] = letter;
        }

        lineCount++;
        bytesSinceSpace++;
    }

    private void writeHex(int value, int digits)
    {
        int i;
        int nybble;

        for (i = 0; i < digits; i++)
        {
            nybble = (value >> 4 * (digits - i - 1)) & 0x0f;

            out.print(HEX[nybble]);
        }
    }
}