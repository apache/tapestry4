package net.sf.tapestry.contrib.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  A wrapper around {@link Statement}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class SimpleStatement implements IStatement
{
    private static final Log LOG = LogFactory.getLog(SimpleStatement.class);

    private String _sql;
    private Statement _statement;

    public SimpleStatement(String SQL, Connection connection) throws SQLException
    {
        _sql = SQL;
        _statement = connection.createStatement();
    }

    public SimpleStatement(String SQL, Connection connection, int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        _sql = SQL;
        _statement = connection.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * Returns the SQL associated with this statement.
     *
     **/

    public String getSQL()
    {
        return _sql;
    }

    /**
     *  Returns the underlying {@link Statement}.
     *
     **/

    public Statement getStatement()
    {
        return _statement;
    }

    /**
     *  Closes the underlying statement, and nulls the reference to it.
     *
     **/

    public void close() throws SQLException
    {
        _statement.close();

        _statement = null;
        _sql = null;
    }

    /**
     *  Executes the statement as a query, returning a {@link ResultSet}.
     *
     **/

    public ResultSet executeQuery() throws SQLException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Executing query: " + this);

        return _statement.executeQuery(_sql);
    }

    /**
     *  Executes the statement as an update, returning the number of rows
     *  affected.
     *
     **/

    public int executeUpdate() throws SQLException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Executing update: " + this);

        return _statement.executeUpdate(_sql);
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("SimpleStatement@");
        buffer.append(Integer.toHexString(hashCode()));

        buffer.append("[SQL=<\n");
        buffer.append(_sql);
        buffer.append("\n>]");

        return buffer.toString();
    }

}