// Copyright 2004, 2005 The Apache Software Foundation
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
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Represents static text in the template that may be passed through to the client unchanged
 * (except, perhaps, for the removal of some whitespace).
 * 
 * @see TokenType#TEXT
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class TextToken extends TemplateToken implements IRender
{
    private char[] _templateData;

    private int _offset;

    private int _length;

    public TextToken(char[] templateData, int startIndex, int endIndex, Location location)
    {
        super(TokenType.TEXT, location);

        if (startIndex < 0 || endIndex < 0 || startIndex > templateData.length
                || endIndex > templateData.length)
            throw new ApplicationRuntimeException(ParseMessages.rangeError(
                    this,
                    templateData.length), this, getLocation(), null);

        _templateData = templateData;

        _offset = startIndex;
        _length = endIndex - startIndex + 1;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_length == 0)
            return;

        // At one time, we would check to see if the cycle was rewinding and
        // only invoke printRaw() if it was. However, that slows down
        // normal rendering (microscopically) and, with the new
        // NullResponseWriter class, the "cost" of invoking cycle.isRewinding()
        // is approximately the same as the "cost" of invoking writer.printRaw().

        writer.printRaw(_templateData, _offset, _length);
    }

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("offset", _offset);
        builder.append("length", _length);
    }

    public String getTemplateDataAsString()
    {
        return new String(_templateData, _offset, _length);
    }

    public int getLength()
    {
        return _length;
    }

    public int getOffset()
    {
        return _offset;
    }
}