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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.form.validator.Validator;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.valid.ValidatorException;

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
        ThreadLocale tl = newMock(ThreadLocale.class);

        expect(tl.getLocale()).andReturn(Locale.ENGLISH);

        return tl;
    }
    
    /**
     * Lots of work to set up the request cycle here, since we have to train it about getting the
     * ClassResolver and the PageRenderSupport.
     */
    protected IRequestCycle newCycle(IComponent component)
    {
        IRequestCycle cycle = newCycle();

        ClassResolver cr = (ClassResolver) newMock(ClassResolver.class);
        
        Infrastructure infra = newMock(Infrastructure.class);

        PageRenderSupport prs = (PageRenderSupport) newMock(PageRenderSupport.class);

        expect(cycle.getInfrastructure()).andReturn(infra);

        expect(infra.getClassResolver()).andReturn(cr);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        return cycle;
    }

    public void testRenderContributionsClientValidationDisabled()
    {
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        
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

    public void testRenderContributionsClientValidationEnabledNoValidators()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);
        
        expect(field.getForm()).andReturn(form);

        expect(form.isClientValidationEnabled()).andReturn(true);

        expect(field.getForm()).andReturn(form);

        expect(form.getName()).andReturn("myform");

        expect(field.getValidators()).andReturn(null);
        
        expect(converter.coerceValue(null, Iterator.class)).andReturn(Collections.EMPTY_LIST.iterator());
        
        replay();

        support.renderContributions(field, writer, cycle);

        verify();
    }

    public void testRenderContributionsClientValidationEnabled()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(field);
        
        Validator validator = newMock(Validator.class);
        
        expect(field.getForm()).andReturn(form);

        expect(form.isClientValidationEnabled()).andReturn(true);

        expect(field.getForm()).andReturn(form);

        expect(form.getName()).andReturn("myform");

        expect(field.getValidators()).andReturn(validator);
        
        expect(converter.coerceValue(validator, Iterator.class))
        .andReturn(Collections.singleton(validator).iterator());
        
        validator.renderContribution(eq(writer), eq(cycle), 
                isA(FormComponentContributorContext.class), eq(field));
        
        replay();

        support.renderContributions(field, writer, cycle);

        verify();
    }

    public void testValidate()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Validator validator = newMock(Validator.class);
        
        Object object = new Object();

        expect(field.getValidators()).andReturn(validator);
        
        expect(converter.coerceValue(validator, Iterator.class))
        .andReturn(Collections.singleton(validator).iterator());
        
        try
        {
            validator.validate(eq(field), isA(ValidationMessages.class), eq(object));
            
            replay();
    
            support.validate(field, writer, cycle, object);
    
            verify();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateFailed()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Validator validator = newMock(Validator.class);
        
        Object object = new Object();

        expect(field.getValidators()).andReturn(validator);
        
        expect(converter.coerceValue(validator, Iterator.class))
        .andReturn(Collections.singleton(validator).iterator());
        
        ValidatorException expected = new ValidatorException("test");
        
        try
        {
            validator.validate(eq(field), isA(ValidationMessages.class), eq(object));
            expectLastCall().andThrow(expected);
            
            replay();
    
            support.validate(field, writer, cycle, object);
    
            unreachable();
        }
        catch (ValidatorException e)
        {
            verify();
            
            assertSame(expected, e);
        }
    }

    public void testValidateNoValidators()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Object object = new Object();

        expect(field.getValidators()).andReturn(null);
        
        expect(converter.coerceValue(null, Iterator.class))
        .andReturn(Collections.EMPTY_LIST.iterator());
        
        try
        {
            replay();
    
            support.validate(field, writer, cycle, object);
    
            verify();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateAcceptNull()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Validator validator = newMock(Validator.class);
        
        expect(field.getValidators()).andReturn(validator);
        
        expect(converter.coerceValue(validator, Iterator.class))
        .andReturn(Collections.singleton(validator).iterator());

        expect(validator.getAcceptsNull()).andReturn(true);
        
        try
        {
            validator.validate(eq(field), isA(ValidationMessages.class), isNull());
            
            replay();
    
            support.validate(field, writer, cycle, null);
    
            verify();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    public void testValidateRejectNull()
    {
        ValueConverter converter = newMock(ValueConverter.class);
        
        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setThreadLocale(newThreadLocale());
        support.setValueConverter(converter);
        
        TranslatedField field = newMock(TranslatedField.class);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        Validator validator = newMock(Validator.class);
        
        expect(field.getValidators()).andReturn(validator);
        
        expect(converter.coerceValue(validator, Iterator.class))
        .andReturn(Collections.singleton(validator).iterator());

        expect(validator.getAcceptsNull()).andReturn(false);
        
        try
        {
            replay();
    
            support.validate(field, writer, cycle, null);
    
            verify();
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
    }

    private ValidatableField newFieldGetValidators(Collection validators)
    {
        ValidatableField field = newMock(ValidatableField.class);

        expect(field.getValidators()).andReturn(validators);

        return field;
    }

    private ValueConverter newValueConverter(Collection validators)
    {
        ValueConverter converter = newMock(ValueConverter.class);

        expect(converter.coerceValue(validators, Iterator.class)).andReturn(validators.iterator());

        return converter;
    }

    private Validator newValidator(boolean isRequired)
    {
        Validator validator = newMock(Validator.class);

        expect(validator.isRequired()).andReturn(isRequired);

        return validator;
    }

    public void testIsRequiredNoValidators()
    {
        Collection validators = Collections.EMPTY_LIST;
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replay();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(false, support.isRequired(field));

        verify();
    }

    public void testIsRequiredNoRequiredValidators()
    {
        Collection validators = Collections.singletonList(newValidator(false));
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replay();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(false, support.isRequired(field));

        verify();
    }

    public void testIsRequiredWithRequiredValidator()
    {
        Collection validators = Collections.singletonList(newValidator(true));
        ValidatableField field = newFieldGetValidators(validators);
        ValueConverter converter = newValueConverter(validators);

        replay();

        ValidatableFieldSupportImpl support = new ValidatableFieldSupportImpl();
        support.setValueConverter(converter);

        assertEquals(true, support.isRequired(field));

        verify();
    }
}
