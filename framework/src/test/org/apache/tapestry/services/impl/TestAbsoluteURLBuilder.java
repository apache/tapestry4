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

package org.apache.tapestry.services.impl;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.AbsoluteURLBuilderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAbsoluteURLBuilder extends TapestryTestCase
{
    private void attempt(String expected, String URI, String scheme, String server, int port)
    {
        AbsoluteURLBuilder b = new AbsoluteURLBuilderImpl();

        String actual = b.constructURL(URI, scheme, server, port);

        assertEquals(expected, actual);
    }

    private void attemptDefault(String expected, String URI, String scheme, String server, int port)
    {
        MockControl control = newControl(WebRequest.class);
        WebRequest request = (WebRequest) control.getMock();

        request.getScheme();
        control.setReturnValue(scheme);

        request.getServerName();
        control.setReturnValue(server);

        request.getServerPort();
        control.setReturnValue(port);

        replayControls();

        AbsoluteURLBuilderImpl b = new AbsoluteURLBuilderImpl();
        b.setRequest(request);

        String actual = b.constructURL(URI);

        assertEquals(expected, actual);

        verifyControls();
    }

    public void testURIIncludesServer()
    {
        attempt("https://foo/bar/baz", "//foo/bar/baz", "https", "SERVER", 100);
    }

    public void testPortZero()
    {
        attempt("http://foo/bar/baz", "/bar/baz", "http", "foo", 0);
    }

    public void testNoLeadingSlash()
    {
        attempt("http://foo/bar/baz", "bar/baz", "http", "foo", 0);
    }

    public void testPortNonZero()
    {
        attempt("http://foo.com:1024/bar/baz", "/bar/baz", "http", "foo.com", 1024);
    }

    public void testDefaultsForPort80()
    {
        attemptDefault("http://foo/bar/baz", "/bar/baz", "http", "foo", 80);
    }

    public void testDefault()
    {
        attemptDefault("http://foo:8080/bar/baz", "/bar/baz", "http", "foo", 8080);
    }
}