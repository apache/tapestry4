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

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class FormMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(FormMessages.class,
            "FormStrings");

    static String formTooManyIds(IComponent form, int actualCount, IComponent component)
    {
        return _formatter.format(
                "form-too-many-ids",
                form.getExtendedId(),
                new Integer(actualCount),
                component.getExtendedId());
    }

    static String formIdMismatch(IComponent form, int mismatchIndex, String expectedId,
            String actualId, IComponent component)
    {
        return _formatter.format("form-id-mismatch", new Object[]
        { form.getExtendedId(), new Integer(mismatchIndex + 1), expectedId, actualId,
                component.getExtendedId() });
    }

    static String formTooFewIds(IComponent form, int remainingCount, String nextExpectedId)
    {
        return _formatter.format("form-too-few-ids", form.getExtendedId(), new Integer(
                remainingCount), nextExpectedId);
    }

    static String encodingTypeContention(IComponent form, String establishedEncodingType,
            String newEncodingType)
    {
        return _formatter.format(
                "encoding-type-contention",
                form.getExtendedId(),
                establishedEncodingType,
                newEncodingType);
    }

    static String fieldAlreadyPrerendered(IComponent field)
    {
        return _formatter.format("field-already-prerenderer", field);
    }
}