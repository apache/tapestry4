/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.contrib.mckoi;

import java.io.File;
import java.io.IOException;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.system.ServiceMBeanSupport;

import com.mckoi.database.control.DBController;
import com.mckoi.database.control.DBSystem;
import com.mckoi.database.control.DefaultDBConfig;
import com.mckoi.database.control.TCPJDBCServer;

/**
 *  An MBean used to start and stop an embedded instance of
 *  <a href="http://www.mckoi.com/database">McKoi Database</a>.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public class McKoiDB extends ServiceMBeanSupport implements McKoiDBMBean
{
    private String _configPath;
    private DBSystem _database;
    private TCPJDBCServer _server;

    public String getConfigPath()
    {
        return _configPath;
    }

    public void setConfigPath(String path)
    {
        log.debug("Config path set to: " + path);
        _configPath = path;
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception
    {
        if (name != null)
            return name;
            
        return new ObjectName(":service=McKoiDB");
    }

    public String getName()
    {
        return "McKoiDB";    }

    public void startService() throws Exception
    {
        if (_configPath == null)
            throw new NullPointerException("McKoiDB: configPath not specified.");

        log.debug("Config path: " + _configPath);

        File configFile = new File(_configPath).getAbsoluteFile();
        File rootFile = configFile.getParentFile();

        DefaultDBConfig config = new DefaultDBConfig(rootFile);

        try
        {
            config.loadFromFile(configFile);
        }
        catch (IOException ex)
        {
            log.error("Unable to initialize McKoi database.", ex);
        }

        DBController controller = DBController.getDefault();
        _database = controller.startDatabase(config);

        _server = new TCPJDBCServer(_database);

        _server.start();

        log.info(_server);

    }

    public void stopService()
    {
        _server.stop();
        _database.close();
    }
}