//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.jdbc;

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