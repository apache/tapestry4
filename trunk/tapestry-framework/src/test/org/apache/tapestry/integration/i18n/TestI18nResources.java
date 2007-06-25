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
package org.apache.tapestry.integration.i18n;

import org.apache.tapestry.integration.JettyRunner;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * Tests the i18n test application. 
 * 
 * Initially created to test TAPESTRY-881 feature enhancement.
 * 
 */
@Test(timeOut = 50000, groups = "integration", sequential=true)
public class TestI18nResources
{
    private static final int JETTY_PORT = 9999;
    private static final String BASE_URL = "http://localhost:9999/";
    
    /** 60 seconds */
    public static final String PAGE_LOAD_TIMEOUT = "600000";
    
    private Selenium _selenium;
    
    private SeleniumServer _server;
    
    private JettyRunner _jettyRunner;
    
    @BeforeClass
    public void startupBackground() throws Exception
    {
        _jettyRunner = new JettyRunner("/", JETTY_PORT, "src/test-data/i18n");
        
        _server = new SeleniumServer();

        _server.start();

        _selenium = new DefaultSelenium("localhost", SeleniumServer.DEFAULT_PORT, "*firefox",
                BASE_URL);

        _selenium.start();
    }

    @AfterClass
    public void shutdownBackground() throws Exception
    {
        _selenium.stop();
        _selenium = null;

        _server.stop();
        _server = null;

        _jettyRunner.stop();
        _jettyRunner = null;
    }
    
    public void test_Key_Exists()
    throws Exception
    {
        _selenium.open(BASE_URL);
        
        assert _selenium.isTextPresent("Random!");
    }
    
}
