package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 *  Used with Timestamp parameters.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TimestampParameter implements IParameter
{
    private Timestamp _timestamp;

    public TimestampParameter(Timestamp timestamp)
    {
        _timestamp = timestamp;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        if (_timestamp == null)
            statement.setNull(index, Types.TIMESTAMP);
        else
            statement.setTimestamp(index, _timestamp);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Timestamp<");
        buffer.append(_timestamp);
        buffer.append('>');

        return buffer.toString();
    }
}
