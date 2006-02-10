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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Discardable;


/**
 * Wraps {@link Connection} to provide hivemind threaded connection
 * services.
 *
 * @author jkuhnert
 */
public class JdbcConnectionProxy implements InvocationHandler, Discardable
{
    
    /* Logger */
    protected Log _log;
    /* connection */
    protected Connection _conn;
    /* Intercepts close method calls */
    protected Method closeMethod;
    
    /**
     * Creates a new proxy wrapping the provided {@link Connection}.
     * @param log
     * @param conn
     * @throws SQLException
     */
    public JdbcConnectionProxy(Log log, Connection conn)
    throws SQLException
    {
        _log = log;
        _conn = conn;
        
        try {
            closeMethod = this.getClass().getDeclaredMethod("threadDidDiscardService", (Class[])null);
        } catch (Exception et) {
            throw new RuntimeException(et);
        }
        
        _conn.setAutoCommit(false);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        try {
            if (method.getName().equals("threadDidDiscardService"))
                return closeMethod.invoke(this, args);
            else if (method.getName().equals("close")) {
                _log.debug("Doing nice connection close.");
                return closeMethod.invoke(this, args);
            }
            
            return method.invoke(_conn, args);
        } catch (IllegalAccessException e) {
            _log.error(e);
            throw e;
        } catch (InvocationTargetException e) {
            _log.error("Error ocurred in invoker, rolling back any pending db transaction.", e);
            try { if (_conn != null) _conn.rollback(); } catch (Exception et) { }
            closeMethod.invoke(this, new Object[0]);
            throw e.getTargetException();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void threadDidDiscardService()
    {
        try {
            if (_log.isDebugEnabled())
                _log.debug("threadDidDiscardService(): Closing connection: " + _conn.isClosed());
            
            if (!_conn.isClosed()) {
                if (!_conn.getAutoCommit()) {
                    if (_log.isDebugEnabled())
                        _log.debug("Committing uncommitted transaction.");
                    _conn.commit();
                }
            }
        } catch (SQLException e) {
            _log.error("SQL error cleaning up connection, rolling back transaction(if any).", e);
            try { if (_conn != null) _conn.rollback(); } catch (Exception et) { }
        } finally {
            try { if (_conn != null) _conn.close(); } catch (Exception e) { }
        }
    }

}
