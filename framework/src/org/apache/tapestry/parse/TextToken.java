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

package org.apache.tapestry.parse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.ILocation;
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

    public TextToken(char[] templateData, int startIndex, int endIndex, ILocation location)
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
