package net.sf.tapestry.junit.mock;

import net.sf.tapestry.junit.TapestryTestCase;

/**
 *  Test case for Mock Servlet API tests using the Simple application.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class SimpleMockTest extends TapestryTestCase
{

    public SimpleMockTest(String name)
    {
        super(name);
    }

    private MockTester attempt(String path)
    throws Exception
    {
        MockTester tester = new MockTester(path);
        
        tester.execute();
        
        return tester;
    }

    public void testInitialRequest()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/simple/Simple.xml");
    }
}
