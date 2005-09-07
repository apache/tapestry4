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

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.test.EqualsMatcher;
import org.apache.hivemind.test.TypeMatcher;
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
import org.easymock.MockControl;
import org.easymock.internal.AlwaysMatcher;

/**
 * Test case for {@link TranslatedFieldSupportImpl}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestTranslatedFieldSupportImpl extends BaseComponentTestCase
{
    private ThreadLocale newThreadLocale()
    {
        MockControl control = newControl(ThreadLocale.class);
        ThreadLocale tl = (ThreadLocale) control.getMock();

        tl.getLocale();
        control.setReturnValue(Locale.ENGLISH);

        return tl;
    }

    private IRequestCycle newCycle(IComponent component)
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        ClassResolver cr = (ClassResolver) newMock(ClassResolver.class);

        MockControl infrac = newControl(Infrastructure.class);
        Infrastructure infra = (Infrastructure) infrac.getMock();

        PageRenderSupport prs = (PageRenderSupport) newMock(PageRenderSupport.class);

        cycle.getInfrastructure();
        cyclec.setReturnValue(infra);

        infra.getClassResolver();
        infrac.setReturnValue(cr);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        return cycle;
    }

    public void testRenderContributionsClientValidationDisabled()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        field.getForm();
        fieldControl.setReturnValue(form);

        form.isClientValidationEnabled();
        formControl.setReturnValue(false);

        replayControls();

        support.renderContributions(field, writer, cycle);

        verifyControls();
    }

    public void testRenderContributionsClientValidationEnabled()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);

        MockControl translatorControl = newControl(Translator.class);
        Translator translator = (Translator) translatorControl.getMock();

        field.getForm();
        fieldControl.setReturnValue(form);

        form.isClientValidationEnabled();
        formControl.setReturnValue(true);

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getName();
        formControl.setReturnValue("myform");

        field.getTranslator();
        fieldControl.setReturnValue(translator);

        translator.renderContribution(
                writer,
                cycle,
                new FormComponentContributorContextImpl(field),
                field);
        translatorControl.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { new EqualsMatcher(), new EqualsMatcher(), new TypeMatcher(), new EqualsMatcher() }));

        replayControls();

        support.renderContributions(field, writer, cycle);

        verifyControls();
    }

    public void testFormat() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        MockControl delegateControl = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegateControl.getMock();

        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();

        Object object = new Object();
        String expected = "result";

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getDelegate();
        formControl.setReturnValue(delegate);

        delegate.isInError();
        delegateControl.setReturnValue(false);

        field.getTranslator();
        fieldControl.setReturnValue(translator);

        trainFormat(translatorc, translator, field, object, expected);

        support.setThreadLocale(newThreadLocale());

        replayControls();

        String result = support.format(field, object);

        verifyControls();

        assertSame(expected, result);
    }

    private void trainFormat(MockControl control, Translator translator, TranslatedField field,
            Object input, String result)
    {

        translator.format(field, Locale.ENGLISH, input);
        control.setReturnValue(result);
    }

    public void testFormatInError()
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        MockControl delegateControl = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegateControl.getMock();

        String expected = "result";

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getDelegate();
        formControl.setReturnValue(delegate);

        delegate.isInError();
        delegateControl.setReturnValue(true);

        delegate.getFieldInputValue();
        delegateControl.setReturnValue(expected);

        replayControls();

        String result = support.format(field, new Object());

        verifyControls();

        assertSame(expected, result);
    }

    public void testParse() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        MockControl delegateControl = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegateControl.getMock();

        MockControl translatorControl = newControl(Translator.class);
        Translator translator = (Translator) translatorControl.getMock();

        String text = "test";
        Object expected = new Object();

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getDelegate();
        formControl.setReturnValue(delegate);

        delegate.recordFieldInputValue(text);

        field.getTranslator();
        fieldControl.setReturnValue(translator);

        support.setThreadLocale(newThreadLocale());

        trainParse(translatorControl, translator, field, text, expected);

        replayControls();

        Object result = support.parse(field, text);

        verifyControls();

        assertSame(expected, result);

    }

    private void trainParse(MockControl control, Translator translator, TranslatedField field,
            String text, Object result) throws ValidatorException
    {
        ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);

        translator.parse(field, messages, text);
        control.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, new TypeMatcher() }));
        control.setReturnValue(result);
    }

    public void testParseFailed() throws Exception
    {
        TranslatedFieldSupportImpl support = new TranslatedFieldSupportImpl();

        support.setThreadLocale(newThreadLocale());

        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();

        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();

        MockControl delegateControl = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegateControl.getMock();

        MockControl translatorControl = newControl(Translator.class);
        Translator translator = (Translator) translatorControl.getMock();

        String text = "test";

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getDelegate();
        formControl.setReturnValue(delegate);

        delegate.recordFieldInputValue(text);

        field.getTranslator();
        fieldControl.setReturnValue(translator);

        ValidatorException expected = new ValidatorException("Failure");

        ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);

        translator.parse(field, messages, text);
        translatorControl.setMatcher(new AlwaysMatcher());
        translatorControl.setThrowable(expected);

        replayControls();

        try
        {
            support.parse(field, text);

            unreachable();
        }
        catch (ValidatorException e)
        {
            verifyControls();

            assertSame(expected, e);
        }
    }
}
