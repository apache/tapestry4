package org.apache.tapestry.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.tapestry.junit.enhance.TestClassFabricator;
import org.apache.tapestry.junit.form.TestListEditMap;
import org.apache.tapestry.junit.parse.TestLocation;
import org.apache.tapestry.junit.parse.TestSpecificationParser;
import org.apache.tapestry.junit.parse.TestTemplateParser;
import org.apache.tapestry.junit.script.TestScript;
import org.apache.tapestry.junit.spec.TestApplicationSpecification;
import org.apache.tapestry.junit.spec.TestComponentSpecification;
import org.apache.tapestry.junit.utils.TestAdaptorRegistry;
import org.apache.tapestry.junit.utils.TestDataSqueezer;
import org.apache.tapestry.junit.utils.TestEnum;
import org.apache.tapestry.junit.utils.TestIdAllocator;
import org.apache.tapestry.junit.utils.TestLocalizedNameGenerator;
import org.apache.tapestry.junit.utils.TestPool;
import org.apache.tapestry.junit.utils.TestPropertyFinder;
import org.apache.tapestry.junit.utils.TestRegexpMatcher;
import org.apache.tapestry.junit.valid.ValidSuite;

/**
 *  Used to isolate the non-mock tests from the mock Tapestry tests, since they run much
 *  faster.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class BasicTestSuite extends TestSuite
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Basic Tapestry Test Suite");

        suite.addTestSuite(TestTapestryFindLocation.class);
        suite.addTestSuite(TestStaticLink.class);
        suite.addTestSuite(TestEngineServiceLink.class);
        suite.addTestSuite(TestAdaptorRegistry.class);
        suite.addTestSuite(TestTapestryCoerceToIterator.class);
        suite.addTestSuite(TestPool.class);
        suite.addTestSuite(TestLocalizedNameGenerator.class);
        suite.addTestSuite(TestResourceLocation.class);
        suite.addTestSuite(TestPropertyFinder.class);
        suite.addTestSuite(TestListenerMap.class);
        suite.addTestSuite(TestIdAllocator.class);
        suite.addTestSuite(TestComponentStrings.class);
        suite.addTestSuite(TestTemplateParser.class);
        suite.addTestSuite(TestLocation.class);
        suite.addTestSuite(TestSpecificationParser.class);
        suite.addTestSuite(TestApplicationSpecification.class);
        suite.addTest(ValidSuite.suite());
        suite.addTestSuite(TestMultipart.class);
        suite.addTestSuite(TestEnum.class);
        suite.addTestSuite(TestDataSqueezer.class);
        suite.addTestSuite(TestScript.class);
        suite.addTestSuite(TestComponentSpecification.class);
        suite.addTestSuite(TestBindings.class);
        suite.addTestSuite(TestPropertySource.class);
        suite.addTestSuite(TestComponent.class);
        suite.addTestSuite(TestClassFabricator.class);
        suite.addTestSuite(TestTapestryGetClassName.class);
        suite.addTestSuite(TestRegexpMatcher.class);
        suite.addTestSuite(TestListEditMap.class);

        return suite;
    }
}
