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

package org.apache.tapestry.coerce;

import java.util.Collections;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.coerce.TypeConverterWrapper}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestTypeConverterWrapper extends HiveMindTestCase
{
    public void testNonNull()
    {
        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        Object expected = "BARNEY";

        tc.convertValue("FRED");
        tcc.setReturnValue(expected);

        replayControls();

        TypeConverterContribution contrib = new TypeConverterContribution();
        contrib.setSubjectClass(Object.class);
        contrib.setConverter(tc);

        TypeConverterWrapper w = new TypeConverterWrapper();

        w.setContributions(Collections.singletonList(contrib));

        w.initializeService();

        Object actual = w.convertValue("FRED");

        assertSame(expected, actual);

        verifyControls();
    }

    public void testNull()
    {
        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        Object expected = "NULL";

        tc.convertValue(null);
        tcc.setReturnValue(expected);

        replayControls();

        TypeConverterWrapper w = new TypeConverterWrapper();

        w.setNullConverter(tc);

        Object actual = w.convertValue(null);

        assertSame(expected, actual);

        verifyControls();
    }

    public void testNullWithNoNullConverter()
    {
        TypeConverterWrapper w = new TypeConverterWrapper();

        assertNull(w.convertValue(null));
    }
}