package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  A wrapper around an integer parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class IntegerParameter implements IParameter
{
    private int _value;

    public IntegerParameter(int value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setInt(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Integer<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
