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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.web.WebContextResource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class WebContextResourceTest extends BaseComponentTestCase
{
    private WebContext newContext()
    {
        return newMock(WebContext.class);
    }

    public void testConstructor()
    {
        WebContext context = newContext();

        replay();

        Resource r = new WebContextResource(context, "/foo/bar/baz_en.html", Locale.ENGLISH);

        assertEquals("context:/foo/bar/baz_en.html", r.toString());

        assertEquals("/foo/bar/baz_en.html", r.getPath());

        assertEquals("baz_en.html", r.getName());

        assertEquals(Locale.ENGLISH, r.getLocale());

        verify();
    }

    public void testLocalizationExists() throws Exception
    {
        WebContext context = newContext();

        trainGetResource(context, "/foo/bar/baz_en.html", new URL("http://foo.com"));

        replay();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertEquals("/foo/bar/baz_en.html", r2.getPath());
        assertEquals(Locale.ENGLISH, r2.getLocale());

        verify();
    }

    private void trainGetResource(WebContext context, String path, URL url)
    {
        expect(context.getResource(path)).andReturn(url);
    }

    public void testLocalizationSame() throws Exception
    {
        WebContext context = newContext();

        trainGetResource(context, "/foo/bar/baz_en.html", null);
        trainGetResource(context, "/foo/bar/baz.html", new URL("http://foo.com"));

        replay();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertSame(r2, r1);

        verify();
    }

    public void testLocalizationMissing() throws Exception
    {
        WebContext context = newContext();

        trainGetResource(context, "/foo/bar/baz_en.html", null);
        trainGetResource(context, "/foo/bar/baz.html", null);

        replay();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");

        assertNull(r1.getLocalization(Locale.ENGLISH));

        verify();
    }

    public void testGetRelativeResource()
    {
        WebContext context = newContext();

        replay();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz.html");
        Resource r2 = r1.getRelativeResource("baz.gif");

        assertEquals("/foo/bar/baz.gif", r2.getPath());

        verify();
    }

    public void testGetExtensionlessResource() throws Exception
    {
        WebContext context = newContext();

        trainGetResource(context, "/foo/bar/baz_en", new URL("http://foo.com"));

        replay();

        Resource r1 = new WebContextResource(context, "/foo/bar/baz");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertEquals("/foo/bar/baz_en", r2.getPath());
        assertEquals(Locale.ENGLISH, r2.getLocale());

        verify();
    }
}