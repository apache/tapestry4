/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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