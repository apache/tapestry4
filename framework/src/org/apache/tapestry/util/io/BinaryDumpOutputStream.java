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

package org.apache.tapestry.util.io;

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