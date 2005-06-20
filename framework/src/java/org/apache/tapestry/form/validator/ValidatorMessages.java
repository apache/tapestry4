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

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard Lewis Ship
 * @since 4.0
 */
class ValidatorMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(
            ValidatorMessages.class, "ValidatorStrings");

    public static String unknownValidator(String name)
    {
        return _formatter.format("unknown-validator", name);
    }

    public static String needsConfiguration(String name)
    {
        return _formatter.format("needs-configuration", name);
    }

    public static String notConfigurable(String name, String value)
    {
        return _formatter.format("not-configurable", name, value);
    }

    public static String errorInitializingValidator(String name, Class validatorClass,
            Throwable cause)
    {
        return _formatter.format(
                "error-initializing-validator",
                name,
                validatorClass.getName(),
                cause);
    }

    public static String badSpecification(String specification)
    {
        return _formatter.format("bad-specification", specification);
    }
}
