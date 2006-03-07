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
package org.apache.tapestry.timetracker.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Checks the database instance running against for correct table
 * setup, if it can't find any of the required tables will install
 * them manually.
 *
 * @author jkuhnert
 */
public class DatabaseInstaller
{
    /** File ISO format. */
    public static final String ISO_FORMAT = "ISO8859_1";
    protected static Log _log = LogFactory.getLog(DatabaseInstaller.class);
    
    /** threaded db conn. */
    protected Connection _conn;
    /** db installer file path. */
    protected String _filePath;
    
    /** default constructor. */
    public DatabaseInstaller() { }
    
    /**
     * Invoked to cause initialization of db checks.
     */
    public void initialise()
    throws Exception
    {
        assert _filePath != null;
        
        if (!tablesExist())
            createDatabase();
    }
    
    /**
     * Checks for existance of database tables.
     * @return True, if any row exists in a table called "projects".
     * @throws SQLException on error
     */
    public boolean tablesExist()
    throws SQLException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = _conn.prepareStatement("select 'x' from INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_NAME = 'PROJECTS'");
            rs = ps.executeQuery();
            
            return rs.next();
            
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }
        }
    }
    
    /**
     * Creates the database by reading in a sql creation file
     * and running the contents found within on the connected 
     * database. 
     * @throws Exception If any io/db errors occur.
     */
    protected void createDatabase()
    throws Exception
    {
        _log.debug("createDatabase() creating database tables..");
        PreparedStatement ps = null;
        try {
            
            ps = _conn.prepareStatement(FileUtils.readFileToString(new File(_filePath), ISO_FORMAT));
            ps.execute();
            
        } catch (Throwable t) {
            _log.error("Error creating database.", t);
            try { if (_conn != null) _conn.rollback(); } catch (Exception e) { }
            throw new RuntimeException(t);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { }
        }
    }
    
    /**
     * Sets db connection.
     * @param conn
     */
    public void setConnection(Connection conn)
    {
        _conn = conn;
    }
    
    /**
     * Sets the createDatabase.sql file path.
     * @param filePath
     */
    public void setFilePath(String filePath)
    {
        _filePath = filePath;
    }
}
