package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  Wrapper around a boolean parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class BooleanParameter implements IParameter
{
    private boolean _value;

    public static final BooleanParameter TRUE = new BooleanParameter(true);

    public static final BooleanParameter FALSE = new BooleanParameter(false);

    private BooleanParameter(boolean value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setBoolean(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Boolean<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }

}
