//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  A wrapper around {@link Statement} or {@link java.sql.PreparedStatement}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
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
     *  Returns the underlying {@link Statement} (or {@link java.sql.PreparedStatement}).
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