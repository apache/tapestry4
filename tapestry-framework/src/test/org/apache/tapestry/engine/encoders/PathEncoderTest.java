package org.apache.tapestry.engine.encoders;

import com.javaforge.tapestry.testng.TestBase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests functionality of the {@link PathEncoder} .
 */
@Test
public class PathEncoderTest extends TestBase {

    public void test_Wrong_Service()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, "foo");

        replay();

        PathEncoder encoder = new PathEncoder();
        encoder.setService("bar");
        
        encoder.encode(encoding);

        verify();
    }

    protected void trainGetParameterValue(ServiceEncoding encoding, String name, String value)
    {
        expect(encoding.getParameterValue(name)).andReturn(value);
    }

    protected ServiceEncoding newEncoding()
    {
        return newMock(ServiceEncoding.class);
    }

    public void test_Wrong_Path()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/Home.page");

        replay();

        PathEncoder encoder = new PathEncoder();
        encoder.setPath("/rounded");
        encoder.setService("rounded");
        
        encoder.decode(encoding);

        verify();
    }

    protected void trainGetServletPath(ServiceEncoding encoding, String servletPath)
    {
        expect(encoding.getServletPath()).andReturn(servletPath);
    }

    public void test_Encode()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, "rounded");

        encoding.setServletPath("/rounded");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);

        replay();

        PathEncoder encoder = new PathEncoder();
        encoder.setPath("/rounded");
        encoder.setService("rounded");

        encoder.encode(encoding);

        verify();
    }
}
