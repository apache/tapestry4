// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.enhance.EnhancedClassValidatorImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EnhancedClassValidatorTest extends BaseComponentTestCase
{
    /**
     * Test for a class that fulfills its requirements (by implementing all inherited abstract
     * methods.
     */

    public void testComplete()
    {
        EnhancedClassValidator v = new EnhancedClassValidatorImpl();

        v.validate(AbstractBase.class, Complete.class, new ComponentSpecification());
    }

    /**
     * Pass in an abstract class (with enhancement, its possible that a supposedly concrete class
     * may omit implementing an inherited abstract method, which is the whole point of the
     * validator.
     */

    public void testIncomplete()
    {
        ErrorLog log = newErrorLog();
        Location l = newLocation();

        IComponentSpecification spec = newSpec();

        trainGetLocation(spec, l);

        log
                .error(
                        "Method 'public abstract void org.apache.tapestry.enhance.AbstractBase.foo()' "
                                + "(declared in class org.apache.tapestry.enhance.AbstractBase) has no implementation in class "
                                + "org.apache.tapestry.enhance.AbstractBase (or enhanced subclass org.apache.tapestry.enhance.Incomplete).",
                        l,
                        null);

        replay();

        EnhancedClassValidatorImpl v = new EnhancedClassValidatorImpl();
        v.setErrorLog(log);

        v.validate(AbstractBase.class, Incomplete.class, spec);

        verify();
    }

    public void testInheritsMissingMethod()
    {
        ErrorLog log = newErrorLog();
        Location l = newLocation();

        IComponentSpecification spec = newSpec();

        trainGetLocation(spec, l);

        log
                .error(
                        "Method 'public abstract void java.lang.Runnable.run()' "
                                + "has no implementation in class org.apache.tapestry.enhance.AbstractRunnable "
                                + "(or enhanced subclass org.apache.tapestry.enhance.AbstractRunnableSubclass).",
                        l,
                        null);

        replay();

        EnhancedClassValidatorImpl v = new EnhancedClassValidatorImpl();
        v.setErrorLog(log);

        v.validate(AbstractRunnable.class, AbstractRunnableSubclass.class, spec);

        verify();
    }

    private ErrorLog newErrorLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    /**
     * Ensures that the code works when passed java.lang.Object (which has different inheritance
     * than other classes.
     */

    public void testObject()
    {
        EnhancedClassValidator v = new EnhancedClassValidatorImpl();

        v.validate(Object.class, Object.class, new ComponentSpecification());
    }
}