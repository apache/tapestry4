/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.parse;

import org.apache.commons.hivemind.Location;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Represents static text in the template that may be passed through
 *  to the client unchanged (except, perhaps, for the removal of
 *  some whitespace).
 *
 *  @see TokenType#TEXT
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TextToken extends TemplateToken implements IRender
{
    private char[] _templateData;

    private int _startIndex = -1;
    private int _endIndex = -1;

    private int _offset;
    private int _length;
    private boolean _needsTrim = true;

    public TextToken(char[] templateData, int startIndex, int endIndex, Location location)
    {
        super(TokenType.TEXT, location);

        if (startIndex < 0
            || endIndex < 0
            || startIndex > templateData.length
            || endIndex > templateData.length)
            throw new IllegalArgumentException(
                Tapestry.format(
                    "TextToken.range-error",
                    this,
                    Integer.toString(templateData.length)));

        _templateData = templateData;
        _startIndex = startIndex;
        _endIndex = endIndex;

        // Values actually used to render, may be adjusted to remove excess
        // leading and trailing whitespace.

        _offset = startIndex;
        _length = endIndex - startIndex + 1;
    }

    public synchronized void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_needsTrim)
        {
            trim();
            _needsTrim = false;
        }

        if (_length == 0)
            return;

        // At one time, we would check to see if the cycle was rewinding and
        // only invoke printRaw() if it was.  However, that slows down
        // normal rendering (microscopically) and, with the new
        // NullResponseWriter class, the "cost" of invoking cycle.isRewinding()
        // is approximately the same as the "cost" of invoking writer.printRaw().

        writer.printRaw(_templateData, _offset, _length);
    }

    /**
      *  Strip off all leading and trailing whitespace by adjusting offset and length.
      *
      **/

    private void trim()
    {
        if (_length == 0)
            return;

        try
        {
            boolean didTrim = false;

            // Shave characters off the end until we hit a non-whitespace
            // character.

            while (_length > 0)
            {
                char ch = _templateData[_offset + _length - 1];

                if (!Character.isWhitespace(ch))
                    break;

                _length--;
                didTrim = true;
            }

            // Restore one character of whitespace to the end

            if (didTrim)
                _length++;

            didTrim = false;

            // Strip characters off the front until we hit a non-whitespace
            // character.

            while (_length > 0)
            {
                char ch = _templateData[_offset];

                if (!Character.isWhitespace(ch))
                    break;

                _offset++;
                _length--;
                didTrim = true;
            }

            // Again, restore one character of whitespace.

            if (didTrim)
            {
                _offset--;
                _length++;
            }

        }
        catch (IndexOutOfBoundsException ex)
        {
            throw new RuntimeException(Tapestry.format("TextToken.error-trimming", this));
        }

        // Ok, this isn't perfect.  I don't want to write into templateData[] even
        // though I'd prefer that my single character of whitespace was always a space.
        // It would also be kind of neat to shave whitespace within the static HTML, rather
        // than just on the edges.
    }

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("startIndex", _startIndex);
        builder.append("endIndex", _endIndex);
    }

    public int getEndIndex()
    {
        return _endIndex;
    }

    public int getStartIndex()
    {
        return _startIndex;
    }

    public char[] getTemplateData()
    {
        return _templateData;
    }

}
