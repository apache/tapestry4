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

package org.apache.tapestry.container;

import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.container.ContainerContextResource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestContainerContextResource extends HiveMindTestCase
{
    private ContainerContext newContext()
    {
        return (ContainerContext) newMock(ContainerContext.class);
    }

    public void testConstructor()
    {
        ContainerContext context = newContext();

        replayControls();

        Resource r = new ContainerContextResource(context, "/foo/bar/baz_en.html", Locale.ENGLISH);

        assertEquals("context:/foo/bar/baz_en.html", r.toString());

        assertEquals("/foo/bar/baz_en.html", r.getPath());

        assertEquals("baz_en.html", r.getName());

        assertEquals(Locale.ENGLISH, r.getLocale());

        verifyControls();
    }

    public void testLocalizationExists() throws Exception
    {
        MockControl control = newControl(ContainerContext.class);
        ContainerContext context = (ContainerContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(new URL("http://foo.com"));

        replayControls();

        Resource r1 = new ContainerContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertEquals("/foo/bar/baz_en.html", r2.getPath());
        assertEquals(Locale.ENGLISH, r2.getLocale());

        verifyControls();
    }

    public void testLocalizationSame() throws Exception
    {
        MockControl control = newControl(ContainerContext.class);
        ContainerContext context = (ContainerContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(null);

        context.getResource("/foo/bar/baz.html");
        control.setReturnValue(new URL("http://foo.com"));

        replayControls();

        Resource r1 = new ContainerContextResource(context, "/foo/bar/baz.html");

        Resource r2 = r1.getLocalization(Locale.ENGLISH);

        assertSame(r2, r1);

        verifyControls();
    }

    public void testLocalizationMissing() throws Exception
    {
        MockControl control = newControl(ContainerContext.class);
        ContainerContext context = (ContainerContext) control.getMock();

        context.getResource("/foo/bar/baz_en.html");
        control.setReturnValue(null);

        context.getResource("/foo/bar/baz.html");
        control.setReturnValue(null);

        replayControls();

        Resource r1 = new ContainerContextResource(context, "/foo/bar/baz.html");

        assertNull(r1.getLocalization(Locale.ENGLISH));

        verifyControls();
    }

    public void testGetRelativeResource()
    {
        ContainerContext context = newContext();

        replayControls();

        Resource r1 = new ContainerContextResource(context, "/foo/bar/baz.html");
        Resource r2 = r1.getRelativeResource("baz.gif");

        assertEquals("/foo/bar/baz.gif", r2.getPath());

        verifyControls();
    }
}