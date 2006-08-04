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

package org.apache.tapestry.coerce;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import java.util.Collections;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.coerce.TypeConverterWrapper}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestTypeConverterWrapper extends BaseComponentTestCase
{
    public void testNonNull()
    {
        TypeConverter tc = newMock(TypeConverter.class);

        Object expected = "BARNEY";

        expect(tc.convertValue("FRED")).andReturn(expected);

        replay();

        TypeConverterContribution contrib = new TypeConverterContribution();
        contrib.setSubjectClass(Object.class);
        contrib.setConverter(tc);

        TypeConverterWrapper w = new TypeConverterWrapper();

        w.setContributions(Collections.singletonList(contrib));

        w.initializeService();

        Object actual = w.convertValue("FRED");

        assertSame(expected, actual);

        verify();
    }

    public void testNull()
    {
        TypeConverter tc = newMock(TypeConverter.class);

        Object expected = "NULL";

        expect(tc.convertValue(null)).andReturn(expected);

        replay();

        TypeConverterWrapper w = new TypeConverterWrapper();

        w.setNullConverter(tc);

        Object actual = w.convertValue(null);

        assertSame(expected, actual);

        verify();
    }

    public void testNullWithNoNullConverter()
    {
        TypeConverterWrapper w = new TypeConverterWrapper();

        assertNull(w.convertValue(null));
    }
}
