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

package org.apache.tapestry.junit.valid;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Suite of tests for validators and delegates related to
 *  {@link org.apache.tapestry.valid.ValidField}.
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
        suite.addTest(new TestSuite(TestEmailValidator.class));
        suite.addTest(new TestSuite(TestPatternValidator.class));
		suite.addTest(new TestSuite(TestUrlValidator.class));

        return suite;
    }

}