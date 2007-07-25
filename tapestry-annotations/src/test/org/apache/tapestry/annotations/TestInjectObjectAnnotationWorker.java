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
import org.apache.tapestry.enhance.InjectObjectWorker;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.engine.IPropertySource;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.InjectObjectAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInjectObjectAnnotationWorker extends BaseAnnotationTestCase
{
    public void testDefault()
    {
        InjectObjectAnnotationWorker worker = new InjectObjectAnnotationWorker();

        assertNotNull(worker._delegate);
    }

    public void testDelegation()
    {
        Location l = newLocation();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
	    IPropertySource propertySource = newPropertySource();

        InjectObjectWorker delegate = org.easymock.classextension.EasyMock.createNiceMock(InjectObjectWorker.class);

        InjectedValueProvider provider = newMock(InjectedValueProvider.class);

        delegate.setProvider(provider);
        
        replay();
        org.easymock.classextension.EasyMock.replay(delegate);
        
        InjectObjectAnnotationWorker worker = new InjectObjectAnnotationWorker(delegate);
        worker.setProvider(provider);
        
        verify();
        org.easymock.classextension.EasyMock.verify(delegate);
        
        Method m = findMethod(AnnotatedPage.class, "getInjectedObject");
        
        delegate.injectObject(op, "barney", "injectedObject", l);
        
        replay();
        
        worker.performEnhancement(op, spec, m, l, propertySource);
        
        verify();
        org.easymock.classextension.EasyMock.verify(delegate);
    }

}
