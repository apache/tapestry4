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

import java.lang.reflect.Method;

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.InjectAssetWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.annotations.InjectAssetAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectAssetAnnotationWorker extends BaseAnnotationTestCase
{
    public void testDefault()
    {
        InjectAssetAnnotationWorker worker = new InjectAssetAnnotationWorker();

        assertNotNull(worker._delegate);
    }

    public void testDelegation()
    {
        Location l = newLocation();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        InjectAssetWorker delegate = (InjectAssetWorker) newMock(InjectAssetWorker.class);

        Method m = findMethod(AnnotatedPage.class, "getStylesheetAsset");

        delegate.injectAsset(op, "stylesheet", "stylesheetAsset", l);

        replayControls();

        InjectAssetAnnotationWorker worker = new InjectAssetAnnotationWorker(delegate);

        worker.performEnhancement(op, spec, m, l);

        verifyControls();
    }
}
