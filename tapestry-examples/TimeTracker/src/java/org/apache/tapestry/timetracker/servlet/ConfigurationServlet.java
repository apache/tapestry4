// Copyright Apr 21, 2006 The Apache Software Foundation
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
package org.apache.tapestry.timetracker.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.apache.tapestry.ApplicationServlet;

/**
 * Used to configure logging.
 * 
 * @author jkuhnert
 */
public class ConfigurationServlet extends ApplicationServlet implements ServletContextListener {
    
    /**
     * generated.
     */
    private static final long serialVersionUID = -5959967554036278600L;

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() {
        try {
            // Use basic logging configuration until Log4j is properly configured
            PropertyConfigurator.configure(getServletContext().getRealPath("/")
                    + "/WEB-INF/log4j.properties");
            super.init();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    public void destroy()
    {
        super.destroy();
        org.apache.log4j.LogManager.shutdown();
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)
    {
        org.apache.log4j.LogManager.shutdown();
    }

    /** 
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent arg0)
    {
    }
}
