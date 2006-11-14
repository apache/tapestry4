// Copyright 2004, 2005 The Apache Software Foundation
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

/**
 *  A wrapper around {@link java.sql.Statement} or 
 *  {@link java.sql.PreparedStatement} which hides the differences
 *  between the two.  
 *
 *  @author Howard Lewis Ship
 *  @see org.apache.tapestry.contrib.jdbc.StatementAssembly#createStatement(Connection)
 * 
 **/

public interface IStatement
{
    /**
     * Returns the SQL associated with this statement.
     *
     **/

    String getSQL();

    /**
     *  Returns the underlying {@link java.sql.Statement} 
     *  (or {@link java.sql.PreparedStatement}).
     *
     **/

    Statement getStatement();

    /**
     *  Closes the underlying statement, and nulls the reference to it.
     *
     **/

    void close() throws SQLException;

    /**
     *  Executes the statement as a query, returning a {@link ResultSet}.
     *
     **/

    ResultSet executeQuery() throws SQLException;

    /**
     *  Executes the statement as an update, returning the number of rows
     *  affected.
     *
     **/

    int executeUpdate() throws SQLException;
}
