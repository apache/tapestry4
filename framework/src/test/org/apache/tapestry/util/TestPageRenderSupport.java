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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.util.PageRenderSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPageRenderSupport extends HiveMindTestCase
{
    private IEngineService newService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private ILink newLink(String URL)
    {
        MockControl control = newControl(ILink.class);
        ILink link = (ILink) control.getMock();

        link.getURL();
        control.setReturnValue(URL);

        return link;
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
        IEngineService service = newService();
        Location l = fabricateLocation(99);

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(service, l);

        assertSame(l, prs.getLocation());

        verifyControls();
    }

    public void testGetPreloadedImageReference()
    {
        IEngineService service = newService();
        Location l = fabricateLocation(99);
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(service, l);

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

    public void testAddBodyScript()
    {
        IEngineService service = newService();
        Location l = fabricateLocation(99);
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(service, l);

        prs.addBodyScript("myBodyScript();");

        prs.writeBodyScript(writer, cycle);

        assertOutput(new String[]
        { "<script language=\"JavaScript\" type=\"text/javascript\"><!--", "", "myBodyScript();",
                "", "// --></script>" });

        verifyControls();
    }

    public void testGetUniqueValue()
    {
        IEngineService service = newService();
        Location l = fabricateLocation(99);

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(service, l);

        assertEquals("foo", prs.getUniqueString("foo"));
        assertEquals("foo$0", prs.getUniqueString("foo"));
        assertEquals("bar", prs.getUniqueString("bar"));
        assertEquals("foo$1", prs.getUniqueString("foo"));

        verifyControls();
    }

    public void testAddInitializationScript()
    {
        IEngineService service = newService();
        Location l = fabricateLocation(99);
        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(service, l);

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

        Location l = fabricateLocation(22);
        ClassResolver resolver = new DefaultClassResolver();
        Resource filea = new ClasspathResource(resolver, "org/apache/tapestry/utils/filea.txt");
        Resource fileb = new ClasspathResource(resolver, "org/apache/tapestry/utils/fileb.txt");

        MockControl assetServicec = newControl(IEngineService.class);
        IEngineService assetService = (IEngineService) assetServicec.getMock();

        IRequestCycle cycle = newCycle();

        assetService.getLink(cycle, "org/apache/tapestry/utils/filea.txt");
        assetServicec.setReturnValue(newLink("/app?filea.txt"));

        assetService.getLink(cycle, "org/apache/tapestry/utils/fileb.txt");
        assetServicec.setReturnValue(newLink("/app?fileb.txt"));

        IMarkupWriter writer = newWriter();

        replayControls();

        PageRenderSupportImpl prs = new PageRenderSupportImpl(assetService, l);

        prs.addExternalScript(filea);
        prs.addExternalScript(fileb);
        prs.addExternalScript(filea);

        prs.writeBodyScript(writer, cycle);

        // PageRenderSupport is a little sloppy about using \n for a newline, vs. using
        // the property line seperator sequence and it bites us right here.

        assertOutput(scriptTagFor("/app?filea.txt") + newline + scriptTagFor("/app?fileb.txt")
                + newline);

        verifyControls();
    }

    private String scriptTagFor(String url)
    {
        return "<script language=\"JavaScript\" type=\"text/javascript\" src=\"" + url
                + "\"></script>";
    }
}