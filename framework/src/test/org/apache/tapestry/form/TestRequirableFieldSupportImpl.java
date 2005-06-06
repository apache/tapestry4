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

package org.apache.tapestry.form;

import java.text.MessageFormat;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Test case for {@link RequirableFieldSupportImpl}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestRequirableFieldSupportImpl extends TapestryTestCase
{
    private RequirableFieldSupport _support = new RequirableFieldSupportImpl();
    
    private MockControl _componentControl = MockControl.createControl(RequirableField.class);
    private RequirableField _component = (RequirableField) _componentControl.getMock();
    
    private MockControl _writerControl = MockControl.createControl(IMarkupWriter.class);
    private IMarkupWriter _writer = (IMarkupWriter) _writerControl.getMock();

    private MockControl _cycleControl = MockControl.createControl(IRequestCycle.class);
    private IRequestCycle _cycle = (IRequestCycle) _cycleControl.getMock();

    private MockControl _formControl = MockControl.createControl(IForm.class);
    private IForm _form = (IForm) _formControl.getMock();
    
    private MockControl _delegateControl = MockControl.createControl(IValidationDelegate.class);
    private IValidationDelegate _delegate = (IValidationDelegate) _delegateControl.getMock();
    
    /**
     * @see org.apache.hivemind.test.HiveMindTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        _componentControl.reset();
        _writerControl.reset();
        _cycleControl.reset();
        _formControl.reset();
        
        super.tearDown();
    }

    private void replay()
    {
        _componentControl.replay();
        _writerControl.replay();
        _cycleControl.replay();
        _formControl.replay();
    }

    private void verify()
    {
        _componentControl.verify();
        _writerControl.verify();
        _cycleControl.verify();
        _formControl.verify();
    }
    
    public void testNotRequiredRender()
    {
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _component.isRequired();
        _componentControl.setReturnValue(false);
        
        replay();
        
        _support.render(_component, _writer, _cycle);
        
        verify();
    }
    
    public void testRequiredClientValidationDisabledRender()
    {
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _component.isRequired();
        _componentControl.setReturnValue(true);

        _form.isClientValidationEnabled();
        _formControl.setReturnValue(false);
        
        replay();
        
        _support.render(_component, _writer, _cycle);
        
        verify();
    }

    public void testRequiredClientValidationEnabledRender()
    {
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _component.isRequired();
        _componentControl.setReturnValue(true);

        _form.isClientValidationEnabled();
        _formControl.setReturnValue(true);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        _component.getRequiredMessage();
        _componentControl.setReturnValue("You must enter a value for {0}.");
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Name");

        _form.addEventHandler(FormEventType.SUBMIT, "require(document.formName.fieldName,'You must enter a value for Field Name.')");
        _formControl.setVoidCallable();
        
        replay();
        
        _support.render(_component, _writer, _cycle);
        
        verify();
    }
    
    public void testNotRequiredRewind()
    {
        _component.getSubmittedValue(_cycle);
        _componentControl.setReturnValue("value");
        
        _component.isRequired();
        _componentControl.setReturnValue(false);

        try
        {
            _component.bind(_writer, _cycle);
            _componentControl.setVoidCallable();
        
            replay();
            
            _support.rewind(_component, _writer, _cycle);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        finally
        {
            verify();
        }
    }
    
    public void testRequiredBlankRewind()
    {
        _component.getSubmittedValue(_cycle);
        _componentControl.setReturnValue("");
        
        _component.isRequired();
        _componentControl.setReturnValue(true);

        String pattern = "You must enter a value for {0}.";
        
        _component.getRequiredMessage();
        _componentControl.setReturnValue(pattern);
        
        String displayName = "Field Name";
        _component.getDisplayName();
        _componentControl.setReturnValue(displayName);

        String message = MessageFormat.format(pattern, new Object[] { displayName });
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getDelegate();
        _formControl.setReturnValue(_delegate);
        
        _delegate.record(new ValidatorException(message, ValidationConstraint.REQUIRED));
        _delegateControl.setVoidCallable();
        
        replay();
        
        _support.rewind(_component, _writer, _cycle);
        
        verify();
    }
    
    public void testRequiredNotBlankRewind()
    {
        _component.getSubmittedValue(_cycle);
        _componentControl.setReturnValue("value");
        
        _component.isRequired();
        _componentControl.setReturnValue(true);
        
        try
        {
            _component.bind(_writer, _cycle);
            _componentControl.setVoidCallable();
        
            replay();
            
            _support.rewind(_component, _writer, _cycle);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        finally
        {
            verify();
        }
    }
}
