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

package org.apache.tapestry.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.tapestry.junit.engine.TestRequestCycleToString;
import org.apache.tapestry.junit.enhance.TestEnhancedClassFactory;
import org.apache.tapestry.junit.enhance.TestMethodSignature;
import org.apache.tapestry.junit.form.TestListEditMap;
import org.apache.tapestry.junit.parse.TestLocation;
import org.apache.tapestry.junit.parse.TestSpecificationParser;
import org.apache.tapestry.junit.parse.TestTemplateParser;
import org.apache.tapestry.junit.script.TestScript;
import org.apache.tapestry.junit.spec.TestApplicationSpecification;
import org.apache.tapestry.junit.spec.TestComponentSpecification;
import org.apache.tapestry.junit.spec.TestDirection;
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
 *  @since 3.0
 *
 **/

public class BasicTestSuite extends TestSuite
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Basic Tapestry Test Suite");

        suite.addTestSuite(TestTapestryFindLocation.class);
        suite.addTestSuite(TestTapestryCheckMethodInvocation.class);
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
        suite.addTestSuite(TestComponentMessages.class);
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
        suite.addTestSuite(TestEnhancedClassFactory.class);
        suite.addTestSuite(TestMethodSignature.class);
        suite.addTestSuite(TestTapestryGetClassName.class);
        suite.addTestSuite(TestRegexpMatcher.class);
        suite.addTestSuite(TestListEditMap.class);
        suite.addTestSuite(TestContentType.class);
        suite.addTestSuite(TestRequestCycleToString.class);
        suite.addTestSuite(TestDirection.class);

        return suite;
    }
}
