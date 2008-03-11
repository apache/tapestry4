// Copyright 2006 The Apache Software Foundation
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

package org.apache.tapestry.integration;

import org.openqa.selenium.server.SeleniumServer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Note: If these tests fail with BindException when starting Jetty, it could be Skype. At least on
 * my system, Skype is listening on localhost:80.
 */
@Test(timeOut = 50000, groups = "integration", sequential=true)
public class TestBrowserIssues extends Assert
{
    private static final int JETTY_PORT = 9999;
    private static final String BASE_URL = "http://localhost:9999/";

    /** 60 seconds. */
    public static final String PAGE_LOAD_TIMEOUT = "60000";

    private Selenium _selenium;

    private SeleniumServer _server;

    private JettyRunner _jettyRunner;

    @BeforeClass
    public void startupBackground() throws Exception
    {
        _jettyRunner = new JettyRunner("/", JETTY_PORT, "src/test-data/app1");

        _server = new SeleniumServer();
        
        _server.start();

        _selenium = new DefaultSelenium("localhost", SeleniumServer.DEFAULT_PORT, "*firefox", BASE_URL);

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

    public void test_issue_1141() throws Exception
    {
        openIssuePage("TAPESTRY-1141");

        String body = _selenium.getBodyText();

        assertTrue(body.contains("[]"));
        
        assertTrue(_selenium.isElementPresent("num"));
        
        _selenium.type("num_1", "4");
        
        _selenium.click("Submit");
        
        waitForInnerHTML("testme", "[4]");        
        
        _selenium.type("num_1", "5");
        
        submitFromTextfield("num_1");
        
        waitForInnerHTML("testme", "[5]");        
        
        _selenium.type("num_1", "6");
        
        _selenium.type("num_0", "2");
        
        submitFromTextfield("num_0");
        
        waitForInnerHTML("testme", "[6]");
    }

    public void test_issue_1129() throws Exception
    {
        openIssuePage("TAPESTRY-1129");

        String body = _selenium.getBodyText();

        assertTrue(body.contains("false"));

        _selenium.click("link=refresh");

        waitForInnerHTML("flag", "true");

        assertTrue(_selenium.isElementPresent("TextArea"));

        assertTrue("".equals(_selenium.getValue("TextArea").trim()));
    }

    public void test_issue_1775_a() throws Exception
    {
        openIssuePage("TAPESTRY-1775");

        assertFalse(_selenium.isElementPresent("msg"));

        _selenium.click("first");

        waitForInnerHTML("msg", "FIRST");

        _selenium.click("second");

        waitForInnerHTML("msg", "SECOND");

        _selenium.click("nothing");

        waitForNodeToDisappear("msg");
    }

    public void test_issue_1775_b() throws Exception
    {
        openIssuePage("TAPESTRY-1775");

        assertTrue("".equals(_selenium.getText("msg2").trim()));

        _selenium.click("success");

        waitForInnerHTML("msg2", "SUCCESS");

        _selenium.click("cancel");

        waitForInnerHTML("msg2", "CANCEL");

        _selenium.click("success");

        waitForInnerHTML("msg2", "SUCCESS");

        _selenium.click("refresh");

        waitForInnerHTML("msg2", "REFRESH");

        _selenium.click("success");

        waitForInnerHTML("msg2", "SUCCESS");
    }

    public void test_issue_2225() throws Exception
    {
        openIssuePage("TAPESTRY-2225");

        assertFalse(_selenium.isElementPresent("normal"));
        assertTrue(_selenium.isElementPresent("status"));

        _selenium.click("asyncR");
        waitForInnerHTML("status", "asyncREFRESH");
        
        _selenium.click("asyncC");
        waitForInnerHTML("status", "asyncCANCEL");

        clickAndWait("normR");
        assertTrue(_selenium.isElementPresent("normal"));
        assertEquals(getInnerHTML("normal"), "refresh");

        clickAndWait("normC");
        assertTrue(_selenium.isElementPresent("normal"));
        assertEquals(getInnerHTML("normal"), "cancel");
    }

    private void openIssuePage(String issue)
    {
        _selenium.open(BASE_URL);

        clickAndWait("link=" + issue);

        assertTrue(_selenium.getTitle().contains(issue));
    }

    private String getInnerHTML(String elm)
    {
        return _selenium.getEval("selenium.browserbot.getCurrentWindow().document.getElementById('"
            + elm + "').innerHTML");
    }

    private void waitForInnerHTML(String elm, String content)
    {
        _selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('"
                + elm + "').innerHTML=='" + content + "'","6000");                
    }

    private void waitForNodeToDisappear(String elm)
    {
        _selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().document.getElementById('"
                + elm + "')","6000");
    }
    
    private void submitFromTextfield(String field)
    {
        _selenium.keyPress(field, "13");
        //_selenium.fireEvent(field, "command");
        /*_selenium.click(field);
        _selenium.keyDown(field, "13");
        _selenium.keyPress(field, "0");
        _selenium.keyUp(field, "13");*/
    }

    private void clickAndWait(String link)
    {
        _selenium.click(link);
        _selenium.waitForPageToLoad(PAGE_LOAD_TIMEOUT);
    }
}
