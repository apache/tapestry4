/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.Tapestry;

/**
 *
 *  Tests for the {@link org.apache.tapestry.Tapestry#coerceToIterator(Object)} method.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TestTapestryCoerceToIterator extends TapestryTestCase
{

    public TestTapestryCoerceToIterator(String name)
    {
        super(name);
    }

    private void check(Object[] expected, Object input)
    {
        Iterator i = Tapestry.coerceToIterator(input);

        if (expected == null)
        {
            assertEquals("Empty input should yield null output", null, i);
            return;
        }

        List l = new ArrayList();

        while (i.hasNext())
            l.add(i.next());

        assertEquals("Number of results", expected.length, l.size());

        for (int x = 0; x < expected.length; x++)
            assertEquals("[" + x + "]", expected[x], l.get(x));
    }

    public void testNull()
    {
        check(null, null);
    }

    public void testEmptyList()
    {
        check(null, new ArrayList());
    }

    public void testEmptyArray()
    {
        check(null, new Object[0]);
    }

    public void testIterator()
    {
        List l = new ArrayList();
        l.add("fred");
        l.add("barney");

        check(new String[] { "fred", "barney" }, l.iterator());
    }

    public void testObjectArray()
    {
        Object[] a = new String[] { "fred", "barney", "wilma" };

        check(a, a);
    }

    public void testObject()
    {
        String object = "manifest-destiny";

        check(new Object[] { object }, object);
    }

    public void testBooleanArray()
    {
        boolean[] input = new boolean[] { true, false, true };

        Boolean[] expected = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };

        check(expected, input);
    }

    public void testEmptyBooleanArray()
    {
        check(null, new boolean[0]);
    }

    public void testByteArray()
    {
        byte[] input = new byte[] { 1, 3, 7 };

        Byte[] expected = new Byte[] { new Byte("1"), new Byte("3"), new Byte("7")};

        check(expected, input);
    }

    public void testEmptyByteArray()
    {
        check(null, new byte[0]);
    }

    public void testCharArray()
    {
        char[] input = new char[] { 'b', 'k' };

        Character[] expected = new Character[] { new Character('b'), new Character('k')};

        check(expected, input);
    }

    public void testEmptyCharArray()
    {
        check(null, new char[0]);
    }

    public void testShortArray()
    {
        short[] input = new short[] { 23, 98 };

        Short[] expected = new Short[] { new Short("23"), new Short("98")};

        check(expected, input);
    }

    public void testEmptyShortArray()
    {
        check(null, new short[0]);
    }

    public void testIntArray()
    {
        int[] input = new int[] { 55, 12873, 12 };

        Integer[] expected = new Integer[] { new Integer(55), new Integer(12873), new Integer(12)};

        check(expected, input);
    }

    public void testEmptyIntArray()
    {
        check(null, new int[0]);
    }

    public void testLongArray()
    {
        long[] input = new long[] { 27l, 191933939l };

        Long[] expected = new Long[] { new Long(27l), new Long(191933939l)};

        check(expected, input);
    }

    public void testEmptyLongArray()
    {
        check(null, new long[0]);
    }

    public void testFloatArray()
    {
        float[] input = new float[] { 3.14f, 2.25f };

        Float[] expected = new Float[] { new Float("3.14"), new Float("2.25")};

        check(expected, input);
    }

    public void testEmptyFloatArray()
    {
        check(null, new float[0]);
    }

    public void testDoubleArray()
    {
        double[] input = new double[] { 18.55, -97.123 };

        Double[] expected = new Double[] { new Double("18.55"), new Double(" -97.123")};

        check(expected, input);
    }

    public void testEmptyDoubleArray()
    {
        check(null, new double[0]);
    }
}
