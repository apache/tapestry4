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

package org.apache.tapestry.web;

import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.web.WebContextResource;
import org.apache.tapestry.web.WebContext;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.WebContextResource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestWebContextResource extends HiveMindTestCase
{
    private WebContext newContext()
    {
        return (WebContext) newMock(WebContext.class);
    }

    public void testConstructor()
    {
        WebContext context = newContext();

        replayControls();

        Resource r = new WebContextResource(context, "/foo/bar/baz_en.html", Locale.ENGLISH);

        assertEquals("context:/foo/bar/baz_en.html", r.toString());

        assertEquals("/foo/bar/baz_en.html", r.getPath());

        assertEquals("baz_en.html", r.getName());

        assertEquals(Locale.ENGLISH, r.getLocale());

        verifyControls();
    }

    public void testLocalizationExists() throws Exception
    {
        MockControl control = newControl(WebContext.class);
        WebContext context = (WebContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(new URL("http://foo.com"));

        replayControls();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertEquals("/foo/bar/baz_en.html", r2.getPath());
        assertEquals(Locale.ENGLISH, r2.getLocale());

        verifyControls();
    }

    public void testLocalizationSame() throws Exception
    {
        MockControl control = newControl(WebContext.class);
        WebContext context = (WebContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(null);

        context.getResource("/foo/bar/baz.html");
        control.setReturnValue(new URL("http://foo.com"));

        replayControls();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertSame(r2, r1);

        verifyControls();
    }

    public void testLocalizationMissing() throws Exception
    {
        MockControl control = newControl(WebContext.class);
        WebContext context = (WebContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(null);

        context.getResource("/foo/bar/baz.html");
        control.setReturnValue(null);

        replayControls();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        assertNull(r1.getLocalization(Locale.ENGLISH));

        verifyControls();
    }

    public void testGetRelativeResource()
    {
        WebContext context = newContext();

        replayControls();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");
        Resource r2 = r1.getRelativeResource("baz.gif");

        assertEquals("/foo/bar/baz.gif", r2.getPath());

        verifyControls();
    }
}