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

package org.apache.tapestry.annotations;

import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.annotations.AnnotationLocation}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestAnnotationLocation extends HiveMindTestCase
{
    public void testLineRowAreZero()
    {
        Resource r = (Resource) newMock(Resource.class);

        replayControls();

        AnnotationLocation l = new AnnotationLocation(r, "location description");

        assertEquals(0, l.getLineNumber());
        assertEquals(0, l.getColumnNumber());

        assertSame(r, l.getResource());

        assertEquals("location description", l.toString());

        verifyControls();
    }

    public void testEquals()
    {
        Resource r = (Resource) newMock(Resource.class);
        Resource r2 = (Resource) newMock(Resource.class);

        replayControls();

        AnnotationLocation l = new AnnotationLocation(r, "location description");
        AnnotationLocation l2 = new AnnotationLocation(r2, "location description");
        AnnotationLocation l3 = new AnnotationLocation(r, "wrong description");
        AnnotationLocation l4 = new AnnotationLocation(r, "location description");

        assertEquals(false, l.equals(null));
        assertEquals(false, l.equals("XYZ"));
        assertEquals(false, l.equals(l2));
        assertEquals(false, l.equals(l3));
        assertEquals(true, l.equals(l4));

        verifyControls();
    }
}
