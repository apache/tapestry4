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

package org.apache.tapestry.junit;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.junit.mock.MockContext;
import org.apache.tapestry.resource.ContextResourceLocation;

/**
 * A few tests to fill in the code coverage for
 * {@link org.apache.tapestry.IResourceLocation} and its implementations.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 *
 */

public class TestContextResource extends TapestryTestCase
{
    private ServletContext _context = new MockContext();
    private ClassResolver _resolver = new DefaultClassResolver();

    public TestContextResource(String name)
    {
        super(name);
    }

    public void testContextEquals()
    {
        Resource l1 = new ContextResourceLocation(_context, "/images/back.png");
        Resource l2 = new ContextResourceLocation(_context, "/images/back.png");

        assertEquals("Object equality.", l1, l2);

        assertEquals("Hash code", l1.hashCode(), l2.hashCode());
    }

    public void testContextRelativeSameResource()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        Resource l2 = l1.getRelativeResource("Baz");

        assertSame(l1, l2);
    }

    public void testContextRelativeSamePath()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        Resource l2 = l1.getRelativeResource("/foo/bar/Baz");

        assertSame(l1, l2);
    }

    public void testContextRelativeSameFolder()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        Resource expected = new ContextResourceLocation(_context, "/foo/bar/Fubar");
        Resource actual = l1.getRelativeResource("Fubar");

        assertEquals(expected, actual);
    }

    public void testContextRelative()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        Resource expected = new ContextResourceLocation(_context, "/foo/bar/gloop/Yup");
        Resource actual = l1.getRelativeResource("gloop/Yup");

        assertEquals(expected, actual);
    }

    public void testContextAbsolute()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz");
        Resource expected = new ContextResourceLocation(_context, "/bip/bop/Boop");
        Resource actual = l1.getRelativeResource("/bip/bop/Boop");

        assertEquals(expected, actual);
    }

    public void testContextLocalize()
    {
        Resource l1 = new ContextResourceLocation(_context, "/images/back.png");
        Resource expected = new ContextResourceLocation(_context, "/images/back_fr.png");
        Resource actual = l1.getLocalization(Locale.FRANCE);

        assertEquals(expected, actual);
    }

    public void testContextLocalizeDefault()
    {
        Resource l1 = new ContextResourceLocation(_context, "/images/back.png");
        Resource actual = l1.getLocalization(Locale.KOREAN);

        assertSame(l1, actual);
    }

    public void testContextLocalizeNull()
    {
        Resource l1 = new ContextResourceLocation(_context, "/images/back.png");
        Resource actual = l1.getLocalization(null);

        assertSame(l1, actual);
    }

    public void testContextLocalizeMissing()
    {
        Resource l1 = new ContextResourceLocation(_context, "/foo/bar/Baz.zap");

        Resource l2 = l1.getLocalization(Locale.ENGLISH);

        assertNull(l2);
    }
    
	public void testCompareDifferentTypes()
	{
		Resource l1 =
			new ClasspathResource(_resolver, "/org/apache/tapestry/junit/mock/app/home.png");
		Resource l2 =
			new ContextResourceLocation(_context, "/org/apache/tapestry/junit/mock/app/home.png");

		// Same paths, but different classes, should not be equal

		assertTrue(!l1.equals(l2));
	}    
}
