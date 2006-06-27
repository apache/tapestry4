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

package org.apache.tapestry.engine.encoders;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.PageServiceEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPageServiceEncoder extends BaseComponentTestCase
{
    public void testEncodeOtherService()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getParameterValue(ServiceConstants.SERVICE)).andReturn("foo");

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");

        encoder.encode(e);

        verify();
    }

    public void testEncode()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getParameterValue(ServiceConstants.SERVICE)).andReturn("page");

        expect(e.getParameterValue(ServiceConstants.PAGE)).andReturn("Home");

        e.setServletPath("/Home.html");
        e.setParameterValue(ServiceConstants.SERVICE, null);
        e.setParameterValue(ServiceConstants.PAGE, null);

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");
        encoder.setExtension("html");

        encoder.encode(e);

        verify();
    }

    public void testEncodeHtm()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getParameterValue(ServiceConstants.SERVICE)).andReturn("page");

        expect(e.getParameterValue(ServiceConstants.PAGE)).andReturn("Home");

        e.setServletPath("/Home.htm");
        e.setParameterValue(ServiceConstants.SERVICE, null);
        e.setParameterValue(ServiceConstants.PAGE, null);

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");
        encoder.setExtension("htm");

        encoder.encode(e);

        verify();
    }
    
    public void testEncodeInNamespace()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getParameterValue(ServiceConstants.SERVICE)).andReturn("page");

        expect(e.getParameterValue(ServiceConstants.PAGE)).andReturn("contrib:Foo");
        
        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");

        encoder.encode(e);

        verify();
    }

    public void testDecodeNoExtension()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getServletPath()).andReturn("/app");
        
        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verify();
    }

    public void testDecodeEndsWithDot()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getServletPath()).andReturn("/ends.with.dot.");

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verify();
    }

    public void testDecodeWrongExtension()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getServletPath()).andReturn("/Home.direct");

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verify();
    }

    public void testDecodeSuccess()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getServletPath()).andReturn("/Home.html");

        e.setParameterValue(ServiceConstants.SERVICE, "page");
        e.setParameterValue(ServiceConstants.PAGE, "Home");

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setExtension("html");
        encoder.setServiceName("page");

        encoder.decode(e);

        verify();
    }
    
    public void testDecodeHtmSuccess()
    {
        ServiceEncoding e = newMock(ServiceEncoding.class);

        expect(e.getServletPath()).andReturn("/Home.htm");

        e.setParameterValue(ServiceConstants.SERVICE, "page");
        e.setParameterValue(ServiceConstants.PAGE, "Home");

        replay();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setExtension("htm");
        encoder.setServiceName("page");

        encoder.decode(e);

        verify();
    }
}