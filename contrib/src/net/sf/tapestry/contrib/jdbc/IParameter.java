package net.sf.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  Represents a parameter within a dynamically generated SQL statement.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *  @see net.sf.tapestry.contrib.jdbc.ParameterizedStatement
 * 
 **/

public interface IParameter
{
    /**
     *  Invokes the appropriate setXXX() method on the 
     *  {@link java.sql.PreparedStatement}.
     * 
     **/
    
    public void set(PreparedStatement statement, int index)
    throws SQLException;
}
