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

    private String attempt(String path)
    throws Exception
    {
        MockTester tester = new MockTester(path);
        
        tester.execute();
        
        return tester.getResponse().getOutputString();
    }

    public void testInitialRequest()
    throws Exception
    {
        String result = attempt("/net/sf/tapestry/junit/mock/simple/Simple.xml");
        
        System.out.println(result);
    }
}
