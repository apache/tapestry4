// Copyright Oct 7, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo;

import org.apache.hivemind.Resource;
import org.apache.tapestry.*;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.Locale;


/**
 * Tests basic functionality of {@link AjaxShellDelegate}.
 * 
 * @author jkuhnert
 */
@Test(sequential=true)
@SuppressWarnings("all")
public class AjaxShellDelegateTest extends BaseComponentTestCase
{
    private static final String SYSTEM_NEWLINE= (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));

    void trainStaticPath(IEngineService engine, IAsset asset, String path)
    {
        Resource res = newMock(Resource.class);
        
        expect(asset.getResourceLocation()).andReturn(res);
        expect(res.getPath()).andReturn(path);
        
        ILink link = newLink();
        
        expect(engine.getLink(Boolean.TRUE, path)).andReturn(link);
        expect(link.getURL()).andReturn("http://" + path);
    }
    
    void trainPageLocale(IRequestCycle cycle, Locale locale)
    {
        IPage page = newMock(IPage.class);        
        
        expect(cycle.getPage()).andReturn(page);
        expect(page.getLocale()).andReturn(locale);
    }
    
    public void test_Default_Render()
    {
        IAsset dojoSource = newAsset();
        IAsset dojoPath = newAsset();
        IAsset tSource = newAsset();
        IAsset tPath = newAsset();
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        IEngine engine = newMock(IEngine.class);
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newBufferWriter();

        expect(cycle.getEngine()).andReturn(engine);
        expect(engine.getOutputEncoding()).andReturn("utf-foo");
        expect(cycle.getPage()).andReturn(page);
        expect(dojoPath.buildURL()).andReturn("http:///dojo/path");

        expect(page.hasFormComponents()).andReturn(false).anyTimes();
        expect(page.hasWidgets()).andReturn(false).anyTimes();

        trainPageLocale(cycle, Locale.US);
        
        expect(dojoSource.buildURL()).andReturn("http:///dojo/path/dojo.js");
        expect(tPath.buildURL()).andReturn("/tapestry");
        expect(tSource.buildURL()).andReturn("/tapestry/tapestry.js");
        
        AjaxShellDelegate d = new AjaxShellDelegate();
        d.setDojoPath(dojoPath);
        d.setDojoSource(dojoSource);
        d.setTapestrySource(tSource);
        d.setTapestryPath(tPath);
        
        replay();
        
        d.render(writer, cycle);
        
        verify();
        
        assertBuffer("<script type=\"text/javascript\">djConfig = {"
                + "\"baseRelativePath\":\"http:///dojo/path\","
                +"\"parseWidgets\":false,\"locale\":\"en-us\"} </script>" + SYSTEM_NEWLINE +
                SYSTEM_NEWLINE + 
                "<script type=\"text/javascript\" src=\"http:///dojo/path/dojo.js\"></script>" + SYSTEM_NEWLINE
                + "<script type=\"text/javascript\">" + SYSTEM_NEWLINE + 
                "dojo.registerModulePath(\"tapestry\", \"/tapestry\");" + SYSTEM_NEWLINE +
                "</script>" + SYSTEM_NEWLINE +
                "<script type=\"text/javascript\" src=\"/tapestry/tapestry.js\"></script>" + SYSTEM_NEWLINE +
                "<script type=\"text/javascript\">" + SYSTEM_NEWLINE +
                "dojo.require(\"tapestry.namespace\");" + SYSTEM_NEWLINE +
                "tapestry.requestEncoding='utf-foo';" + SYSTEM_NEWLINE + 
                "</script>" + SYSTEM_NEWLINE);
    }
    
    public void test_Debug_Render()
    {
        IAsset dojoSource = newAsset();
        IAsset dojoPath = newAsset();
        IAsset tSource = newAsset();
        IAsset tPath = newAsset();
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        IEngine engine = newMock(IEngine.class);
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newBufferWriter();

        expect(cycle.getEngine()).andReturn(engine);
        expect(engine.getOutputEncoding()).andReturn("utf-foo");
        expect(cycle.getPage()).andReturn(page);
        expect(dojoPath.buildURL()).andReturn("http:///dojo/path");

        expect(page.hasFormComponents()).andReturn(false).anyTimes();
        expect(page.hasWidgets()).andReturn(false).anyTimes();

        trainPageLocale(cycle, Locale.UK);
        
        expect(dojoSource.buildURL()).andReturn("http:///dojo/path/dojo.js");
        expect(tPath.buildURL()).andReturn("/tapestry");
        expect(tSource.buildURL()).andReturn("/tapestry/tapestry.js");
        
        AjaxShellDelegate d = new AjaxShellDelegate();
        d.setDojoPath(dojoPath);
        d.setDojoSource(dojoSource);
        d.setTapestrySource(tSource);
        d.setTapestryPath(tPath);
        d.setDebug(true);
        d.setLogLevel(AjaxShellDelegate.BROWSER_LOG_DEBUG);
        d.setConsoleEnabled(true);
        d.setSearchIds("['treeId']");
        
        replay();
        
        d.render(writer, cycle);
        
        verify();
        
        assertBuffer("<script type=\"text/javascript\">djConfig = {\"isDebug\":true,\"baseRelativePath\":\"http:///dojo/path\"," +
                     "\"parseWidgets\":false,\"searchIds\":['treeId'],\"locale\":\"en-gb\"} </script>" + SYSTEM_NEWLINE +
                     SYSTEM_NEWLINE +
                     "<script type=\"text/javascript\" src=\"http:///dojo/path/dojo.js\"></script>" + SYSTEM_NEWLINE +
                     "<script type=\"text/javascript\">" + SYSTEM_NEWLINE +
                     "dojo.require(\"dojo.debug.console\");" + SYSTEM_NEWLINE +
                     "dojo.log.setLevel(dojo.log.getLevel(\"DEBUG\"));" + SYSTEM_NEWLINE +
                     "</script>" + SYSTEM_NEWLINE +
                     "<script type=\"text/javascript\">" + SYSTEM_NEWLINE +
                     "dojo.registerModulePath(\"tapestry\", \"/tapestry\");" + SYSTEM_NEWLINE +
                     "</script>" + SYSTEM_NEWLINE +
                     "<script type=\"text/javascript\" src=\"/tapestry/tapestry.js\"></script>" + SYSTEM_NEWLINE +
                     "<script type=\"text/javascript\">" + SYSTEM_NEWLINE +
                     "dojo.require(\"tapestry.namespace\");" + SYSTEM_NEWLINE +
                     "tapestry.requestEncoding='utf-foo';" + SYSTEM_NEWLINE + 
                     "</script>" + SYSTEM_NEWLINE);
    }
}
