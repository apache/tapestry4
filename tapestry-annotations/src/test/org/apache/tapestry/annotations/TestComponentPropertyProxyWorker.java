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
package org.apache.tapestry.annotations;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link ComponentPropertyProxyWorker}.
 */
@Test
public class TestComponentPropertyProxyWorker extends BaseAnnotationTestCase
{
    
    void addProperty(EnhancementOperation op, IComponentSpecification spec, Location l, 
            String propertyName, Class type)
    {
        IPropertySpecification pspec = new PropertySpecification();

        pspec.setName(propertyName);
        pspec.setPersistence("session");
        pspec.setLocation(l);
        pspec.setType(type.getName());
        
        spec.addPropertySpecification(pspec);
        
        expect(op.convertTypeName(type.getName())).andReturn(type);
        op.validateProperty(propertyName, type);
    }
    
    public void test_Excluded()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        
        addProperty(op, spec, l, "bean", SimpleBean.class);
        
        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        
        List exclude = new ArrayList();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);
        
        replay();
        
        worker.performEnhancement(op, spec);
        
        verify();
        
        IPropertySpecification prop = spec.getPropertySpecification("bean");
        
        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert !prop.canProxy();
    }
    
    public void test_SubClass_Excluded()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        
        addProperty(op, spec, l, "subBean", SubSimpleBean.class);
        
        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        
        List exclude = new ArrayList();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);
        
        replay();
        
        worker.performEnhancement(op, spec);
        
        verify();
        
        IPropertySpecification prop = spec.getPropertySpecification("subBean");
        
        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert !prop.canProxy();
    }
}
