package net.sf.tapestry.junit.valid;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Suite of tests for validators and delegates related to
 *  {@link net.sf.tapestry.valid.ValidField}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ValidSuite
{

    public static Test suite()
    {

        TestSuite suite = new TestSuite("ValidField Suite");

        suite.addTest(new TestSuite(TestStringValidator.class));
        suite.addTest(new TestSuite(TestDateValidator.class));
        suite.addTest(new TestSuite(TestNumberValidator.class));
        suite.addTest(new TestSuite(TestValidationDelegate.class));

        return suite;

    }

}