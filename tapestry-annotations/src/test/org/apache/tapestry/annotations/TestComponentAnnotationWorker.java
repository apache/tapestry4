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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.ComponentAnnotationWorker}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestComponentAnnotationWorker extends BaseAnnotationTestCase
{
    private IContainedComponent run(String id, String methodName, Location location)
    {
        IComponentSpecification spec = new ComponentSpecification();
        
        return run(spec, id, methodName, location);
    }

    private IContainedComponent run(IComponentSpecification spec, String id, String methodName, Location location)
    {
        Method method = findMethod(AnnotatedPage.class, methodName);

        EnhancementOperation op = newOp();

        replay();        

        new ComponentAnnotationWorker().performEnhancement(op, spec, method, location);

        verify();

        return spec.getComponent(id);
    }

    public void test_Simple()
    {
        Location l = newLocation();

        IContainedComponent cc = run("textField", "getTextField", l);

        assertEquals("TextField", cc.getType());
        assertEquals(false, cc.getInheritInformalParameters());
        assertEquals(null, cc.getCopyOf());
        assertSame(l, cc.getLocation());
        assertEquals(true, cc.getBindingNames().isEmpty());
        assertEquals("textField", cc.getPropertyName());
    }
    
    public void test_Without_Type()
    {
        IContainedComponent cc = run("usernameField", "getUsernameField", null);
        
        assertEquals("TextField", cc.getType());
    }

    public void test_Explicit_Id()
    {
        IContainedComponent cc = run("email", "getEmailField", null);

        assertEquals("emailField", cc.getPropertyName());
    }

    public void test_Inherit_Informal_Parameters()
    {
        IContainedComponent cc = run("inherit", "getInherit", null);

        assertEquals(true, cc.getInheritInformalParameters());
    }

    public void test_With_Bindings()
    {
        Location l = newLocation();
        IContainedComponent cc = run("componentWithBindings", "getComponentWithBindings", l);

        IBindingSpecification bs1 = cc.getBinding("condition");
        assertSame(l, bs1.getLocation());
        assertEquals(BindingType.PREFIXED, bs1.getType());
        assertEquals("message", bs1.getValue());

        IBindingSpecification bs2 = cc.getBinding("element");
        assertEquals("div", bs2.getValue());
    }

    public void test_Binding_Whitespace_Trimmed()
    {
        Location l = newLocation();

        IContainedComponent cc = run("whitespace", "getWhitespace", l);

        IBindingSpecification bs1 = cc.getBinding("value");
        assertSame(l, bs1.getLocation());
        assertEquals(BindingType.PREFIXED, bs1.getType());
        assertEquals("email", bs1.getValue());

        IBindingSpecification bs2 = cc.getBinding("displayName");
        assertEquals("message:email-label", bs2.getValue());
    }
    
    public void test_With_Type_And_CopyOf()
    {
        try
        {
            run("anEmailCopy", "getInvalidEmailCopy", null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {           
            assertExceptionSubstring(ex, "both type and copy-of");
        }        
    }      
    
    public void test_CopyOf()
    {
        Location l = newLocation();
        IComponentSpecification spec = new ComponentSpecification();
        run(spec, "componentWithBindings", "getComponentWithBindings", l);
        IContainedComponent cc = run(spec, "aComponentCopy", "getComponentWithBindingsCopy", l);
        
        IBindingSpecification bs1 = cc.getBinding("condition");
        assertSame(l, bs1.getLocation());
        assertEquals(BindingType.PREFIXED, bs1.getType());
        assertEquals("message", bs1.getValue());

        IBindingSpecification bs2 = cc.getBinding("element");
        assertEquals("div", bs2.getValue());
    }     
}
