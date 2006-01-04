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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.services.ClasspathResourceFactory;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.ResetEventHub;
import org.easymock.MockControl;

/**
 * Tests for:
 * <ul>
 * <li>{@link org.apache.tapestry.services.impl.ServletInfoImpl}
 * <li>{@link org.apache.tapestry.services.impl.StoreServletInfoFilter}
 * <li>{@link org.apache.tapestry.services.impl.ApplicationGlobalsImpl}
 * <li>{@link org.apache.tapestry.services.impl.InfrastructureObjectProvider}
 * </ul>
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestBasicInfrastructure extends HiveMindTestCase
{
    public void testRequestGlobals()
    {
        RequestGlobalsImpl si = new RequestGlobalsImpl();

        HttpServletRequest r = (HttpServletRequest) newMock(HttpServletRequest.class);
        HttpServletResponse p = (HttpServletResponse) newMock(HttpServletResponse.class);

        replayControls();

        si.store(r, p);

        assertSame(r, si.getRequest());
        assertSame(p, si.getResponse());

        verifyControls();
    }

    public void testClasspathResourceFactory()
    {
        ClassResolver cr = new DefaultClassResolver();
        ClasspathResourceFactoryImpl f = new ClasspathResourceFactoryImpl();
        f.setClassResolver(cr);

        String path = "/foo/bar";

        ClasspathResource expected = new ClasspathResource(cr, path);

        assertEquals(expected, f.newResource(path));
    }

    /**
     * Validate that the factory is declared properly in the module deployment descriptor.
     */
    public void testClasspathResourceFactoryIntegrated()
    {
        ClassResolver cr = new DefaultClassResolver();

        Registry registry = RegistryBuilder.constructDefaultRegistry();

        ClasspathResourceFactory f = (ClasspathResourceFactory) registry.getService(
                "tapestry.ClasspathResourceFactory",
                ClasspathResourceFactory.class);

        String path = "/foo/bar";

        ClasspathResource expected = new ClasspathResource(cr, path);

        assertEquals(expected, f.newResource(path));
    }

    public void testGlobalPropertyObjectProviderSuccess()
    {
        MockControl sourceControl = newControl(IPropertySource.class);
        IPropertySource source = (IPropertySource) sourceControl.getMock();

        // Training

        source.getPropertyValue("foo");
        sourceControl.setReturnValue("bar");

        replayControls();

        PropertyObjectProvider p = new PropertyObjectProvider();
        p.setSource(source);

        assertEquals("bar", p.provideObject(null, null, "foo", null));

        verifyControls();
    }

    public void testGlobalPropertyObjectProviderFailure()
    {
        Location l = fabricateLocation(223);

        MockControl sourceControl = newControl(IPropertySource.class);
        IPropertySource source = (IPropertySource) sourceControl.getMock();

        // Training

        source.getPropertyValue("foo");
        sourceControl.setThrowable(new ApplicationRuntimeException("failure"));

        replayControls();

        PropertyObjectProvider p = new PropertyObjectProvider();
        p.setSource(source);

        try
        {
            p.provideObject(null, null, "foo", l);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("failure", ex.getMessage());
            assertEquals(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testSuccessfulInfrastructureLookup()
    {
        MockControl ifrControl = newControl(Infrastructure.class);
        Infrastructure ifr = (Infrastructure) ifrControl.getMock();

        ResetEventHub coord = (ResetEventHub) newMock(ResetEventHub.class);

        ifr.getResetEventHub();
        ifrControl.setReturnValue(coord);

        replayControls();

        InfrastructureObjectProvider p = new InfrastructureObjectProvider();

        p.setInfrastructure(ifr);

        Object actual = p.provideObject(null, null, "resetEventHub", null);

        assertSame(coord, actual);

        verifyControls();
    }
}