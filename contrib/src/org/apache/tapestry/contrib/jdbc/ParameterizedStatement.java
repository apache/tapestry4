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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  A wrapper around {@link PreparedStatement}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class ParameterizedStatement implements IStatement
{
    private static final Log LOG = LogFactory.getLog(ParameterizedStatement.class);

    private String _SQL;
    private PreparedStatement _statement;
    private IParameter[] _parameters;

    /**
     *  Create a new instance; the parameters list is copied.
     * 
     *  @param SQL the SQL to execute (see {@link Connection#prepareStatement(java.lang.String)})
     *  @param connection the JDBC connection to use
     *  @param parameters list of {@link IParameter}
     * 
     **/
    
    public ParameterizedStatement(String SQL, Connection connection, List parameters) throws SQLException
    {
        _SQL = SQL;

        _statement = connection.prepareStatement(SQL);

        _parameters = (IParameter[]) parameters.toArray(new IParameter[parameters.size()]);

        for (int i = 0; i < _parameters.length; i++)
        {
            // JDBC numbers things from 1, not 0.

            _parameters[i].set(_statement, i + 1);
        }
    }

    /**
     * Returns the SQL associated with this statement.
     *
     **/

    public String getSQL()
    {
        return _SQL;
    }

    /**
     *  Returns the underlying or {@link PreparedStatement}.
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
        _SQL = null;
    }

    /**
     *  Executes the statement as a query, returning a {@link ResultSet}.
     *
     **/

    public ResultSet executeQuery() throws SQLException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Executing query: " + this);

        return _statement.executeQuery();
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

        return _statement.executeUpdate();
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ParameterizedStatement@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append("[SQL=\n<");
        buffer.append(_SQL);
        buffer.append("\n>");

        for (int i = 0; i < _parameters.length; i++)
        {
            IParameter parameter = _parameters[i];

            buffer.append(" ?");
            buffer.append(i + 1);
            buffer.append('=');

            buffer.append(parameter);
        }

        buffer.append(']');

        return buffer.toString();
    }

}