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

import java.util.Locale;
import org.apache.tapestry.IPage;
import static org.easymock.EasyMock.*;

import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.testng.annotations.Test;


/**
 * Tests basic functionality of {@link AjaxShellDelegate}.
 * 
 * @author jkuhnert
 */
@Test
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
        expect(link.getAbsoluteURL()).andReturn("http://" + path);
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
        IEngineService assetService = newEngineService();
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newBufferWriter();
        
        trainStaticPath(assetService, dojoPath, "/dojo/path");
        
        trainPageLocale(cycle, Locale.US);
        
        trainStaticPath(assetService, dojoSource, "/dojo/path/dojo.js");
        
        trainStaticPath(assetService, tSource, "/tapestry/tapestry.js");
        
        AjaxShellDelegate d = new AjaxShellDelegate();
        d.setAssetService(assetService);
        d.setDojoPath(dojoPath);
        d.setDojoSource(dojoSource);
        d.setTapestrySource(tSource);
        
        replay();
        
        d.render(writer, cycle);
        
        verify();
        
        assertBuffer("<script type=\"text/javascript\">djConfig = {\"isDebug\":false,"
                + "\"debugAtAllCosts\":false,\"baseRelativePath\":\"http:///dojo/path\","
                +"\"preventBackButtonFix\":false,\"parseWidgets\":false,\"locale\":\"en-us\"} </script>\n" + 
                "\n" + 
                " <script type=\"text/javascript\" src=\"http:///dojo/path/dojo.js\"></script>"
                +"<script type=\"text/javascript\" src=\"http:///tapestry/tapestry.js\"></script>\n" + 
                "<script type=\"text/javascript\">\n" + 
                "dojo.require(\"dojo.logging.Logger\");\n" + 
                "dojo.log.setLevel(dojo.log.getLevel(\"WARNING\"));\n" + 
                "dojo.require(\"tapestry.namespace\")\n" + 
        "</script>" + SYSTEM_NEWLINE);
    }
    
    public void test_Debug_Render()
    {
        IAsset dojoSource = newAsset();
        IAsset dojoPath = newAsset();
        IAsset tSource = newAsset();
        IEngineService assetService = newEngineService();
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newBufferWriter();
        
        trainStaticPath(assetService, dojoPath, "/dojo/path");
        
        trainPageLocale(cycle, Locale.UK);
        
        trainStaticPath(assetService, dojoSource, "/dojo/path/dojo.js");
        
        trainStaticPath(assetService, tSource, "/tapestry/tapestry.js");
        
        AjaxShellDelegate d = new AjaxShellDelegate();
        d.setAssetService(assetService);
        d.setDojoPath(dojoPath);
        d.setDojoSource(dojoSource);
        d.setTapestrySource(tSource);
        d.setLogLevel(AjaxShellDelegate.BROWSER_LOG_DEBUG);
        d.setConsoleEnabled(true);
        
        replay();
        
        d.render(writer, cycle);
        
        verify();
        
        assertBuffer("<script type=\"text/javascript\">djConfig = {\"isDebug\":false,"
                + "\"debugAtAllCosts\":false,\"baseRelativePath\":\"http:///dojo/path\","
                +"\"preventBackButtonFix\":false,\"parseWidgets\":false,\"locale\":\"en-gb\"} </script>\n" + 
                "\n" + 
                " <script type=\"text/javascript\" src=\"http:///dojo/path/dojo.js\"></script>"
                +"<script type=\"text/javascript\" src=\"http:///tapestry/tapestry.js\"></script>\n" + 
                "<script type=\"text/javascript\">\n" + 
                "dojo.require(\"dojo.debug.console\");\n" + 
                "dojo.log.setLevel(dojo.log.getLevel(\"DEBUG\"));\n" + 
                "dojo.require(\"tapestry.namespace\")\n" + 
        "</script>" + SYSTEM_NEWLINE);
    }
}
