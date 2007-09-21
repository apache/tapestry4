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

package org.apache.tapestry.asset;

import static org.easymock.EasyMock.expect;

import java.net.URL;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.asset.ResourceDigestSourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestResourceDigestSource extends BaseComponentTestCase
{
    public void testSuccess()
    {
        ResourceDigestSourceImpl s = new ResourceDigestSourceImpl();
        s.setClassResolver(new DefaultClassResolver());

        assertEquals("a5f4663532ea3efe22084df086482290", s
                .getDigestForResource("/org/apache/tapestry/asset/tapestry-in-action.png"));
    }

    public void testMissing()
    {
        ResourceDigestSourceImpl s = new ResourceDigestSourceImpl();
        s.setClassResolver(new DefaultClassResolver());

        try
        {
            s.getDigestForResource("/foo/bar/baz");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Classpath resource '/foo/bar/baz' does not exist.", ex.getMessage());
        }
    }

    public void testFolderInJar()
    {
        ResourceDigestSourceImpl s = new ResourceDigestSourceImpl();
        s.setClassResolver(new DefaultClassResolver());
        // the next will throw a NPE inside JarURLInputStream.close
        // there doesn't seem to be a way to prevent this - so users
        // of this method should be aware
        try
        {
            s.getDigestForResource("/org/apache/hivemind");
            fail("digests for packages in jars are known to throw NPE");
        }
        catch (NullPointerException e)
        {
            // ignore
        }
    }

    public void testCache()
    {
        ClassResolver resolver = newMock(ClassResolver.class);

        URL url = getClass().getResource("tapestry-in-action.png");

        expect(resolver.getResource("/foo")).andReturn(url);

        replay();

        ResourceDigestSourceImpl s = new ResourceDigestSourceImpl();
        s.setClassResolver(resolver);

        assertEquals("a5f4663532ea3efe22084df086482290", s.getDigestForResource("/foo"));

        // Try it in the cache; note that the class resolver is not
        // invoked this time.

        assertEquals("a5f4663532ea3efe22084df086482290", s.getDigestForResource("/foo"));

        verify();

        expect(resolver.getResource("/foo")).andReturn(url);

        replay();

        // This clears the cache

        s.resetEventDidOccur();

        // So this goes to the ClassResolver

        assertEquals("a5f4663532ea3efe22084df086482290", s.getDigestForResource("/foo"));

        verify();

    }
}