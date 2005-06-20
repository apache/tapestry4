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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.util.RegexpMatch;
import org.apache.tapestry.util.RegexpMatcher;

/**
 * Implementation of the tapestry.form.validator.ValidatorFactory service, which builds and caches
 * validators and lists of validators from a "magic" string specification.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ValidatorFactoryImpl implements ValidatorFactory
{
    private static final String PATTERN = "^\\s*(\\w+)\\s*(=\\s*(((?!,|\\[).)*))?";

    /**
     * Cache of List (of Validator) keyed on specification.
     */

    private Map _masterCache = new HashMap();

    private RegexpMatcher _matcher = new RegexpMatcher();

    /**
     * Injected map of validator names to ValidatorContribution.
     */

    private Map _validators;

    public synchronized List constructValidatorList(String specification)
    {
        List result = (List) _masterCache.get(specification);

        if (result == null)
        {
            result = buildValidatorList(specification);
            _masterCache.put(specification, result);
        }

        return result;
    }

    private List buildValidatorList(String specification)
    {
        if (HiveMind.isBlank(specification))
            return Collections.EMPTY_LIST;

        List result = new ArrayList();
        String chopped = specification;

        while (true)
        {
            if (chopped.length() == 0)
                break;

            if (!result.isEmpty())
            {
                if (chopped.charAt(0) != ',')
                    throw new ApplicationRuntimeException(ValidatorMessages
                            .badSpecification(specification));

                chopped = chopped.substring(1);
            }

            RegexpMatch[] matches = _matcher.getMatches(PATTERN, chopped);

            if (matches.length != 1)
                throw new ApplicationRuntimeException(ValidatorMessages
                        .badSpecification(specification));

            RegexpMatch match = matches[0];

            String name = match.getGroup(1);
            String value = match.getGroup(3);
            String message = null;

            int length = match.getMatchLength();

            if (chopped.length() > length)
            {
                char lastChar = chopped.charAt(length);
                if (lastChar == ',')
                    length--;
                else if (lastChar == '[')
                {
                    int messageClose = chopped.indexOf(']', length);
                    message = chopped.substring(length + 1, messageClose);
                    length = messageClose;
                }
            }

            Validator validator = buildValidator(name, value, message);

            result.add(validator);

            if (length >= chopped.length())
                break;
            else
                chopped = chopped.substring(length + 1);

        }

        return Collections.unmodifiableList(result);
    }

    private Validator buildValidator(String name, String value, String message)
    {
        ValidatorContribution vc = (ValidatorContribution) _validators.get(name);

        if (vc == null)
            throw new ApplicationRuntimeException(ValidatorMessages.unknownValidator(name));

        if (value == null && vc.isConfigurable())
            throw new ApplicationRuntimeException(ValidatorMessages.needsConfiguration("name"));

        if (value != null && !vc.isConfigurable())
            throw new ApplicationRuntimeException(ValidatorMessages.notConfigurable(name, value));

        try
        {
            Object result = vc.getValidatorClass().newInstance();

            if (vc.isConfigurable())
                PropertyUtils.smartWrite(result, name, value);

            if (message != null)
                PropertyUtils.write(result, "message", message);

            return (Validator) result;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ValidatorMessages.errorInitializingValidator(
                    name,
                    vc.getValidatorClass(),
                    ex), ex);
        }
    }

    public void setValidators(Map validators)
    {
        _validators = validators;
    }
}
