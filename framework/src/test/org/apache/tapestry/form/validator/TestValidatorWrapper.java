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

package org.apache.tapestry.form.validator;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;

/**
 * Tests for {@link org.apache.tapestry.form.validator.AbstractValidatorWrapper}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidatorWrapper extends BaseValidatorTestCase
{
    public static class Fixture extends AbstractValidatorWrapper
    {
        private final Validator _delegate;

        public Fixture(Validator delegate)
        {
            _delegate = delegate;
        }

        protected Validator getDelegate()
        {
            return _delegate;
        }
    }

    private Validator newValidator()
    {
        return (Validator) newMock(Validator.class);
    }

    public void testValidate() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();
        Object value = new Object();

        Validator delegate = newValidator();

        delegate.validate(field, messages, value);

        replay();

        new Fixture(delegate).validate(field, messages, value);

        verify();
    }

    public void testRenderContribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormComponentContributorContext context = newContext();
        IFormComponent field = newField();

        Validator delegate = newValidator();

        delegate.renderContribution(writer, cycle, context, field);

        replay();

        new Fixture(delegate).renderContribution(writer, cycle, context, field);

        verify();
    }

    public void testGetAcceptsNull()
    {
        Validator delegate = newMock(Validator.class);

        expect(delegate.getAcceptsNull()).andReturn(true);

        replay();

        Validator wrapper = new Fixture(delegate);

        assertEquals(true, wrapper.getAcceptsNull());

        verify();

        expect(delegate.getAcceptsNull()).andReturn(false);

        replay();

        assertEquals(false, wrapper.getAcceptsNull());

        verify();
    }

    public void testIsRequired()
    {
        Validator delegate = newMock(Validator.class);

        expect(delegate.isRequired()).andReturn(true);

        replay();

        Validator wrapper = new Fixture(delegate);

        assertEquals(true, wrapper.isRequired());

        verify();

        expect(delegate.isRequired()).andReturn(false);

        replay();

        assertEquals(false, wrapper.isRequired());

        verify();
    }
}
