package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  Wrapper around long parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class LongParameter implements IParameter
{
    private long _value;

    public LongParameter(long value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setLong(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Long<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
