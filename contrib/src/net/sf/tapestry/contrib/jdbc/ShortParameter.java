package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  A wrapper around a short parameter.  
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ShortParameter implements IParameter
{
    private short _value;

    public ShortParameter(short value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setShort(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Short<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }

}
