// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.markup;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.NestedMarkupWriter;

/**
 * Nested implementation of {@link org.apache.tapestry.IMarkupWriter}. Accumulates content in a
 * {@link java.io.CharArrayWriter}, and prints the buffered content (raw) on {@link #close()}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.IMarkupWriter#getNestedWriter()
 */
public class NestedMarkupWriterImpl extends MarkupWriterImpl implements NestedMarkupWriter
{
    private final IMarkupWriter _parent;

    private final CharArrayWriter _charArrayWriter;

    private boolean _closed;

    public String getBuffer()
    {
        return _charArrayWriter.toString();
    }

    public NestedMarkupWriterImpl(IMarkupWriter parent, MarkupFilter filter)
    {
        // Need to do this awkward double constructor because we want
        // to create an object and pass it to the parent constructor.
        // Java language rules get in the way here.

        this(parent, new CharArrayWriter(), filter);
    }

    private NestedMarkupWriterImpl(IMarkupWriter parent, CharArrayWriter writer, MarkupFilter filter)
    {
        super(parent.getContentType(), new PrintWriter(writer), filter);

        _parent = parent;
        _charArrayWriter = writer;
    }

    /**
     * Closes the internal {@link CharArrayWriter}, then captures its content and invokes
     * {@link org.apache.tapestry.IMarkupWriter#printRaw(String)}&nbsp;on the parent markup writer
     * (the writer that created this writer).
     */

    public void close()
    {
        if (_closed)
            throw new IllegalStateException(MarkupMessages.closeOnce());

        _closed = true;

        super.close();

        String content = getBuffer();

        _parent.printRaw(content);
    }
}