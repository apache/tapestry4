package net.sf.tapestry.junit.mock;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Test suite for just the Mock tests.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockSuite
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Mock Servlet API Suite");
    
        suite.addTestSuite(MockTestCase.class);
        
        return suite;
    }
}
