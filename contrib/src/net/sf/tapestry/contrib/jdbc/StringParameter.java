package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  Used with String parameters.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class StringParameter implements IParameter
{
    private String _value;

    public StringParameter(String value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        if (_value == null)
            statement.setNull(index, Types.VARCHAR);
        else
            statement.setString(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("String<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
