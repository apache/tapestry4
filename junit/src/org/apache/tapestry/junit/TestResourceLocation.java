//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.junit.mock.MockContext;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 *  A few tests to fill in the code coverage for
 *  {@link org.apache.tapestry.IResourceLocation} and its implementations.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestResourceLocation extends TapestryTestCase
{
    private ServletContext _context = new MockContext();
    private IResourceResolver _resolver = new DefaultResourceResolver();

    public void testContextEquals()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/images/back.png");
        IResourceLocation l2 = new ContextResourceLocation(_context, "/images/back.png");

        assertEquals("Object equality.", l1, l2);

        assertEquals("Hash code", l1.hashCode(), l2.hashCode());
    }

    public void testContextRelativeSameResource()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        IResourceLocation l2 = l1.getRelativeLocation("Baz");

        assertSame(l1, l2);
    }

    public void testContextRelativeSamePath()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        IResourceLocation l2 = l1.getRelativeLocation("/foo/bar/Baz");

        assertSame(l1, l2);
    }

    public void testContextRelativeSameFolder()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        IResourceLocation expected = new ContextResourceLocation(_context, "/foo/bar/Fubar");
        IResourceLocation actual = l1.getRelativeLocation("Fubar");

        assertEquals(expected, actual);
    }

    public void testContextRelative()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        IResourceLocation expected = new ContextResourceLocation(_context, "/foo/bar/gloop/Yup");
        IResourceLocation actual = l1.getRelativeLocation("gloop/Yup");

        assertEquals(expected, actual);
    }

    public void testContextAbsolute()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        IResourceLocation expected = new ContextResourceLocation(_context, "/bip/bop/Boop");
        IResourceLocation actual = l1.getRelativeLocation("/bip/bop/Boop");

        assertEquals(expected, actual);
    }

    public void testContextLocalize()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/images/back.png");
        IResourceLocation expected = new ContextResourceLocation(_context, "/images/back_fr.png");
        IResourceLocation actual = l1.getLocalization(Locale.FRANCE);

        assertEquals(expected, actual);
    }

    public void testContextLocalizeDefault()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/images/back.png");
        IResourceLocation actual = l1.getLocalization(Locale.KOREAN);

        assertSame(l1, actual);
    }

    public void testContextLocalizeNull()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/images/back.png");
        IResourceLocation actual = l1.getLocalization(null);

        assertSame(l1, actual);
    }

    public void testClasspathEquals()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");
        IResourceLocation l2 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");

        assertEquals("Object equality.", l1, l2);

        assertEquals("Hash code", l1.hashCode(), l2.hashCode());
    }

    public void testClasspathRelativeSameResource()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz");
        IResourceLocation l2 = l1.getRelativeLocation("Baz");

        assertSame(l1, l2);
    }

    public void testClasspathRelativeSameFolder()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz");
        IResourceLocation expected = new ClasspathResourceLocation(_resolver, "/foo/bar/Fubar");
        IResourceLocation actual = l1.getRelativeLocation("Fubar");

        assertEquals(expected, actual);
    }

    public void testClasspathRelative()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz");
        IResourceLocation expected = new ClasspathResourceLocation(_resolver, "/foo/bar/gloop/Yup");
        IResourceLocation actual = l1.getRelativeLocation("gloop/Yup");

        assertEquals(expected, actual);
    }

    public void testClasspathAbsolute()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz");
        IResourceLocation expected = new ClasspathResourceLocation(_resolver, "/bip/bop/Boop");
        IResourceLocation actual = l1.getRelativeLocation("/bip/bop/Boop");

        assertEquals(expected, actual);
    }

    public void testClasspathLocalize()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");
        IResourceLocation expected =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home_fr.png");
        IResourceLocation actual = l1.getLocalization(Locale.FRANCE);

        assertEquals(expected, actual);
    }

    public void testClasspathLocalizeMissing()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz.zap");

        IResourceLocation l2 = l1.getLocalization(Locale.ENGLISH);

        assertNull(l2);
    }

    public void testContextLocalizeMissing()
    {
        IResourceLocation l1 = new ContextResourceLocation(_context, "/foo/bar/Baz.zap");

        IResourceLocation l2 = l1.getLocalization(Locale.ENGLISH);

        assertNull(l2);
    }

    public void testClasspathLocalizeDefault()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");
        IResourceLocation actual = l1.getLocalization(Locale.KOREAN);

        assertSame(l1, actual);
    }

    public void testClasspathLocalizeNull()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");
        IResourceLocation actual = l1.getLocalization(null);

        assertSame(l1, actual);
    }

    public void testEqualsNull()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");

        assertTrue(!l1.equals(null));
    }

    public void testCompareDifferentTypes()
    {
        IResourceLocation l1 =
            new ClasspathResourceLocation(
                _resolver,
                "/org/apache/tapestry/junit/mock/app/home.png");
        IResourceLocation l2 =
            new ContextResourceLocation(_context, "/org/apache/tapestry/junit/mock/app/home.png");

        // Same paths, but different classes, should not be equal

        assertTrue(!l1.equals(l2));
    }

    public void testClasspathRelativeSamePath()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/foo/bar/Baz");
        IResourceLocation l2 = l1.getRelativeLocation("/foo/bar/Baz");

        assertSame(l1, l2);
    }

    public void testTrailingSlash()
    {
        IResourceLocation l1 = new ClasspathResourceLocation(_resolver, "/");
        IResourceLocation expected = new ClasspathResourceLocation(_resolver, "/foo");
        IResourceLocation actual = l1.getRelativeLocation("foo");

        assertEquals(expected, actual);
    }
}
