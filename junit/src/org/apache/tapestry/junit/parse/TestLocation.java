/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.junit.parse;

import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Location;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 *  Test the {@link org.apache.tapestry.parse.LocationTag} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestLocation extends TapestryTestCase
{
    private static final IResourceResolver _resolver = new DefaultResourceResolver();
    private static final IResourceLocation _resource1 =
        new ClasspathResourceLocation(_resolver, "/somepackage/somefile");
    private static final IResourceLocation _resource2 =
        new ClasspathResourceLocation(_resolver, "/someotherpackage/someotherfile");

    private IResourceLocation _location = new ContextResourceLocation(null, "/WEB-INF/foo.bar");

    public TestLocation(String name)
    {
        super(name);
    }

    public void testNoNumbers()
    {
        ILocation l = new Location(_location);

        assertSame(_location, l.getResourceLocation());
        assertTrue(l.getLineNumber() <= 0);
        assertTrue(l.getColumnNumber() <= 0);
    }

    public void testToStringShort()
    {
        ILocation l = new Location(_location);

        assertEquals("context:/WEB-INF/foo.bar", l.toString());
    }

    public void testWithLine()
    {
        ILocation l = new Location(_location, 22);

        assertEquals(22, l.getLineNumber());
        assertEquals("context:/WEB-INF/foo.bar, line 22", l.toString());
    }

    public void testWithNumbers()
    {
        ILocation l = new Location(_location, 100, 15);

        assertEquals(100, l.getLineNumber());
        assertEquals(15, l.getColumnNumber());
    }

    public void testToStringLong()
    {
        ILocation l = new Location(_location, 97, 3);

        assertEquals("context:/WEB-INF/foo.bar, line 97, column 3", l.toString());
    }
    public void testEqualsBare()
    {
        ILocation l1 = new Location(_resource1);

        assertEquals(true, l1.equals(new Location(_resource1)));

        assertEquals(false, l1.equals(new Location(_resource2)));
        assertEquals(false, l1.equals(new Location(_resource1, 10)));
    }

    public void testEqualsLineNo()
    {
        ILocation l1 = new Location(_resource1, 10);

        assertEquals(true, l1.equals(new Location(_resource1, 10)));
        assertEquals(false, l1.equals(new Location(_resource1, 11)));
        assertEquals(false, l1.equals(new Location(_resource2, 10)));
        assertEquals(false, l1.equals(new Location(_resource1, 10, 1)));
    }

    public void testEqualsFull()
    {
        ILocation l1 = new Location(_resource1, 10, 5);

        assertEquals(true, l1.equals(new Location(_resource1, 10, 5)));
        assertEquals(false, l1.equals(new Location(_resource1, 10, 6)));
        assertEquals(false, l1.equals(new Location(_resource1, 11, 5)));
        assertEquals(false, l1.equals(new Location(_resource2, 10, 5)));
    }

    public void testHashCodeBare()
    {
        ILocation l1 = new Location(_resource1);
        ILocation l2 = new Location(_resource1);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

    public void testHashCodeLineNo()
    {
        ILocation l1 = new Location(_resource1, 15);
        ILocation l2 = new Location(_resource1, 15);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

    public void testHashCodeFull()
    {
        ILocation l1 = new Location(_resource1, 15, 20);
        ILocation l2 = new Location(_resource1, 15, 20);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

}
