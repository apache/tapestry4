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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.test.EqualsMatcher;
import org.apache.hivemind.test.TypeMatcher;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.components.BaseComponentTestCase;
import org.apache.tapestry.form.validator.Validator;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Test case for {@link ValidatableFieldSupportImpl}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestValidatableFieldSupportImpl extends BaseComponentTestCase
{
    private ThreadLocale newThreadLocale()
    {
        MockControl control = newControl(ThreadLocale.class);
        ThreadLocale tl = (ThreadLocale) control.getMock();

        tl.getLocale();
        control.setReturnValue(Locale.ENGLISH);

        return tl;
    }
    
    /**
     * Lots of work to set up the request cycle here, since we have to train it about getting the
     * ClassResolver and the PageRenderSupport.
     */
    protected IRequestCycle newCycle(IComponent component)
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

        trainGetAttribute(cyclec, cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        return cycle;
    }

    public void testRenderContributionsClientValidationDisabled()
    {
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        
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

    public void testRenderContributionsClientValidationEnabledNoValidators()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);
        
        field.getForm();
        fieldControl.setReturnValue(form);

        form.isClientValidationEnabled();
        formControl.setReturnValue(true);

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getName();
        formControl.setReturnValue("myform");

        field.getName();
        fieldControl.setReturnValue("myfield");

        field.getValidators();
        fieldControl.setReturnValue(null);
        
        converter.coerceValue(null, Iterator.class);
        converterControl.setReturnValue(Collections.EMPTY_LIST.iterator());
        
        replayControls();

        support.renderContributions(field, writer, cycle);

        verifyControls();
    }

    public void testRenderContributionsClientValidationEnabled()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        MockControl formControl = newControl(IForm.class);
        IForm form = (IForm) formControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);
        
        MockControl validatorControl = newControl(Validator.class);
        Validator validator = (Validator) validatorControl.getMock();
        
        field.getForm();
        fieldControl.setReturnValue(form);

        form.isClientValidationEnabled();
        formControl.setReturnValue(true);

        field.getForm();
        fieldControl.setReturnValue(form);

        form.getName();
        formControl.setReturnValue("myform");

        field.getName();
        fieldControl.setReturnValue("myfield");

        field.getValidators();
        fieldControl.setReturnValue(validator);
        
        converter.coerceValue(validator, Iterator.class);
        converterControl.setReturnValue(Collections.singleton(validator).iterator());
        
        FormComponentContributorContext context = new FormComponentContributorContextImpl(field);
        
        validator.renderContribution(writer, cycle, context, field);
        validatorControl.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { new EqualsMatcher(), new EqualsMatcher(), new TypeMatcher(), new EqualsMatcher() }));        
        
        replayControls();

        support.renderContributions(field, writer, cycle);

        verifyControls();
    }

    public void testValidate()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        MockControl validatorControl = newControl(Validator.class);
        Validator validator = (Validator) validatorControl.getMock();
        
        Object object = new Object();

        field.getValidators();
        fieldControl.setReturnValue(validator);
        
        converter.coerceValue(validator, Iterator.class);
        converterControl.setReturnValue(Collections.singleton(validator).iterator());

        ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);
        
        try
        {
            validator.validate(field, messages, object);
            validatorControl.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
            { new EqualsMatcher(), new TypeMatcher(), new EqualsMatcher() }));
            
            replayControls();
    
            support.validate(field, writer, cycle, object);
    
            verifyControls();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateFailed()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        MockControl validatorControl = newControl(Validator.class);
        Validator validator = (Validator) validatorControl.getMock();
        
        Object object = new Object();

        field.getValidators();
        fieldControl.setReturnValue(validator);
        
        converter.coerceValue(validator, Iterator.class);
        converterControl.setReturnValue(Collections.singleton(validator).iterator());

        ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);
        
        ValidatorException expected = new ValidatorException("test");
        
        try
        {
            validator.validate(field, messages, object);
            validatorControl.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
            { new EqualsMatcher(), new TypeMatcher(), new EqualsMatcher() }));
            validatorControl.setThrowable(expected);
            
            replayControls();
    
            support.validate(field, writer, cycle, object);
    
            unreachable();
        }
        catch (ValidatorException e)
        {
            verifyControls();
            
            assertSame(expected, e);
        }
    }

    public void testValidateNoValidators()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Object object = new Object();

        field.getValidators();
        fieldControl.setReturnValue(null);
        
        converter.coerceValue(null, Iterator.class);
        converterControl.setReturnValue(Collections.EMPTY_LIST.iterator());
        
        try
        {
            replayControls();
    
            support.validate(field, writer, cycle, object);
    
            verifyControls();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateAcceptNull()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        MockControl validatorControl = newControl(Validator.class);
        Validator validator = (Validator) validatorControl.getMock();
        
        field.getValidators();
        fieldControl.setReturnValue(validator);
        
        converter.coerceValue(validator, Iterator.class);
        converterControl.setReturnValue(Collections.singleton(validator).iterator());

        validator.getAcceptsNull();
        validatorControl.setReturnValue(true);

        ValidationMessages messages = new ValidationMessagesImpl(field, Locale.ENGLISH);
        
        try
        {
            validator.validate(field, messages, null);
            validatorControl.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
            { new EqualsMatcher(), new TypeMatcher(), new EqualsMatcher() }));
            
            replayControls();
    
            support.validate(field, writer, cycle, null);
    
            verifyControls();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateRejectNull()
    {
        MockControl converterControl = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterControl.getMock();
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        MockControl fieldControl = newControl(TranslatedField.class);
        TranslatedField field = (TranslatedField) fieldControl.getMock();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        MockControl validatorControl = newControl(Validator.class);
        Validator validator = (Validator) validatorControl.getMock();
        
        field.getValidators();
        fieldControl.setReturnValue(validator);
        
        converter.coerceValue(validator, Iterator.class);
        converterControl.setReturnValue(Collections.singleton(validator).iterator());

        validator.getAcceptsNull();
        validatorControl.setReturnValue(false);
        
        try
        {
            replayControls();
    
            support.validate(field, writer, cycle, null);
    
            verifyControls();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    private ValidatableField newFieldGetValidators(Collection validators)
    {
        MockControl control = newControl(ValidatableField.class);
        ValidatableField field = (ValidatableField) control.getMock();

        field.getValidators();
        control.setReturnValue(validators);

        return field;
    }

    private ValueConverter newValueConverter(Collection validators)
    {
        MockControl control = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) control.getMock();

        converter.coerceValue(validators, Iterator.class);
        control.setReturnValue(validators.iterator());

        return converter;
    }

    private Validator newValidator(boolean isRequired)
    {
        MockControl control = newControl(Validator.class);
        Validator validator = (Validator) control.getMock();

        validator.isRequired();
        control.setReturnValue(isRequired);

        return validator;
    }

    public void testIsRequiredNoValidators()
    {
        Collection validators = Collections.EMPTY_LIST;
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replayControls();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(false, support.isRequired(field));

        verifyControls();
    }

    public void testIsRequiredNoRequiredValidators()
    {
        Collection validators = Collections.singletonList(newValidator(false));
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replayControls();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(false, support.isRequired(field));

        verifyControls();
    }

    public void testIsRequiredWithRequiredValidator()
    {
        Collection validators = Collections.singletonList(newValidator(true));
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replayControls();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(true, support.isRequired(field));

        verifyControls();
    }
}
