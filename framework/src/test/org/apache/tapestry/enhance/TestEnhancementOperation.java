// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.service.impl.ClassFactoryImpl;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.components.Insert;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.EnhancementOperationImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestEnhancementOperation extends HiveMindTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();

        EnhancementOperationImpl._uid = 97;
    }

    public abstract static class Fixture
    {
        public abstract String getStringProperty();

        public abstract boolean isBooleanProperty();

        public abstract boolean getFlagProperty();
    }

    public abstract static class GetClassReferenceFixture
    {
        public abstract Class getClassReference();
    }

    public static class MissingConstructorFixture
    {
        public MissingConstructorFixture(String foo)
        {
            //
        }
    }

    public void testClaimedProperty()
    {
        EnhancementOperationImpl eo = new EnhancementOperationImpl();

        eo.claimProperty("foo");
        eo.claimProperty("bar");

        try
        {
            eo.claimProperty("foo");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(EnhanceMessages.claimedProperty("foo"), ex.getMessage());
        }
    }

    public void testConstructorAndAccessors()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        replayControls();

        assertSame(spec, eo.getSpecification());
        assertSame(BaseComponent.class, eo.getBaseClass());

        verifyControls();
    }

    public void testValidatePropertyNew()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        replayControls();

        // Validates because BaseComponent doesn't have such a property.

        eo.validateProperty("abraxis", Set.class);

        verifyControls();
    }

    public void testValidatePropertyMatches()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        replayControls();

        // Validates because BaseComponent inherits a page property of type IPage

        eo.validateProperty("page", IPage.class);

        verifyControls();
    }

    public void testValidatePropertyMismatch()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        replayControls();

        // Validates because BaseComponent inherits a page property of type IPage

        try
        {
            eo.validateProperty("page", String.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(EnhanceMessages.propertyTypeMismatch(
                    BaseComponent.class,
                    "page",
                    IPage.class,
                    String.class), ex.getMessage());
        }

        verifyControls();
    }

    public void testConvertTypeName()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        replayControls();

        assertSame(boolean.class, eo.convertTypeName("boolean"));
        assertSame(float[].class, eo.convertTypeName("float[]"));
        assertSame(double[][].class, eo.convertTypeName("double[][]"));

        assertSame(Set.class, eo.convertTypeName(Set.class.getName()));

        assertSame(List[].class, eo.convertTypeName(List.class.getName() + "[]"));

        try
        {
            eo.convertTypeName("package.DoesNotExist");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            // Ignore message, it's from HiveMind anyway.
        }

        verifyControls();

    }

    public void testAddField()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);

        MockControl cfc = newControl(ClassFactory.class);
        ClassFactory cf = (ClassFactory) cfc.getMock();

        ClassFab fab = (ClassFab) newMock(ClassFab.class);

        cf.newClass("$BaseComponent_97", BaseComponent.class, Thread.currentThread()
                .getContextClassLoader());

        cfc.setReturnValue(fab);

        fab.addField("fred", String.class);

        replayControls();

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        eo.addField("fred", String.class);

        assertEquals(true, eo.hasEnhancements());

        verifyControls();
    }

    public void testAddMethod()
    {
        Class baseClass = Insert.class;
        MethodSignature sig = new MethodSignature(void.class, "frob", null, null);

        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);

        MockControl cfc = newControl(ClassFactory.class);
        ClassFactory cf = (ClassFactory) cfc.getMock();

        MockControl fabc = newControl(ClassFab.class);
        ClassFab fab = (ClassFab) fabc.getMock();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // We force the uid to 97 in setUp()

        cf.newClass("$Insert_97", baseClass, classLoader);

        cfc.setReturnValue(fab);

        fab.addMethod(Modifier.PUBLIC, sig, "method body");
        fabc.setReturnValue(null);

        replayControls();

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                baseClass, cf);

        eo.addMethod(Modifier.PUBLIC, sig, "method body");

        assertEquals(true, eo.hasEnhancements());

        verifyControls();
    }

    public void testGetAccessorMethodName()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        replayControls();

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                Fixture.class, cf);

        assertEquals("getStringProperty", eo.getAccessorMethodName("stringProperty"));
        assertEquals("isBooleanProperty", eo.getAccessorMethodName("booleanProperty"));
        assertEquals("getFlagProperty", eo.getAccessorMethodName("flagProperty"));
        assertEquals("getUnknownProperty", eo.getAccessorMethodName("unknownProperty"));

        verifyControls();
    }

    /**
     * On this test, instead of mocking up everything, we actually use the raw implementations to
     * construct a new class; the class gets a class reference passed to it in its constructor.
     */

    public void testGetClassReference() throws Exception
    {
        Location l = fabricateLocation(99);
        MockControl specControl = newControl(IComponentSpecification.class);

        IComponentSpecification spec = (IComponentSpecification) specControl.getMock();

        spec.getLocation();
        specControl.setReturnValue(l);

        replayControls();

        EnhancementOperationImpl eo = new EnhancementOperationImpl(new DefaultClassResolver(),
                spec, GetClassReferenceFixture.class, new ClassFactoryImpl());

        // This does two things; it creates a new field, and it sets up a new constructor
        // parameter to inject the class value (Map.class) into each new instance.

        String ref = eo.getClassReference(Map.class);
        String ref2 = eo.getClassReference(Map.class);

        eo.addMethod(Modifier.PUBLIC, new MethodSignature(Class.class, "getClassReference", null,
                null), "return " + ref + ";");

        ComponentConstructor cc = eo.getConstructor();

        GetClassReferenceFixture f = (GetClassReferenceFixture) cc.newInstance();

        assertSame(Map.class, f.getClassReference());

        verifyControls();

        assertSame(ref, ref2);
    }

    /**
     * Test exception reporting when the base component class does not contain the requisite
     * no-arguments constructor.
     */

    public void testMissingConstructor()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);

        replayControls();

        EnhancementOperationImpl eo = new EnhancementOperationImpl(new DefaultClassResolver(),
                spec, MissingConstructorFixture.class, new ClassFactoryImpl());

        try
        {
            eo.getConstructor();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Class org.apache.tapestry.enhance.TestEnhancementOperation$MissingConstructorFixture does not define a public, no arguments constructor.",
                    ex.getMessage());
        }

        verifyControls();
    }

    /**
     * Really a test for {@link org.apache.tapestry.enhance.ComponentConstructorImpl};
     * {@link #testGetClassReference()}tests the success case, just want to fill in the failure.
     */

    public void testComponentConstructorFailure()
    {
        Location l = fabricateLocation(13);

        ComponentConstructor cc = new ComponentConstructorImpl(BaseComponent.class
                .getConstructors()[0], new Object[]
        { "unexpected" }, l);

        try
        {
            cc.newInstance();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(
                    ex,
                    "Unable to instantiate instance of class org.apache.tapestry.BaseComponent");
            assertSame(l, ex.getLocation());
        }
    }

    public void testGetPropertyType()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        ClassFactory cf = (ClassFactory) newMock(ClassFactory.class);

        replayControls();

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        assertEquals(Map.class, eo.getPropertyType("assets"));
        assertEquals(IPage.class, eo.getPropertyType("page"));

        // Doesn't exist, so returns null

        assertNull(eo.getPropertyType("foosball"));

        verifyControls();
    }

    public void testForceEnhancement()
    {
        IComponentSpecification spec = (IComponentSpecification) newMock(IComponentSpecification.class);
        MockControl cfc = newControl(ClassFactory.class);
        ClassFactory cf = (ClassFactory) cfc.getMock();

        ClassFab fab = (ClassFab) newMock(ClassFab.class);

        cf.newClass("$BaseComponent_97", BaseComponent.class, Thread.currentThread()
                .getContextClassLoader());
        cfc.setReturnValue(fab);

        replayControls();

        EnhancementOperation eo = new EnhancementOperationImpl(new DefaultClassResolver(), spec,
                BaseComponent.class, cf);

        eo.forceEnhancement();

        verifyControls();
    }
}