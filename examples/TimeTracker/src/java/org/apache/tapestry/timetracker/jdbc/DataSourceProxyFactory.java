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

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.hivemind.Discardable;
import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;
import org.apache.hivemind.events.RegistryShutdownListener;


/**
 * Wrapper around jdbc {@link BasicDataSource} pools for providing 
 * transaction aware connection pooling services to hivemind.
 *
 * @author jkuhnert
 */
public class DataSourceProxyFactory implements ServiceImplementationFactory, 
RegistryShutdownListener
{
    /* logger */
    protected Log _log;
    /* jdbc pool */
    protected BasicDataSource _dataSource;
    
    /**
     * {@inheritDoc}
     */
    public Object createCoreServiceImplementation(ServiceImplementationFactoryParameters parms)
    {
        if (_dataSource == null)
            throw new IllegalStateException("No dataSource configured for factory.");
        
        _log = parms.getLog();
        try {
            _log.debug("Opening connection ");
            Connection conn = _dataSource.getConnection();
            
            return Proxy.newProxyInstance(this.getClass()
                    .getClassLoader(), new Class[] { Connection.class,
                Discardable.class }, new JdbcConnectionProxy(_log, conn));
        } catch (SQLException e) {
            _log.fatal("Unable to create a new DB connection", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void registryDidShutdown()
    {
        try {
            _dataSource.close();
        } catch (Throwable t) {
            _log.error("Error shutting down dataSource", t);
        }
    }
    
    /**
     * Sets the data source.
     * @param dataSource
     */
    public void setDataSource(BasicDataSource dataSource)
    {
        _dataSource = dataSource;
    }
}
