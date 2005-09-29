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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Test case for {@link org.apache.tapestry.annotations.MetaAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MetaAnnotationWorkerTest extends BaseAnnotationTestCase
{

    public void testMeta()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        spec.setProperty("foo", "bar");
        spec.setProperty("biff", "bazz");

        replayControls();

        new MetaAnnotationWorker().performEnhancement(op, spec, MetaPage.class, l);

        verifyControls();
    }

    public void testMetaInSubclass()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        // From MetaPage
        spec.setProperty("foo", "bar");
        spec.setProperty("biff", "bazz");

        // From MetaPageSubclass
        spec.setProperty("in-subclass", "true");

        replayControls();

        new MetaAnnotationWorker().performEnhancement(op, spec, MetaPageSubclass.class, l);

        verifyControls();

    }

    public void testNoEqualsSign()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        replayControls();

        MetaAnnotationWorker worker = new MetaAnnotationWorker();

        try
        {
            worker.performEnhancement(op, spec, MissingEqualsMetaPage.class, l);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "The meta value 'noequals' must include an equals sign to seperate the key and the value.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();

    }
}
