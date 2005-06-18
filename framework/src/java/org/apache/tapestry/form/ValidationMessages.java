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

/**
 * Support interface used to provide access to validation messages. Typically used by
 * {@link org.apache.tapestry.form.validator.Validator}s.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface ValidationMessages
{
    /**
     * Formats a validation message. Automatically chooses the correct localization.
     * 
     * @param messageOverride
     *            a supplied override to the default message format taken from the
     *            ValidationStrings.properties message catalog.
     * @param messageKey
     *            used to look up pa message format when messageOverride is blank (null or empty)
     * @param arguments
     *            array of arguments formatted with the message format
     */

    public String formatValidationMessage(String messageOverride, String messageKey,
            Object[] arguments);
}
