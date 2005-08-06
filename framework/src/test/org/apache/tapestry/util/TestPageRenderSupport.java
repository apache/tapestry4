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

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.util.PageRenderSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPageRenderSupport extends HiveMindTestCase
{
    private AssetFactory newAssetFactory()
    {
        return (AssetFactory) newMock(AssetFactory.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private Resource newResource()
    {
        return (Resource) newMock(Resource.class);
    }

    private IAsset newAsset(IRequestCycle cycle, String url)
    {
        MockControl control = newControl(IAsset.class);
        IAsset asset = (IAsset) control.getMock();

        asset.buildURL(cycle);
        control.setReturnValue(url);

        return asset;
    }

    private CharArrayWriter _writer;

    private IMarkupWriter newWriter()
    {
        _writer = new CharArrayWriter();

        return new MarkupWriterImpl("text/html", new PrintWriter(_writer), new AsciiMarkupFilter());
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

        assertEquals(expected, actual);

        _writer.reset();
    }

    public void testGetLocation()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l);

        assertSame(l, prs.getLocation());

        verifyControls();
    }

    public void testGetPreloadedImageReference()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l);

        assertEquals("tapestry_preload[0].src", prs.getPreloadedImageReference("/foo/bar.gif"));
        assertEquals("tapestry_preload[1].src", prs.getPreloadedImageReference("/zip/zap.png"));
        assertEquals("tapestry_preload[0].src", prs.getPreloadedImageReference("/foo/bar.gif"));

        prs.addBodyScript("myBodyScript();");

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script language=\"JavaScript\" type=\"text/javascript\"><!--", "",
                "var tapestry_preload = new Array();", "if (document.images)", "{",
                "  tapestry_preload[0] = new Image();",
                "  tapestry_preload[0].src = \"/foo/bar.gif\";",
                "  tapestry_preload[1] = new Image();",
                "  tapestry_preload[1].src = \"/zip/zap.png\";", "}", "", "", "myBodyScript();",
                "", "// --></script>" });

        verifyControls();
    }

    public void testPreloadedImagesInNamespace()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "NAMESPACE", l);

        assertEquals("NAMESPACE_preload[0].src", prs.getPreloadedImageReference("/foo/bar.gif"));

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script language=\"JavaScript\" type=\"text/javascript\"><!--", "",
                "var NAMESPACE_preload = new Array();", "if (document.images)", "{",
                "  NAMESPACE_preload[0] = new Image();",
                "  NAMESPACE_preload[0].src = \"/foo/bar.gif\";", "}", "", "", "// --></script>" });

        verifyControls();
    }

    public void testAddBodyScript()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l);

        prs.addBodyScript("myBodyScript();");

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script language=\"JavaScript\" type=\"text/javascript\"><!--", "", "myBodyScript();",
                "", "// --></script>" });

        verifyControls();
    }

    public void testGetUniqueString()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l);

        assertEquals("foo", prs.getUniqueString("foo"));
        assertEquals("foo$0", prs.getUniqueString("foo"));
        assertEquals("bar", prs.getUniqueString("bar"));
        assertEquals("foo$1", prs.getUniqueString("foo"));

        verifyControls();
    }

    public void testGetUniqueStringWithNamespace()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "NAMESPACE", l);

        assertEquals("fooNAMESPACE", prs.getUniqueString("foo"));
        assertEquals("fooNAMESPACE$0", prs.getUniqueString("foo"));
        assertEquals("barNAMESPACE", prs.getUniqueString("bar"));
        assertEquals("fooNAMESPACE$1", prs.getUniqueString("foo"));

        verifyControls();
    }

    public void testAddInitializationScript()
    {
        AssetFactory factory = newAssetFactory();
        Location l = newLocation();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(factory, "", l);

        prs.addInitializationScript("myInitializationScript1();");
        prs.addInitializationScript("myInitializationScript2();");

        prs.writeInitializationScript(writer);

        assertOutput(new String[]
        { "<script language=\"JavaScript\" type=\"text/javascript\"><!--",
                "myInitializationScript1();", "myInitializationScript2();", "", "// --></script>" });

        verifyControls();
    }

    public void testAddExternalScript() throws Exception
    {
        String newline = System.getProperty("line.separator");

        IRequestCycle cycle = newCycle();

        IMarkupWriter writer = newWriter();

        MockControl assetFactoryc = newControl(AssetFactory.class);
        AssetFactory assetFactory = (AssetFactory) assetFactoryc.getMock();

        Resource script1 = newResource();
        IAsset asset1 = newAsset(cycle, "/script1.js");
        Resource script2 = newResource();
        IAsset asset2 = newAsset(cycle, "/script2.js");

        assetFactory.createAsset(script1, null);
        assetFactoryc.setReturnValue(asset1);

        assetFactory.createAsset(script2, null);
        assetFactoryc.setReturnValue(asset2);

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(assetFactory, "", null);

        prs.addExternalScript(script1);
        prs.addExternalScript(script2);
        prs.addExternalScript(script1);

        prs.writeBodyScript(writer, cycle);

        // PageRenderSupport is a little sloppy about using \n for a newline, vs. using
        // the property line seperator sequence and it bites us right here.

        assertOutput(scriptTagFor("/script1.js") + newline + scriptTagFor("/script2.js") + newline);

        verifyControls();
    }

    private String scriptTagFor(String url)
    {
        return "<script language=\"JavaScript\" type=\"text/javascript\" src=\"" + url
                + "\"></script>";
    }
}