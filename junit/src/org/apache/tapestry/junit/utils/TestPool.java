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

package org.apache.tapestry.junit.utils;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.pool.IPoolable;
import org.apache.tapestry.util.pool.IPoolableAdaptor;
import org.apache.tapestry.util.pool.Pool;

/**
 *  Tests {@link org.apache.tapestry.util.pool.Pool}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestPool extends TapestryTestCase
{
    public static class OrdinaryBean
    {
    }

    public static class BadBean
    {
        private BadBean()
        {
        }
    }

    public static class NotifyBean implements IPoolable
    {
        public boolean _reset;
        public boolean _discarded;

        public void discardFromPool()
        {
            _discarded = true;
        }

        public void resetForPool()
        {
            _reset = true;
        }
    }

    public static class AdaptedBean
    {
        public boolean _reset;
        public boolean _discarded;
    }

    public static class Adaptor implements IPoolableAdaptor
    {
        public void discardFromPool(Object object)
        {
            AdaptedBean bean = (AdaptedBean) object;

            bean._discarded = true;
        }

        public void resetForPool(Object object)
        {
            AdaptedBean bean = (AdaptedBean) object;

            bean._reset = true;
        }

    }

    public void testStore()
    {
        Pool p = new Pool(false);

        assertEquals(0, p.getKeyCount());
        assertEquals(0, p.getPooledCount());

        String key = "KEY";
        String value = "VALUE";

        p.store(key, value);

        assertEquals(1, p.getKeyCount());
        assertEquals(1, p.getPooledCount());
    }

    public void testStoreAndRetrieve()
    {
        String key = "KEY";
        Pool p = new Pool(false);

        for (int i = 0; i < 3; i++)
            p.store(key, new OrdinaryBean());

        Object last = null;

        for (int i = 0; i < 4; i++)
        {
            Object bean = p.retrieve(key);

            if (i == 3)
                assertNull(bean);
            else
                assertNotNull(bean);

            assertTrue("Different bean than last retrieved. ", !(last == bean));

            last = bean;
        }
    }

    public void testNotifications()
    {
        String key = "KEY";

        Pool p = new Pool(false);
        p.setWindow(1);

        NotifyBean bean = new NotifyBean();

        assertEquals(false, bean._reset);
        assertEquals(false, bean._discarded);

        p.store(key, bean);

        assertEquals(true, bean._reset);
        assertEquals(false, bean._discarded);

        p.executeCleanup();

        assertTrue(bean._discarded);

        assertNull(p.retrieve(key));
    }

    public void testAdaptedNotifications()
    {
        String key = "KEY";

        Pool p = new Pool(false);
        p.registerAdaptor(AdaptedBean.class, new Adaptor());
        p.setWindow(1);

        AdaptedBean bean = new AdaptedBean();

        assertEquals(false, bean._reset);
        assertEquals(false, bean._discarded);

        p.store(key, bean);

        assertEquals(true, bean._reset);
        assertEquals(false, bean._discarded);

        p.executeCleanup();

        assertTrue(bean._discarded);

        assertNull(p.retrieve(key));
    }

    public void testClear()
    {
        String key = "KEY";

        Pool p = new Pool(false);
        p.setWindow(1);

        NotifyBean[] bean = new NotifyBean[3];

        for (int i = 0; i < 3; i++)
        {
            bean[i] = new NotifyBean();

            p.store(key, bean[i]);
        }

        assertEquals(3, p.getPooledCount());

        p.clear();

        for (int i = 0; i < 3; i++)
            assertEquals(true, bean[i]._discarded);

        assertEquals(0, p.getPooledCount());

        assertNull(p.retrieve(key));
    }

    /**
     *  Test retrieve and store of StringBuffer.
     * 
     **/

    public void testStringBuffer()
    {
        Pool p = new Pool(false);

        StringBuffer buffer = (StringBuffer) p.retrieve(StringBuffer.class);

        assertNotNull(buffer);

        buffer.append("foo");

        p.store(buffer);

        assertEquals(0, buffer.length());

        StringBuffer buffer2 = (StringBuffer) p.retrieve(StringBuffer.class);

        assertSame(buffer, buffer2);

        StringBuffer buffer3 = (StringBuffer) p.retrieve(StringBuffer.class);

        assertEquals(false, buffer == buffer3);
    }

    /**
     *  Test retrieve of a bean that can't be instantiated.
     * 
     **/

    public void testBadBean()
    {
        Pool p = new Pool(false);

        try
        {
            p.retrieve(BadBean.class);

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(
                ex,
                "Unable to instantiate new instance of class org.apache.tapestry.junit.utils.TestPool$BadBean.");
        }
    }
}
