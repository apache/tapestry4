// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine.state;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestNamedClassStateObjectFactory extends HiveMindTestCase
{
    public void testSuccess()
    {
        NamedClassStateObjectFactory f = new NamedClassStateObjectFactory();
        f.setClassResolver(new DefaultClassResolver());
        f.setClassName(HashMap.class.getName());

        assertTrue(f.createStateObject() instanceof HashMap);
    }

    public void testFailure()
    {
        Location l = fabricateLocation(99);

        NamedClassStateObjectFactory f = new NamedClassStateObjectFactory();
        f.setClassResolver(new DefaultClassResolver());
        f.setClassName(Map.class.getName());
        f.setLocation(l);

        try
        {
            f.createStateObject();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Unable to instantiate an instance of java.util.Map");

            assertSame(l, ex.getLocation());

        }

    }

}