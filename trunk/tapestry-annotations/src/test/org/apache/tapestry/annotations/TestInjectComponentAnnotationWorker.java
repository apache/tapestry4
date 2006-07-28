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

import static org.testng.AssertJUnit.assertNotNull;

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.InjectComponentWorker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.InjectComponentAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInjectComponentAnnotationWorker extends BaseAnnotationTestCase
{

    public void testDefault()
    {
        InjectComponentAnnotationWorker worker = new InjectComponentAnnotationWorker();

        assertNotNull(worker._delegate);
    }

    public void testDelegation()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        InjectComponentWorker delegate = org.easymock.classextension.EasyMock.createMock(InjectComponentWorker.class);
        
        delegate.injectComponent(op, "fred", "fredField", l);
        
        replay();
        org.easymock.classextension.EasyMock.replay(delegate);
        
        InjectComponentAnnotationWorker worker = new InjectComponentAnnotationWorker(delegate);

        worker.performEnhancement(op, spec, findMethod(AnnotatedPage.class, "getFredField"), l);
        
        verify();
        org.easymock.classextension.EasyMock.verify(delegate);
    }
}
