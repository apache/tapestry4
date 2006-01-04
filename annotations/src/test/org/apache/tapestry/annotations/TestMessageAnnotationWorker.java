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
import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.service.impl.ClassFactoryImpl;
import org.apache.tapestry.enhance.EnhanceUtils;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementOperationImpl;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;
import org.easymock.internal.ArrayMatcher;

public class TestMessageAnnotationWorker extends BaseAnnotationTestCase
{
    public void testConvertMethodNameToKeyName()
    {
        MessageAnnotationWorker w = new MessageAnnotationWorker();

        assertEquals("foo-bar", w.convertMethodNameToKeyName("fooBar"));
        assertEquals("foo-bar", w.convertMethodNameToKeyName("FooBar"));
        assertEquals("foo-bar", w.convertMethodNameToKeyName("getFooBar"));
        assertEquals("foo", w.convertMethodNameToKeyName("foo"));
    }

    public void testNoArgsMessage()
    {
        attempt("noArgsMessage", "{\n  return getMessages().getMessage(\"no-args-message\");\n}\n");
    }

    public void testMessageWithSpecificKey()
    {
        attempt(
                "messageWithSpecificKey",
                "{\n  return getMessages().getMessage(\"message-key\");\n}\n");
    }

    public void testMessageWithParameters()
    {
        attempt("messageWithParameters", "{\n"
                + "  java.lang.Object[] params = new java.lang.Object[2];\n"
                + "  params[0] = $1;\n" + "  params[1] = $2;\n"
                + "  return getMessages().format(\"message-with-parameters\", params);\n}\n");
    }

    public void testMessageWithPrimitiveParameters()
    {
        attempt("messageWithPrimitives", "{\n"
                + "  java.lang.Object[] params = new java.lang.Object[2];\n"
                + "  params[0] = ($w) $1;\n" + "  params[1] = ($w) $2;\n"
                + "  return getMessages().format(\"message-with-primitives\", params);\n}\n");
    }

    public void testNotStringReturnType()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, "voidMessage");

        replayControls();

        try
        {
            new MessageAnnotationWorker().performEnhancement(op, spec, method, null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "The method's return type is void; this annotation is only allowed on methods that return java.lang.String.",
                    ex.getMessage());
        }

        verifyControls();

    }

    public void testSetterIsClaimed()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, "getLikeGetter");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(method),
                "{\n  return getMessages().getMessage(\"like-getter\");\n}\n",
                l);
        op.claimReadonlyProperty("likeGetter");

        replayControls();

        new MessageAnnotationWorker().performEnhancement(op, spec, method, l);

        verifyControls();
    }

    private void attempt(String methodName, String codeBlock)
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, methodName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(method), codeBlock, l);

        replayControls();

        new MessageAnnotationWorker().performEnhancement(op, spec, method, l);

        verifyControls();
    }

    private Object construct(Class baseClass, String methodName, Messages messages)
    {
        Location l = newLocation();

        ComponentSpecification spec = new ComponentSpecification();
        EnhancementOperationImpl op = new EnhancementOperationImpl(getClassResolver(), spec,
                baseClass, new ClassFactoryImpl(), null);

        op.addInjectedField("_messages", Messages.class, messages);

        EnhanceUtils.createSimpleAccessor(op, "_messages", "messages", Messages.class, l);

        Method method = findMethod(baseClass, methodName);

        new MessageAnnotationWorker().performEnhancement(op, spec, method, l);

        ComponentConstructor cc = op.getConstructor();

        return cc.newInstance();
    }

    public void testNoParams()
    {
        MockControl control = newControl(Messages.class);
        Messages messages = (Messages) control.getMock();

        messages.getMessage("no-params");
        control.setReturnValue("<no params>");

        MessagesTarget mt = (MessagesTarget) construct(MessagesTarget.class, "noParams", messages);

        replayControls();

        assertEquals("<no params>", mt.noParams());

        verifyControls();
    }

    public void testObjectParam()
    {
        MockControl control = newControl(Messages.class);
        Messages messages = (Messages) control.getMock();

        Object[] params = new Object[]
        { "PinkFloyd" };

        messages.format("object-param", params);
        control.setMatcher(new ArrayMatcher());
        control.setReturnValue("<object param>");

        MessagesTarget mt = (MessagesTarget) construct(
                MessagesTarget.class,
                "objectParam",
                messages);

        replayControls();

        assertEquals("<object param>", mt.objectParam("PinkFloyd"));

        verifyControls();
    }

    public void testPrimitiveParam()
    {

        MockControl control = newControl(Messages.class);
        Messages messages = (Messages) control.getMock();

        Object[] params = new Object[]
        { 451 };

        messages.format("primitive-param", params);
        control.setMatcher(new ArrayMatcher());
        control.setReturnValue("<primitive param>");

        MessagesTarget mt = (MessagesTarget) construct(
                MessagesTarget.class,
                "primitiveParam",
                messages);

        replayControls();

        assertEquals("<primitive param>", mt.primitiveParam(451));

        verifyControls();

    }

    public void testInvalidBindings()
    {
        invalidBinding("no equals");
        invalidBinding("= at start");
        invalidBinding("equals at end=");
    }

    private void invalidBinding(String binding)
    {
        try
        {
            new ComponentAnnotationWorker().addBinding(null, binding, null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(AnnotationMessages.bindingWrongFormat(binding), ex.getMessage());
        }
    }
}
