/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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