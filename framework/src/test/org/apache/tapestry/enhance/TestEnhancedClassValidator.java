// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.EnhancedClassValidatorImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestEnhancedClassValidator extends HiveMindTestCase
{
    public static abstract class AbstractBase
    {
        public abstract void foo();
    }

    public static abstract class Incomplete extends AbstractBase
    {
        public void bar()
        {
            //
        }
    }

    public static class Complete extends AbstractBase
    {
        public void foo()
        {
            //
        }
    }

    /**
     * Test for a class that fulfills its requirements (by implementing all inherited abstract
     * methods.
     */

    public void testComplete()
    {
        EnhancedClassValidator v = new EnhancedClassValidatorImpl();

        v.validate(AbstractBase.class, Complete.class, null);
    }

    /**
     * Pass in an abstract class (with enhancement, its possible that a supposedly concrete class
     * may omit implementing an inherited abstract method, which is the whole point of the
     * validator.
     */

    public void testIncomplete()
    {
        Log log = (Log) newMock(Log.class);
        ErrorHandler errorHandler = (ErrorHandler) newMock(ErrorHandler.class);

        Location l = fabricateLocation(11);

        MockControl specControl = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specControl.getMock();

        spec.getLocation();
        specControl.setReturnValue(l);

        errorHandler
                .error(
                        log,
                        "Method 'public abstract void org.apache.tapestry.enhance.TestEnhancedClassValidator$AbstractBase.foo()' (declared in class org.apache.tapestry.enhance.TestEnhancedClassValidator$AbstractBase) has no implementation in class org.apache.tapestry.enhance.TestEnhancedClassValidator$AbstractBase (or enhanced subclass org.apache.tapestry.enhance.TestEnhancedClassValidator$Incomplete).",
                        l,
                        null);

        replayControls();

        EnhancedClassValidatorImpl v = new EnhancedClassValidatorImpl();
        v.setLog(log);
        v.setErrorHandler(errorHandler);

        v.validate(AbstractBase.class, Incomplete.class, spec);

        verifyControls();
    }

    /**
     * Ensures that the code works when passed java.lang.Object (which has different inheritance
     * than other classes.
     */

    public void testObject()
    {
        EnhancedClassValidator v = new EnhancedClassValidatorImpl();

        v.validate(Object.class, Object.class, null);
    }
}