package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  An arbitrary object parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ObjectParameter implements IParameter
{
    private Object _value;

    public ObjectParameter(Object value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setObject(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Object<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }
}
