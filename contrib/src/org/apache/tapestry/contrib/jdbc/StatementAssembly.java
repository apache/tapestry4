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

package org.apache.tapestry.contrib.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *  Class for creating and executing JDBC statements.  Allows statements to be assembled
 *  incrementally (like a {@link StringBuffer}), but also tracks parameters, shielding
 *  the developer from the differences between constructing and 
 *  using a JDBC 
 *  {@link java.sql.Statement} and 
 *  a JDBC {@link java.sql.PreparedStatement}.  This class is somewhat skewed towards
 *  Oracle, which works more efficiently with prepared staments than
 *  simple SQL.
 * 
 *  <p>In addition, implements {@link #toString()} in a useful way (you can see the
 *  SQL and parameters), which is invaluable when debugging.
 * 
 *  <p>{@link #addParameter(int)} (and all overloaded versions of it for scalar types)
 *  adds a "?" to the statement and records the parameter value.
 * 
 *  <p>{@link #addParameter(Integer)} (and all overloaded version of it for wrapper
 *  types) does the same ... unless the value is null, in which case "NULL" is
 *  inserted into the statement.
 * 
 *  <p>{@link #addParameterList(int[], String)} (and all overloaded versions of it)
 *  simply invokes the appropriate {@link #addParameter(int)}, adding the
 *  separator in between parameters.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class StatementAssembly
{
    private StringBuffer _buffer = new StringBuffer();

    private static final String NULL = "NULL";

    public static final String SEP = ", ";

    /**
     *  List of {@link IParameter}
     * 
     **/

    private List _parameters;

    private int _lineLength;
    private int _maxLineLength = 80;
    private int _indent = 5;

    /**
     *  Default constructor; uses a maximum line length of 80 and an indent of 5.
     *
     **/

    public StatementAssembly()
    {
    }

    public StatementAssembly(int maxLineLength, int indent)
    {
        _maxLineLength = maxLineLength;
        _indent = indent;
    }

    /**
     *  Clears the assembly, preparing it for re-use.
     * 
     *  @since 1.0.7
     * 
     **/

    public void clear()
    {
        _buffer.setLength(0);
        _lineLength = 0;

        if (_parameters != null)
            _parameters.clear();
    }

    /**
     *  Maximum length of a line.
     *
     **/

    public int getMaxLineLength()
    {
        return _maxLineLength;
    }

    /**
     *  Number of spaces to indent continuation lines by.
     *
     **/

    public int getIndent()
    {
        return _indent;
    }

    /**
     *  Adds text to the current line, unless that would make the line too long, in
     *  which case a new line is started (and indented) before adding the text.
     *
     *  <p>Text is added as-is, with no concept of quoting.  To add arbitrary strings
     *  (such as in a where clause), use {@link #addParameter(String)}.
     *
     *
     **/

    public void add(String text)
    {
        int textLength;

        textLength = text.length();

        if (_lineLength + textLength > _maxLineLength)
        {
            _buffer.append('\n');

            for (int i = 0; i < _indent; i++)
                _buffer.append(' ');

            _lineLength = _indent;
        }

        _buffer.append(text);
        _lineLength += textLength;
    }
    
    public void add(short value)
    {
        add(Short.toString(value));
    }
    
    public void add(int value)
    {
        add(Integer.toString(value));
    }
    
    public void add(long value)
    {
        add(Long.toString(value));
    }
    
    public void add(float value)
    {
        add(Float.toString(value));
    }
    
    public void add(double value)
    {
        add(Double.toString(value));
    }

    /**
     *  Adds a date value to a {@link StatementAssembly} converting
     *  it to a {@link java.sql.Timestamp} first.
     *
     **/

    public void addParameter(Date date)
    {
        if (date == null)
        {
            add("NULL");
            return;
        }

        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);

        Date adjusted = calendar.getTime();

        Timestamp timestamp = new Timestamp(adjusted.getTime());

        addParameter(timestamp);
    }

    /** 
     *  Adds a separator (usually a comma and a space) to the current line, regardless
     *  of line length.  This is purely aesthetic ... it just looks odd if a separator
     *  gets wrapped to a new line by itself.
     *
     **/

    public void addSep(String text)
    {
        _buffer.append(text);
        _lineLength += text.length();
    }

    /**
     *  Starts a new line, without indenting.
     *
     **/

    public void newLine()
    {
        if (_buffer.length() != 0)
            _buffer.append('\n');

        _lineLength = 0;
    }

    /**
     * Starts a new line, then adds the given text.
     *
     **/

    public void newLine(String text)
    {
        if (_buffer.length() != 0)
            _buffer.append('\n');

        _buffer.append(text);

        _lineLength = text.length();
    }

    public void addList(String[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            add(items[i]);
        }
    }

    public void addParameterList(int[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameterList(Integer[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameterList(long[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameterList(Long[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameterList(String[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameterList(double[] items, String separator)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (i > 0)
                addSep(separator);

            addParameter(items[i]);
        }
    }

    public void addParameter(Object value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(new ObjectParameter(value));
    }

    public void addParameter(Timestamp timestamp)
    {
        if (timestamp == null)
            add(NULL);
        else
            addParameter(new TimestampParameter(timestamp));
    }

    public void addParameter(String value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(new StringParameter(value));
    }

    public void addParameter(int value)
    {
        addParameter(new IntegerParameter(value));
    }

    public void addParameter(Integer value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.intValue());
    }

    public void addParameter(long value)
    {
        addParameter(new LongParameter(value));
    }

    public void addParameter(Long value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.longValue());
    }

    public void addParameter(float value)
    {
        addParameter(new FloatParameter(value));
    }

    public void addParameter(Float value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.floatValue());
    }

    public void addParameter(double value)
    {
        addParameter(new DoubleParameter(value));
    }

    public void addParameter(Double value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.doubleValue());
    }

    public void addParameter(short value)
    {
        addParameter(new ShortParameter(value));
    }

    public void addParameter(Short value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.shortValue());
    }

    public void addParameter(boolean value)
    {
        addParameter(value ? BooleanParameter.TRUE : BooleanParameter.FALSE);
    }

    public void addParameter(Boolean value)
    {
        if (value == null)
            add(NULL);
        else
            addParameter(value.booleanValue());
    }

    private void addParameter(IParameter parameter)
    {
        if (_parameters == null)
            _parameters = new ArrayList();

        _parameters.add(parameter);

        add("?");
    }

    /**
     *  Creates and returns an {@link IStatement} based on the SQL and parameters
     *  acquired.
     *
     **/

    public IStatement createStatement(Connection connection) throws SQLException
    {
        String sql = _buffer.toString();

        if (_parameters == null || _parameters.isEmpty())
            return new SimpleStatement(sql, connection);

        return new ParameterizedStatement(sql, connection, _parameters);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("StatementAssembly@");

        buffer.append(Integer.toHexString(hashCode()));
        buffer.append("[SQL=\n<");
        buffer.append(_buffer);
        buffer.append("\n>");

        if (_parameters != null)
        {
            int count = _parameters.size();
            for (int i = 0; i < count; i++)
            {
                Object parameter = _parameters.get(i);

                buffer.append(" ?");
                buffer.append(i + 1);
                buffer.append('=');

                buffer.append(parameter);
            }
        }

        buffer.append(']');

        return buffer.toString();
    }
}