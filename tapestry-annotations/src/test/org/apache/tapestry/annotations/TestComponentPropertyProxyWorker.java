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
@Test(sequential = true)
public class TestComponentPropertyProxyWorker extends BaseAnnotationTestCase
{

    IPropertySpecification addProperty(EnhancementOperation op, IComponentSpecification spec, Location l,
            String propertyName) {
        IPropertySpecification pspec = new PropertySpecification();

        pspec.setName(propertyName);
        pspec.setPersistence("session");
        pspec.setLocation(l);

        spec.addPropertySpecification(pspec);
        
        return pspec;
    }
    
    
    public void test_Generics_Excluded() {
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);
        checkOrder(op, false);

        IComponentSpecification spec = new ComponentSpecification();

        expect(op.getBaseClass()).andReturn(AnnotatedGenericPage.class).anyTimes();

        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        
        addProperty(op, spec, l, "value");

        List<String> exclude = new ArrayList<String>();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);
        
        replay();

        worker.performEnhancement(op, spec);

        verify();

        IPropertySpecification prop = spec.getPropertySpecification("value");

        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert !prop.canProxy();
    }

    public void test_Valid_Property() {
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);
        checkOrder(op, false);

        IComponentSpecification spec = new ComponentSpecification();

        expect(op.getBaseClass()).andReturn(AnnotatedGenericPersistentPage.class).anyTimes();

        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();

        addProperty(op, spec, l, "object");

        List<String> exclude = new ArrayList<String>();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);

        replay();

        worker.performEnhancement(op, spec);

        verify();

        IPropertySpecification prop = spec.getPropertySpecification("object");

        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert prop.canProxy();
    }
    
    public void test_Type_Found()
    {
        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        List<String> exclude = new ArrayList<String>();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);
        
        IPropertySpecification prop = new PropertySpecification();
        prop.setName("value");
        prop.setPersistence("session");
        
        assertEquals(worker.extractPropertyType(AnnotatedGenericPersistentPage.class, "value", prop), Persistent.class);
        
        prop.setGeneric(false);
        prop.setType(null);
        prop.setName("secondValue");
        
        Class type = worker.extractPropertyType(AnnotatedGenericPersistentPage.class, "secondValue", prop);
        
        assert type != null;
        assert prop.isGeneric();
        
        assertEquals(type, Persistent.class);
    }
    
    public void test_Write_Property_Non_Generic() {
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);
        checkOrder(op, false);

        IComponentSpecification spec = new ComponentSpecification();

        expect(op.getBaseClass()).andReturn(AnnotatedGenericPersistentPage.class).anyTimes();

        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        
        IPropertySpecification p = addProperty(op, spec, l, "listValue");

        List<String> exclude = new ArrayList<String>();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);

        replay();

        worker.performEnhancement(op, spec);

        verify();

        IPropertySpecification prop = spec.getPropertySpecification("listValue");

        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert prop.canProxy();
        
        assertEquals(p.getType(), "java.util.List");
    }
    
    public void test_Write_Property_Generic() {
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);
        checkOrder(op, false);

        IComponentSpecification spec = new ComponentSpecification();

        expect(op.getBaseClass()).andReturn(AnnotatedGenericPersistentPage.class).anyTimes();

        ComponentPropertyProxyWorker worker = new ComponentPropertyProxyWorker();
        
        IPropertySpecification p = addProperty(op, spec, l, "secondValue");

        List<String> exclude = new ArrayList<String>();
        exclude.add("Entity");
        worker.setExcludedPackages(exclude);

        replay();

        worker.performEnhancement(op, spec);

        verify();

        IPropertySpecification prop = spec.getPropertySpecification("secondValue");
        
        assert prop != null;
        assert prop.isPersistent();
        assert prop.isProxyChecked();
        assert !prop.canProxy();
        
        assertEquals(p.getType(), Persistent.class.getName());
    }
    
    
    public void test_Excluded()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        
        expect(op.getBaseClass()).andReturn(AnnotatedPage.class).anyTimes();
        
        addProperty(op, spec, l, "bean");
        
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
        
        expect(op.getBaseClass()).andReturn(AnnotatedPage.class).anyTimes();
        
        addProperty(op, spec, l, "subBean");
        
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
