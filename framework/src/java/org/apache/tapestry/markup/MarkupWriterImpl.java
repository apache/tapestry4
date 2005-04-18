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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

/**
 * Completely revised (for 4.0) implementation of {@link org.apache.tapestry.IMarkupWriter}. No
 * longer does internal buffering (since the servlet/portlet APIs support that natively) and wraps
 * around a {@link java.io.PrintWriter}&nbsp;(rather than an {@link java.io.OutputStream}).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MarkupWriterImpl implements IMarkupWriter
{
    /**
     * The underlying {@link PrintWriter}that output is sent to.
     */

    private PrintWriter _writer;

    /**
     * Filter used to "escape" characters that need any kind of special encoding for the output
     * content type.
     */

    private MarkupFilter _filter;

    /**
     * Indicates whether a tag is open or not. A tag is opened by {@link #begin(String)}or
     * {@link #beginEmpty(String)}. It stays open while calls to the <code>attribute()</code>
     * methods are made. It is closed (the '&gt;' is written) when any other method is invoked.
     */

    private boolean _openTag = false;

    /**
     * Indicates that the tag was opened with {@link #beginEmpty(String)}, which affects how the
     * tag is closed (a slash is added to indicate the lack of a body). This is compatible with
     * HTML, but reflects an XML/XHTML leaning.
     */

    private boolean _emptyTag = false;

    private String _contentType;

    /**
     * A Stack of Strings used to track the active tag elements. Elements are active until the
     * corresponding close tag is written. The {@link #push(String)}method adds elements to the
     * stack, {@link #pop()}removes them.
     */

    private List _activeElementStack;

    public MarkupWriterImpl(String contentType, PrintWriter writer, MarkupFilter filter)
    {
        Defense.notNull(contentType, "contentType");
        Defense.notNull(writer, "writer");
        Defense.notNull(filter, "filter");

        _contentType = contentType;
        _writer = writer;
        _filter = filter;
    }

    public void attribute(String name, int value)
    {
        checkTagOpen();

        _writer.print(' ');
        _writer.print(name);
        _writer.print("=\"");
        _writer.print(value);
        _writer.print('"');
    }

    public void attribute(String name, boolean value)
    {
        checkTagOpen();

        _writer.print(' ');
        _writer.print(name);
        _writer.print("=\"");
        _writer.print(value);
        _writer.print('"');
    }

    public void attribute(String name, String value)
    {
        attribute(name, value, false);
    }

    public void attribute(String name, String value, boolean raw)
    {
        checkTagOpen();

        _writer.print(' ');

        // Could use a check here that name contains only valid characters

        _writer.print(name);
        _writer.print("=\"");

        if (value != null)
        {
            char[] data = value.toCharArray();
            maybePrintFiltered(data, 0, data.length, raw, true);
        }

        _writer.print('"');
    }

    /**
     * Prints the value, if non-null. May pass it through the filter, unless raw is true.
     */

    private void maybePrintFiltered(char[] data, int offset, int length, boolean raw,
            boolean isAttribute)
    {
        if (data == null || length <= 0)
            return;

        if (raw)
        {
            _writer.write(data, offset, length);
            return;
        }

        _filter.print(_writer, data, offset, length, isAttribute);
    }

    public void attributeRaw(String name, String value)
    {
        attribute(name, value, true);
    }

    public void begin(String name)
    {
        if (_openTag)
            closeTag();

        push(name);

        _writer.print('<');
        _writer.print(name);

        _openTag = true;
        _emptyTag = false;
    }

    public void beginEmpty(String name)
    {
        if (_openTag)
            closeTag();

        _writer.print('<');
        _writer.print(name);

        _openTag = true;
        _emptyTag = true;
    }

    public boolean checkError()
    {
        return _writer.checkError();
    }

    public void close()
    {
        if (_openTag)
            closeTag();

        // Close any active elements.

        while (!stackEmpty())
        {
            _writer.print("</");
            _writer.print(pop());
            _writer.print('>');
        }

        _writer.close();

        _writer = null;
        _filter = null;
        _activeElementStack = null;
    }

    public void closeTag()
    {
        if (_emptyTag)
            _writer.print('/');

        _writer.print('>');

        _openTag = false;
        _emptyTag = false;
    }

    public void comment(String value)
    {
        if (_openTag)
            closeTag();

        _writer.print("<!-- ");
        _writer.print(value);
        _writer.println(" -->");
    }

    public void end()
    {
        if (_openTag)
            closeTag();

        if (stackEmpty())
            throw new ApplicationRuntimeException(MarkupMessages.endWithEmptyStack());

        _writer.print("</");
        _writer.print(pop());
        _writer.print('>');
    }

    public void end(String name)
    {
        if (_openTag)
            closeTag();

        if (_activeElementStack == null || !_activeElementStack.contains(name))
            throw new ApplicationRuntimeException(MarkupMessages.elementNotOnStack(
                    name,
                    _activeElementStack));

        while (true)
        {
            String tagName = pop();

            _writer.print("</");
            _writer.print(tagName);
            _writer.print('>');

            if (tagName.equals(name))
                break;
        }
    }

    public void flush()
    {
        _writer.flush();
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedMarkupWriterImpl(this, _filter);
    }

    public void print(char[] data, int offset, int length)
    {
        print(data, offset, length, false);
    }

    public void printRaw(char[] buffer, int offset, int length)
    {
        print(buffer, offset, length, true);
    }

    public void print(char[] buffer, int offset, int length, boolean raw)
    {
        if (_openTag)
            closeTag();

        maybePrintFiltered(buffer, offset, length, raw, false);
    }

    public void print(String value)
    {
        print(value, false);
    }

    public void printRaw(String value)
    {
        print(value, true);
    }

    public void print(String value, boolean raw)
    {
        if (value == null || value.length() == 0)
        {
            print(null, 0, 0, raw);
            return;
        }

        char[] buffer = value.toCharArray();

        print(buffer, 0, buffer.length, raw);
    }

    public void print(char value)
    {
        char[] data = new char[]
        { value };

        print(data, 0, 1);
    }

    public void print(int value)
    {
        if (_openTag)
            closeTag();

        _writer.print(value);
    }

    public void println()
    {
        if (_openTag)
            closeTag();

        _writer.println();
    }

    public String getContentType()
    {
        return _contentType;
    }

    private void checkTagOpen()
    {
        if (!_openTag)
            throw new IllegalStateException(MarkupMessages.tagNotOpen());
    }

    private void push(String name)
    {
        if (_activeElementStack == null)
            _activeElementStack = new ArrayList();

        _activeElementStack.add(name);
    }

    private String pop()
    {
        int lastIndex = _activeElementStack.size() - 1;

        return (String) _activeElementStack.remove(lastIndex);
    }

    private boolean stackEmpty()
    {
        return _activeElementStack == null || _activeElementStack.isEmpty();
    }
}