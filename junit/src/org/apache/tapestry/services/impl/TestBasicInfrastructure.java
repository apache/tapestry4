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

package org.apache.tapestry.services.impl;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.services.ClasspathResourceFactory;
import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.services.ServletInfo;

/**
 * Tests for:
 * <ul>
 * <li>{@link org.apache.tapestry.services.impl.ServletInfoImpl}
 * <li>{@link org.apache.tapestry.services.impl.StoreServletInfoFilter}
 * <li>{@link org.apache.tapestry.services.impl.ApplicationGlobalsImpl}.
 * </ul>
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestBasicInfrastructure extends HiveMindTestCase
{
    private static final Log LOG = LogFactory.getLog(TestBasicInfrastructure.class);

    public void testServletInfoImpl()
    {
        ServletInfoImpl si = new ServletInfoImpl();

        HttpServletRequest r = (HttpServletRequest) newMock(HttpServletRequest.class);
        HttpServletResponse p = (HttpServletResponse) newMock(HttpServletResponse.class);

        replayControls();

        si.store(r, p);

        assertSame(r, si.getRequest());
        assertSame(p, si.getResponse());

        verifyControls();
    }

    public void testStoreServletInfoFilter() throws Exception
    {
        ServletInfo si = (ServletInfo) newMock(ServletInfo.class);
        HttpServletRequest r = (HttpServletRequest) newMock(HttpServletRequest.class);
        HttpServletResponse p = (HttpServletResponse) newMock(HttpServletResponse.class);
        RequestServicer n = (RequestServicer) newMock(RequestServicer.class);

        si.store(r, p);

        n.service(r, p);

        replayControls();

        StoreServletInfoFilter f = new StoreServletInfoFilter();

        f.setServletInfo(si);

        f.service(r, p, n);

        verifyControls();
    }

    public void testMasterIntializer() throws Exception
    {
        ApplicationServlet servlet = new ApplicationServlet();

        ApplicationInitializer ai = (ApplicationInitializer) newMock(ApplicationInitializer.class);

        ai.initialize(servlet);

        replayControls();

        // Build the list.

        InitializerContribution ic = new InitializerContribution();
        ic.setName("test");
        ic.setInitializer(ai);

        List l = Collections.singletonList(ic);

        MasterInitializer mi = new MasterInitializer();
        mi.setErrorHandler(new DefaultErrorHandler());
        mi.setLog(LOG);
        mi.setInitializers(l);

        mi.initialize(servlet);

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
     * Validate that the factory is declared properly in the module
     * deployment descriptor.
     */
    public void testClasspathResourceFactoryIntegrated()
    {
        ClassResolver cr = new DefaultClassResolver();

        Registry registry = RegistryBuilder.constructDefaultRegistry();

        ClasspathResourceFactory f =
            (ClasspathResourceFactory) registry.getService(
                "tapestry.ClasspathResourceFactory",
                ClasspathResourceFactory.class);

        String path = "/foo/bar";

        ClasspathResource expected = new ClasspathResource(cr, path);

        assertEquals(expected, f.newResource(path));
    }

    public void testServletInfoInitializer() throws Exception
    {
        HttpServletRequest request = (HttpServletRequest) newMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) newMock(HttpServletResponse.class);
        ServletInfo servletInfo = (ServletInfo) newMock(ServletInfo.class);

        ServletInfoInitializer sii = new ServletInfoInitializer();

        RequestServicer rs = (RequestServicer) newMock(RequestServicer.class);

        sii.setServletInfo(servletInfo);

        // Training

        servletInfo.store(request, response);

        rs.service(request, response);

        replayControls();

        sii.service(request, response, rs);

        verifyControls();
    }
}
