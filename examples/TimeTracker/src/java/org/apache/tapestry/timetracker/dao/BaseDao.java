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
package org.apache.tapestry.timetracker.dao;

import java.sql.Connection;


/**
 * Provides standard base methods.
 *  
 * @author jkuhnert
 */
public class BaseDao
{
    /** jdbc connection. */
    protected Connection _conn;
    
    /** Default constructor. */
    public BaseDao() { }
    
    /**
     * Injected connection.
     * @param conn
     */
    public void setConnection(Connection conn)
    {
        _conn = conn;
    }
    
    /**
     * 
     * @return The local connection being used.
     */
    public Connection getConnection()
    {
        return _conn;
    }
}
