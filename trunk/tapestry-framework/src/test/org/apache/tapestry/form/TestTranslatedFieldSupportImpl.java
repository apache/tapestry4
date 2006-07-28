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

package org.apache.tapestry.form;

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.assertSame;

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Test case for {@link TranslatedFieldSupportImpl}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
@Test
public class TestTranslatedFieldSupportImpl extends BaseComponentTestCase
{
    private ThreadLocale newThreadLocale()
    {
        ThreadLocale tl = newMock(ThreadLocale.class);
        checkOrder(tl, false);
        
        expect(tl.getLocale()).andReturn(Locale.ENGLISH).anyTimes();

        return tl;
    }

    private IRequestCycle newCycle(IComponent component)
    {
        IRequestCycle cycle = newCycle();

        ClassResolver cr = newMock(ClassResolver.class);
        Infrastructure infra = newMock(Infrastructure.class);

        PageRenderSupport prs = newMock(PageRenderSupport.class);

        expect(cycle.getInfrastructure()).andReturn(infra);

        expect(infra.getClassResolver()).andReturn(cr);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        return cycle;
    }

    public void testRenderContributionsClientValidationDisabled()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();
        
        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        expect(field.getForm()).andReturn(form);

        expect(form.isClientValidationEnabled()).andReturn(false);

        replay();

        support.renderContributions(field, writer, cycle);

        verify();
    }

    public void testRenderContributionsClientValidationEnabled()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());

        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);
        
        Translator translator = newMock(Translator.class);

        expect(field.getForm()).andReturn(form);

        expect(form.isClientValidationEnabled()).andReturn(true);

        expect(field.getForm()).andReturn(form);

        expect(form.getName()).andReturn("myform");

        expect(field.getTranslator()).andReturn(translator);
        
        translator.renderContribution(
                eq(writer),
                eq(cycle),
                isA(FormComponentContributorContextImpl.class),
                eq(field));
        
        replay();

        support.renderContributions(field, writer, cycle);

        verify();
    }

    public void testFormat() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();
        
        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);
        
        IValidationDelegate delegate = newMock(IValidationDelegate.class);
        Translator translator = newMock(Translator.class);

        Object object = new Object();
        String expected = "result";

        expect(field.getForm()).andReturn(form);

        expect(form.getDelegate()).andReturn(delegate);

        expect(delegate.isInError()).andReturn(false);

        expect(field.getTranslator()).andReturn(translator);

        support.setThreadLocale(newThreadLocale());
        
        trainFormat(translator, field, object, expected);

        replay();

        String result = support.format(field, object);

        verify();

        assertSame(expected, result);
    }

    private void trainFormat(Translator translator, TranslatedField field,
            Object input, String result)
    {

        expect(translator.format(field, Locale.ENGLISH, input)).andReturn(result);
    }

    public void testFormatInError()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);
        
        IValidationDelegate delegate = newMock(IValidationDelegate.class);

        String expected = "result";

        expect(field.getForm()).andReturn(form);

        expect(form.getDelegate()).andReturn(delegate);

        expect(delegate.isInError()).andReturn(true);

        expect(delegate.getFieldInputValue()).andReturn(expected);

        replay();

        String result = support.format(field, new Object());

        verify();

        assertSame(expected, result);
    }

    public void testParse() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);
        
        IValidationDelegate delegate = newMock(IValidationDelegate.class);
        Translator translator = newMock(Translator.class);

        String text = "test";
        Object expected = new Object();

        expect(field.getForm()).andReturn(form);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.recordFieldInputValue(text);
        
        support.setThreadLocale(newThreadLocale());
        
        expect(field.getTranslator()).andReturn(translator);
        
        trainParse(translator, field, text, expected);
        
        replay();

        Object result = support.parse(field, text);

        verify();

        assertSame(expected, result);

    }

    private void trainParse(Translator translator, TranslatedField field,
            String text, Object result) throws ValidatorException
    {
        // ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);

        expect(translator.parse(eq(field), isA(ValidationMessages.class), eq(text))).andReturn(result);
    }

    public void testParseFailed() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        support.setThreadLocale(newThreadLocale());

        TranslatedField field = newMock(TranslatedField.class);
        IForm form = newMock(IForm.class);
        
        IValidationDelegate delegate = newMock(IValidationDelegate.class);
        Translator translator = newMock(Translator.class);

        String text = "test";

        expect(field.getForm()).andReturn(form);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.recordFieldInputValue(text);

        expect(field.getTranslator()).andReturn(translator);
        
        ValidatorException expected = new ValidatorException("Failure");

        //ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);
        
        expect(translator.parse(isA(TranslatedField.class), 
                isA(ValidationMessages.class), isA(String.class))).andThrow(expected);
        
        replay();

        try
        {
            support.parse(field, text);

            unreachable();
        }
        catch (ValidatorException e)
        {
            verify();

            assertSame(expected, e);
        }
    }
}
