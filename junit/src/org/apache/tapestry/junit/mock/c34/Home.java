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

package org.apache.tapestry.junit.mock.c34;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 * Test class used to check whether we can disable abstract method checks.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public abstract class Home extends BasePage
{
    public abstract void setMessage(String message);

    public void triggerError(IRequestCycle cycle)
    {
        try
        {
            unimplementedMethod();
        }
        catch (AbstractMethodError e)
        {
            setMessage("Got an AbstractMethodError invoking unimplementedMethod().");
        }
    }

    /**
     * Normally, this would be caught as an error, but we've disabled
     * the check for unimplemented abstract methods.
     */
    public abstract void unimplementedMethod();
}
