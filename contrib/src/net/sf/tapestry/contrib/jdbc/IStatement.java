package net.sf.tapestry.contrib.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  A wrapper around {@link java.sql.Statement} or 
 *  {@link java.sql.PreparedStatement} which hides the differences
 *  between the two.  
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @see net.sf.tapestry.contrib.jdbc.StatementAssembly#createStatement(Connection)
 * 
 **/

public interface IStatement
{
    /**
     * Returns the SQL associated with this statement.
     *
     **/

    public String getSQL();

    /**
     *  Returns the underlying {@link java.sql.Statement} 
     *  (or {@link java.sql.PreparedStatement}).
     *
     **/

    public Statement getStatement();

    /**
     *  Closes the underlying statement, and nulls the reference to it.
     *
     **/

    public void close() throws SQLException;

    /**
     *  Executes the statement as a query, returning a {@link ResultSet}.
     *
     **/

    public ResultSet executeQuery() throws SQLException;

    /**
     *  Executes the statement as an update, returning the number of rows
     *  affected.
     *
     **/

    public int executeUpdate() throws SQLException;
}