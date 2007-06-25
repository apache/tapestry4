// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.util;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.impl.DefaultResponseBuilder;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.util.PageRenderSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential=true)
@SuppressWarnings("all")
public class TestPageRenderSupport extends BaseComponentTestCase
{
    private static final String SYSTEM_NEWLINE= (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    
    private static final String TEST_NEWLINE= "\n";
    
    private AssetFactory newAssetFactory()
    {
        return newMock(AssetFactory.class);
    }

    private IAsset newAsset(String url)
    {
        IAsset asset = newMock(IAsset.class);
        checkOrder(asset, false);
        
        expect(asset.buildURL()).andReturn(url);

        return asset;
    }

    private CharArrayWriter _writer;

    private IMarkupWriter createWriter()
    {
        _writer = new CharArrayWriter();

        return new MarkupWriterImpl("text/html", new PrintWriter(_writer), new AsciiMarkupFilter());
    }

    private ResponseBuilder newBuilder(IMarkupWriter writer)
    {
        return new DefaultResponseBuilder(writer);
    }
    
    private void assertOutput(String[] expectedLines)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < expectedLines.length; i++)
        {
            // Note: PageRenderSupport is a bit sloppy; a lot of code just uses \n for
            // a newline seperator, other parts go through IMarkupWriter.println() and get
            // a proper newline seperator (which may be different).

            if (i > 0)
                buffer.append("\n");

            buffer.append(expectedLines[i]);
        }

        assertOutput(buffer.toString());
    }

    private void assertOutput(String expected)
    {
        String actual = _writer.toString();
        
        // Replace any system line feeds with \n
        actual = actual.replaceAll(SYSTEM_NEWLINE, TEST_NEWLINE);
        
        assertEquals(actual, expected);

        _writer.reset();
    }

    public void testGetLocation()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l, newBuilder(null));

        assertSame(l, prs.getLocation());

        verify();
    }

    public void testGetPreloadedImageReference()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = createWriter();
        
        IComponent comp = newMock(IComponent.class);
        IAsset img = newAsset("/zip/zap.png");
        
        replay();
        
        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l, newBuilder(writer));
        
        assertEquals(prs.getPreloadedImageReference("/foo/bar.gif"), "tapestry.preload[0].src");
        assertEquals(prs.getPreloadedImageReference(comp, img), "tapestry.preload[1].src");
        assertEquals(prs.getPreloadedImageReference("/foo/bar.gif"), "tapestry.preload[0].src");
        
        prs.addBodyScript("myBodyScript();");

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script type=\"text/javascript\"><!--","",
                "dojo.addOnLoad(function(e) {",
                "tapestry.preload = [];", "if (document.images)", "{",
                "tapestry.preload[0] = new Image();",
                "  tapestry.preload[0].src = \"/foo/bar.gif\";",
                "  tapestry.preload[1] = new Image();",
                "  tapestry.preload[1].src = \"/zip/zap.png\";}", "});myBodyScript();",
                "// --></script>" });

        verify();
    }

    public void testPreloadedImagesInNamespace()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = createWriter();

        replay();
        
        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "NAMESPACE.", l, newBuilder(writer));
        
        assertEquals(prs.getPreloadedImageReference("/foo/bar.gif"), "NAMESPACE.preload[0].src");
        
        prs.writeBodyScript(writer, cycle);
        
        assertOutput(new String[]
        {"<script type=\"text/javascript\"><!--", "",
                "dojo.addOnLoad(function(e) {",
                "NAMESPACE.preload = [];", "if (document.images)", "{",
                "NAMESPACE.preload[0] = new Image();",
                "  NAMESPACE.preload[0].src = \"/foo/bar.gif\";}",
                "});",
                "// --></script>" });

        verify();
    }

    public void testAddBodyScript()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = createWriter();

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l, newBuilder(writer));

        prs.addBodyScript("myBodyScript();");

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script type=\"text/javascript\"><!--",
                "myBodyScript();",
                "// --></script>" });

        verify();
    }

    public void testGetUniqueString()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l, newBuilder(null));

        assertEquals("foo", prs.getUniqueString("foo"));
        assertEquals("foo_0", prs.getUniqueString("foo"));
        assertEquals("bar", prs.getUniqueString("bar"));
        assertEquals("foo_1", prs.getUniqueString("foo"));

        verify();
    }

    public void testGetUniqueStringWithNamespace()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "NAMESPACE", l, newBuilder(null));

        assertEquals("fooNAMESPACE", prs.getUniqueString("foo"));
        assertEquals("fooNAMESPACE_0", prs.getUniqueString("foo"));
        assertEquals("barNAMESPACE", prs.getUniqueString("bar"));
        assertEquals("fooNAMESPACE_1", prs.getUniqueString("foo"));

        verify();
    }
    
    public void testAddInitializationScript()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IMarkupWriter writer = createWriter();

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l, newBuilder(writer));

        prs.addInitializationScript("myInitializationScript1();");
        prs.addInitializationScript("myInitializationScript2();");
        prs.addInitializationScript("dojo.require(\"dojo.event.*\");");
        prs.addInitializationScript("dojo.require(\"dojo.widget.*\");dojo.require(\"dojo.event.*\");");
        prs.addInitializationScript("dojo.require(\"dojo.event.*\");");
        prs.addInitializationScript("dojo.require(\"tapestry.form\");tapestry.form.registerForm(\"valid\");\n" + 
                "tapestry.form.focusField(\'inputEnabled\');\n" + 
                "dojo.require(\"tapestry.form\");tapestry.form.registerForm(\"form\");");
        
        prs.writeInitializationScript(writer);
        
        assertOutput(new String[]
        { "<script type=\"text/javascript\"><!--",
                "dojo.addOnLoad(function(e) {",
                "myInitializationScript1();", "myInitializationScript2();",
                "dojo.require(\"dojo.event.*\");","dojo.require(\"dojo.widget.*\");",
                "",
                "tapestry.form.registerForm(\"valid\");",
                "tapestry.form.focusField(\'inputEnabled\');",
                "dojo.require(\"tapestry.form\");tapestry.form.registerForm(\"form\");});",
                "// --></script>" });

        verify();
    }

    public void testAddExternalScript() throws Exception
    {
        String newline = TEST_NEWLINE;

        IRequestCycle cycle = newCycle();
        
        IMarkupWriter writer = createWriter();
        
        AssetFactory assetFactory = newMock(AssetFactory.class);
        
        Resource script1 = newResource();
        IAsset asset1 = newAsset("/script1.js");
        
        Resource script2 = newResource();
        IAsset asset2 = newAsset("/script2.js");
        
        expect(assetFactory.createAsset(script1, null)).andReturn(asset1);

        expect(assetFactory.createAsset(script2, null)).andReturn(asset2);

        replay();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(assetFactory, "", null, newBuilder(writer));

        prs.addExternalScript(script1);
        prs.addExternalScript(script2);
        prs.addExternalScript(script1);

        prs.writeBodyScript(writer, cycle);

        // PageRenderSupport is a little sloppy about using \n for a newline, vs. using
        // the property line seperator sequence and it bites us right here.

        assertOutput(scriptTagFor("/script1.js") + newline + scriptTagFor("/script2.js") + newline);

        verify();
    }

    private String scriptTagFor(String url)
    {
        return "<script type=\"text/javascript\" src=\"" + url
                + "\"></script>";
    }
}