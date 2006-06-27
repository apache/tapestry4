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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.Locale;

import org.apache.hivemind.Messages;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.valid.ValidationStrings;

/**
 * Tests for {@link org.apache.tapestry.form.ValidationMessagesImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidationMessages extends BaseComponentTestCase
{
    private IFormComponent newField()
    {
        return (IFormComponent) newMock(IFormComponent.class);
    }

    public void testMessageOverrideNull()
    {
        IFormComponent field = newField();

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replay();

        assertEquals("You must enter a value for My Field.", m.formatValidationMessage(
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        m = new ValidationMessagesImpl(field, new Locale("es"));

        assertEquals("Tiene que ingresar un valor para My Field.", m.formatValidationMessage(
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        verify();
    }

    public void testGetLocale()
    {
        IFormComponent field = newField();

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replay();

        assertSame(Locale.ENGLISH, m.getLocale());

        verify();
    }

    public void testMessageOverride()
    {
        IFormComponent field = newField();

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replay();

        assertEquals("Gimme data for My Field.", m.formatValidationMessage(
                "Gimme data for {0}.",
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        verify();
    }

    /**
     * Test the use of the '%key' construct as the message.
     */

    public void testMessageOverrideAsReference()
    {
        Messages messages = newMessage("myfield-required", "Yo Dawg! Gimme a piece of {0}.");
        IComponent container = newComponent(messages);
        IFormComponent field = newField(container);

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replay();

        assertEquals("Yo Dawg! Gimme a piece of My Field.", m.formatValidationMessage(
                "%myfield-required",
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        verify();
    }

    private IFormComponent newField(IComponent container)
    {
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getContainer()).andReturn(container);

        return field;
    }

    private IComponent newComponent(Messages messages)
    {
        IComponent component = newComponent();

        expect(component.getMessages()).andReturn(messages);

        return component;
    }

    private Messages newMessage(String key, String message)
    {
        Messages messages = newMock(Messages.class);

        expect(messages.getMessage(key)).andReturn(message);

        return messages;
    }
}
