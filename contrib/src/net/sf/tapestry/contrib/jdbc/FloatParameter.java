package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  A wrapper around a float parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class FloatParameter implements IParameter
{
    private float _value;

    public FloatParameter(float value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setFloat(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Float<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
