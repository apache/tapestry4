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

package org.apache.tapestry.util.text;

import java.io.IOException;
import java.io.Reader;

/**
 * A Reader that provides some additional functionality, such as peek().
 * 
 * @author mb
 * @since 3.1
 */
public class ExtendedReader extends Reader
{
    private Reader _reader;
    private boolean _hasBufferedChar = false;
    private char _bufferedChar;
    
    /**
     * Creates a new extended reader that reads from the provided object
     * 
     * @param in the Reader to get data from
     */
    public ExtendedReader(Reader in)
    {
        _reader = in;
    }

    /**
     * Returns the next character in the stream without actually comitting the read.
     * Multiple consequtive invocations of this method should return the same value.
     * 
     * @return the next character waiting in the stream or -1 if the end of the stream is reached
     * @throws IOException if an error occurs
     */
    public synchronized int peek() throws IOException
    {
        if (!_hasBufferedChar) {
            int bufferedChar = read();
            if (bufferedChar < 0)
                return bufferedChar;
            _bufferedChar = (char) bufferedChar;
            _hasBufferedChar = true;
        }
        return _bufferedChar;
    }
    
    /**
     * Determines whether the end of the stream is reached
     * 
     * @return true if at the end of stream
     * @throws IOException if an error occurs
     */
    public synchronized boolean isEndOfStream() throws IOException
    {
        return peek() < 0;
    }

    /**
     * Skips the next characters until a character that does not match the provided rule is reached.
     * 
     * @param matcher the object determining whether a character should be skipped
     * @throws IOException if an error occurs
     */
    public synchronized void skipCharacters(ICharacterMatcher matcher) throws IOException
    {
        while (true) {
            if (isEndOfStream())
                break;
            char ch = (char) peek();
            if (!matcher.matches(ch))
                break;
            read();
        }
    }
    
    /**
     * Reads the next characters until a character that does not match the provided rule is reached.
     * 
     * @param matcher the object determining whether a character should be read
     * @return the string of characters read
     * @throws IOException if an error occurs
     */
    public synchronized String readCharacters(ICharacterMatcher matcher) throws IOException
    {
        StringBuffer buf = new StringBuffer();
        while (true) {
            if (isEndOfStream())
                break;
            char ch = (char) peek();
            if (!matcher.matches(ch))
                break;
            buf.append(read());
        }
        return buf.toString();
    }
    
    /** 
     * @see java.io.FilterReader#read(char[], int, int)
     */
    public synchronized int read(char[] cbuf, int off, int len) throws IOException
    {
        if (len <= 0)
            return 0;
        
        boolean extraChar = _hasBufferedChar;
        if (_hasBufferedChar) {
            _hasBufferedChar = false;
            cbuf[off++] = _bufferedChar;
            len--;
        }

        int read = _reader.read(cbuf, off, len);
        if (extraChar)
            read++;
        return read;
    }
    
    /** 
     * @see java.io.FilterReader#ready()
     */
    public synchronized boolean ready() throws IOException
    {
        if (_hasBufferedChar)
            return true;
        return _reader.ready();
    }
    
    /** 
     * @see java.io.FilterReader#markSupported()
     */
    public synchronized boolean markSupported()
    {
        return false;
    }
    
    /** 
     * @see java.io.FilterReader#reset()
     */
    public synchronized void reset() throws IOException
    {
        _hasBufferedChar = false;
        _reader.reset();
    }
    
    /** 
     * @see java.io.FilterReader#skip(long)
     */
    public synchronized long skip(long n) throws IOException
    {
        if (_hasBufferedChar && n > 0) {
            _hasBufferedChar = false;
            n--;
        }
        return _reader.skip(n);
    }

    /** 
     * @see java.io.Reader#close()
     */
    public synchronized void close() throws IOException
    {
        _hasBufferedChar = false;
        _reader.close();
    }
    
}
