package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  A wrapper around a double parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class DoubleParameter implements IParameter
{
    private double _value;

    public DoubleParameter(double value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setDouble(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Double<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
