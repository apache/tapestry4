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

import org.apache.hivemind.Messages;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.valid.ValidationStrings;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.ValidationMessagesImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidationMessages extends HiveMindTestCase
{
    private IFormComponent newField()
    {
        return (IFormComponent) newMock(IFormComponent.class);
    }

    public void testMessageOverrideNull()
    {
        IFormComponent field = newField();

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replayControls();

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

        verifyControls();
    }

    public void testMessageOverride()
    {
        IFormComponent field = newField();

        ValidationMessages m = new ValidationMessagesImpl(field, Locale.ENGLISH);

        replayControls();

        assertEquals("Gimme data for My Field.", m.formatValidationMessage(
                "Gimme data for {0}.",
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        verifyControls();
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

        replayControls();

        assertEquals("Yo Dawg! Gimme a piece of My Field.", m.formatValidationMessage(
                "%myfield-required",
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "My Field" }));

        verifyControls();
    }

    private IFormComponent newField(IComponent container)
    {
        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getContainer();
        control.setReturnValue(container);

        return field;
    }

    private IComponent newComponent(Messages messages)
    {
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        component.getMessages();
        control.setReturnValue(messages);

        return component;
    }

    private Messages newMessage(String key, String message)
    {
        MockControl control = newControl(Messages.class);
        Messages messages = (Messages) control.getMock();

        messages.getMessage(key);
        control.setReturnValue(message);

        return messages;
    }
}
