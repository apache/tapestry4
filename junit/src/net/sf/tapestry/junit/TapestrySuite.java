package net.sf.tapestry.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import net.sf.tapestry.junit.mock.MockTestCase;
import net.sf.tapestry.junit.parse.SpecificationParserTest;
import net.sf.tapestry.junit.parse.TemplateParserTest;
import net.sf.tapestry.junit.script.ScriptTest;
import net.sf.tapestry.junit.spec.TestApplicationSpecification;
import net.sf.tapestry.junit.spec.TestComponentSpecification;
import net.sf.tapestry.junit.utils.TestAdaptorRegistry;
import net.sf.tapestry.junit.utils.TestDataSqueezer;
import net.sf.tapestry.junit.utils.TestEnum;
import net.sf.tapestry.junit.utils.TestIdAllocator;
import net.sf.tapestry.junit.utils.TestLocalizedNameGenerator;
import net.sf.tapestry.junit.utils.TestPool;
import net.sf.tapestry.junit.utils.TestPropertyFinder;
import net.sf.tapestry.junit.valid.ValidSuite;

/**
 *  Master suite of Tapestry tests, combining all other test suites.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TapestrySuite extends TestSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(TestAdaptorRegistry.class);
        suite.addTestSuite(TestTapestryCoerceToIterator.class);
        suite.addTestSuite(TestPool.class);
        suite.addTestSuite(TestLocalizedNameGenerator.class);
        suite.addTestSuite(TestResourceLocation.class);
        suite.addTestSuite(TestPropertyFinder.class);
        suite.addTestSuite(TestListenerMap.class);
        suite.addTestSuite(TestIdAllocator.class);
        suite.addTestSuite(ComponentStringsTest.class);
        suite.addTestSuite(TemplateParserTest.class);
        suite.addTestSuite(SpecificationParserTest.class);
        suite.addTestSuite(TestApplicationSpecification.class);
        suite.addTest(ValidSuite.suite());
        suite.addTestSuite(TestEnum.class);
        suite.addTestSuite(TestDataSqueezer.class);
        suite.addTestSuite(ScriptTest.class);
        suite.addTestSuite(TestComponentSpecification.class);
        suite.addTestSuite(BindingsTestCase.class);
        suite.addTestSuite(TestPropertySource.class);
        suite.addTestSuite(ComponentTest.class);
        suite.addTestSuite(MockTestCase.class);

        return suite;
    }

}