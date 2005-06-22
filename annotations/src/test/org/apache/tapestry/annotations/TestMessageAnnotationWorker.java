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
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

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
        attempt("noArgsMessage", "return getMessages().getMessage(\"no-args-message\");");
    }

    public void testMessageWithSpecificKey()
    {
        attempt("messageWithSpecificKey", "return getMessages().getMessage(\"message-key\");");
    }

    public void testMessageWithParameters()
    {
        attempt(
                "messageWithParameters",
                "return getMessages().format(\"message-with-parameters\", new java.lang.Object[] { $1, $2 });");
    }

    public void testMessageWithPrimitiveParameters()
    {
        attempt(
                "messageWithPrimitives",
                "return getMessages().format(\"message-with-primitives\", new java.lang.Object[] { ($w) $1, ($w) $2 });");
    }

    public void testNotStringReturnType()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, "voidMessage");

        replayControls();

        try
        {
            new MessageAnnotationWorker().performEnhancement(op, spec, method);
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
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, "getLikeGetter");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(method),
                "return getMessages().getMessage(\"like-getter\");");
        op.claimProperty("likeGetter");

        replayControls();

        new MessageAnnotationWorker().performEnhancement(op, spec, method);

        verifyControls();
    }

    private void attempt(String methodName, String codeBlock)
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Method method = findMethod(AnnotatedPage.class, methodName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(method), codeBlock);

        replayControls();

        new MessageAnnotationWorker().performEnhancement(op, spec, method);

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
            new ComponentAnnotationWorker().addBinding(null, binding);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(AnnotationMessages.bindingWrongFormat(binding), ex.getMessage());
        }
    }
}
