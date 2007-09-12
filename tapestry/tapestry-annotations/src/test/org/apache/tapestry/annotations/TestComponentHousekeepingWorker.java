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

import static org.easymock.EasyMock.expect;

import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.ComponentHousekeepingWorker}.
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
@Test
public class TestComponentHousekeepingWorker extends BaseAnnotationTestCase
{
    public void test_No_Components()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        expect(spec.getComponentIds()).andReturn(Collections.EMPTY_LIST);

        replay();

        new ComponentHousekeepingWorker().performEnhancement(op, spec);

        verify();
    }
    
    public void test_Unable_To_Copy()
    {        
        IComponentSpecification spec = new ComponentSpecification();

        IContainedComponent ccOne = new ContainedComponent();
        ccOne.setType("Insert");
        spec.addComponent("time", ccOne);
        
        IContainedComponent ccTwo = new ContainedComponent();
        ccTwo.setCopyOf("date");
        spec.addComponent("invalid", ccTwo);

        try 
        {
            new ComponentHousekeepingWorker().performEnhancement(null, spec);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Unable to copy");
        }
    }   
    
    public void test_CopyOf_Valid()
    {        
        IComponentSpecification spec = new ComponentSpecification();

        IContainedComponent ccOne = new ContainedComponent();
        ccOne.setType("Insert");
        ccOne.setBinding("value", new BindingSpecification());
        spec.addComponent("time", ccOne);
        
        
        IContainedComponent ccTwo = new ContainedComponent();
        ccTwo.setCopyOf("time");
        spec.addComponent("valid", ccTwo);
        assertNull( ccTwo.getBinding("value") );

        new ComponentHousekeepingWorker().performEnhancement(null, spec);
        
        IContainedComponent result = spec.getComponent("valid");
        assertEquals(result, ccTwo);
        assertNotNull( result.getBinding("value") );
    }     
    
}
