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

package org.apache.tapestry.describe;

import java.util.Collection;
import java.util.Iterator;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

/**
 * Implementation of {@link org.apache.tapestry.describe.DescriptionReceiver}that produces HTML
 * output using a {@link org.apache.tapestry.IMarkupWriter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class HTMLDescriptionReceiver implements DescriptionReceiver
{
    // Emitted for null values.

    static final String NULL_VALUE = "<NULL>";

    private IMarkupWriter _writer;

    private boolean _emitDefault = true;

    private String _title;

    private String _section;

    private DescribableAdapter _adapter;

    public HTMLDescriptionReceiver(IMarkupWriter writer, DescribableAdapter adapter)
    {
        Defense.notNull(writer, "writer");
        Defense.notNull(adapter, "adapter");

        _writer = writer;
        _adapter = adapter;
    }

    public void describe(Object object)
    {
        if (object == null)
        {
            _writer.print(NULL_VALUE);
            return;
        }

        _adapter.describeObject(object, this);

        finishUp(object);
    }

    void finishUp(Object object)
    {
        if (_emitDefault)
        {
            String value = _title != null ? _title : object.toString();

            _writer.print(value);
        }
        // Not emit default .. means a property was emitted, so a table was started and must be
        // finished.
        else
            _writer.end("table");

        _writer.println();
    }

    public void title(String title)
    {
        Defense.notNull(title, "title");

        if (_title != null)
            throw new IllegalStateException(DescribeMessages.setTitleOnce());

        _title = title;
    }

    public void section(String section)
    {
        Defense.notNull(section, "section");

        if (_title == null)
            throw new IllegalStateException(DescribeMessages.mustSetTitleBeforeSection());

        _section = section;
    }

    private void assertTitleSet()
    {
        if (_title == null)
            throw new IllegalStateException(DescribeMessages.mustSetTitleBeforeProperty());
    }

    private void emitSection()
    {
        if (_emitDefault)
        {
            _emitDefault = false;

            _writer.begin("div");
            _writer.attribute("class", "described-object-title");
            _writer.print(_title);
            _writer.end();
            _writer.println();

            _writer.begin("table");
            _writer.attribute("class", "described-object");
            _writer.println();
        }

        if (_section != null)
        {
            _writer.begin("tr");
            _writer.attribute("class", "section");
            _writer.begin("th");
            _writer.attribute("colspan", 2);
            _writer.print(_section);
            _writer.end("tr");
            _writer.println();

            _section = null;
        }
    }

    private void pair(String key, String value)
    {
        assertTitleSet();
        emitSection();

        _writer.begin("tr");
        _writer.begin("th");
        _writer.print(key);
        _writer.end();
        _writer.begin("td");
        _writer.print(value);
        _writer.end("tr");
        _writer.println();
    }

    public void property(String key, Object value)
    {
        Defense.notNull(key, "key");

        assertTitleSet();
        emitSection();

        _writer.begin("tr");
        _writer.begin("th");
        _writer.print(key);
        _writer.end();
        _writer.begin("td");

        describeNested(value);

        _writer.end("tr");
        _writer.println();
    }

    private void describeNested(Object value)
    {
        if (value == null)
        {
            _writer.print(NULL_VALUE);
            return;
        }

        new HTMLDescriptionReceiver(_writer, _adapter).describe(value);
    }

    public void property(String key, boolean value)
    {
        Defense.notNull(key, "key");

        // toString is JDK 1.4 and above, so we'll provide our own.

        pair(key, value ? "true" : "false");
    }

    public void property(String key, byte value)
    {
        Defense.notNull(key, "key");

        pair(key, Byte.toString(value));
    }

    public void property(String key, short value)
    {
        Defense.notNull(key, "key");

        pair(key, Short.toString(value));
    }

    public void property(String key, int value)
    {
        Defense.notNull(key, "key");

        pair(key, Integer.toString(value));
    }

    public void property(String key, long value)
    {
        Defense.notNull(key, "key");

        pair(key, Long.toString(value));
    }

    public void property(String key, float value)
    {
        Defense.notNull(key, "key");

        pair(key, Float.toString(value));
    }

    public void property(String key, double value)
    {
        Defense.notNull(key, "key");

        pair(key, Double.toString(value));
    }

    public void property(String key, char value)
    {
        Defense.notNull(key, "key");

        pair(key, Character.toString(value));
    }

    public void array(String key, Object[] values)
    {
        Defense.notNull(key, "key");

        assertTitleSet();

        if (values == null || values.length == 0)
            return;

        emitSection();

        for (int i = 0; i < values.length; i++)
        {
            _writer.begin("tr");
            _writer.begin("th");

            if (i == 0)
                _writer.print(key);

            _writer.end();

            _writer.begin("td");

            describeNested(values[i]);

            _writer.end("tr");
            _writer.println();
        }

    }

    public void collection(String key, Collection values)
    {
        Defense.notNull(key, "key");

        assertTitleSet();

        if (values == null || values.isEmpty())
            return;

        emitSection();

        Iterator i = values.iterator();
        boolean first = true;

        while (i.hasNext())
        {
            _writer.begin("tr");
            _writer.begin("th");

            if (first)
                _writer.print(key);

            _writer.end();
            _writer.begin("td");

            describeNested(i.next());

            _writer.end("tr");
            _writer.println();

            first = false;
        }
    }
}