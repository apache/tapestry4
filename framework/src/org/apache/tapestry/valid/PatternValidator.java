//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.valid;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.util.RegexpMatcher;

/**
 * <p>The validator bean that provides a pattern validation service.
 * 
 * <p>The actual pattern matching algorithm is provided by the 
 * {@link org.apache.tapestry.valid.PatternDelegate}. This enables the user to provide
 * custom pattern matching implementations. In the event a custom implementation is not 
 * provided, this validator will use the {@link org.apache.tapestry.util.RegexpMatcher}.
 * 
 * <p>This validator has the ability to provide client side validation on demand. 
 * To enable client side validation simply set the <code>clientScriptingEnabled</code>
 * property to <code>true</code>.
 * The default implementation of the script will be in JavaScript and allows the user to 
 * override this with a custom implementation by setting the path to the custom  
 * script via {@link #setScriptPath(String)}.
 * 
 * @author  Harish Krishnaswamy
 * @version $Id$
 * @since   3.0
 */
public class PatternValidator extends BaseValidator
{
    /**
     * The pattern that this validator will use to validate the input. The default 
     * pattern is an empty string.
     */
    private String _patternString = "";

    /**
     * A custom message in the event of a validation failure.
     */
    private String _patternNotMatchedMessage;

    /**
     * The object that handles pattern matching.
     */
    private PatternDelegate _patternDelegate;

    /**
     * The location of the script specification for client side validation.
     */
    private String _scriptPath = "/org/apache/tapestry/valid/PatternValidator.script";

    /**
     * Returns custom validation failure message. The default message comes from 
     * <code>ValidationStrings.properties</code> file for key 
     * <code>pattern-not-matched</code>.
     */
    public String getPatternNotMatchedMessage()
    {
        return _patternNotMatchedMessage;
    }

    /**
     * Returns the pattern that this validator uses for validation.
     */
    public String getPatternString()
    {
        return _patternString;
    }

    /**
     * Allows for a custom message to be set typically via the bean specification.
     */
    public void setPatternNotMatchedMessage(String message)
    {
        _patternNotMatchedMessage = message;
    }

    /**
     * Allows the user to change the validation pattern. 
     */
    public void setPatternString(String pattern)
    {
        _patternString = pattern;
    }

    /**
     * Static inner class that acts as a delegate to RegexpMatcher and conforms to the 
     * PatternDelegate contract.
     */
    private static class RegExpDelegate implements PatternDelegate
    {
        private RegexpMatcher _matcher;

        private RegexpMatcher getPatternMatcher()
        {
            if (_matcher == null)
                _matcher = new RegexpMatcher();

            return _matcher;
        }

        public boolean contains(String patternString, String input)
        {
            return getPatternMatcher().contains(patternString, input);
        }

        public String getEscapedPatternString(String patternString)
        {
            return getPatternMatcher().getEscapedPatternString(patternString);
        }
    }

    /**
     * Allows for a custom implementation to do the pattern matching. The default pattern 
     * matching is done with {@link org.apache.tapestry.util.RegexpMatcher}.
     */
    public void setPatternDelegate(PatternDelegate patternDelegate)
    {
        _patternDelegate = patternDelegate;
    }

    /**
     * Returns the custom pattern matcher if one is provided or creates and returns the 
     * default matcher laziliy.
     */
    public PatternDelegate getPatternDelegate()
    {
        if (_patternDelegate == null)
            _patternDelegate = new RegExpDelegate();

        return _patternDelegate;
    }

    /**
     * @see org.apache.tapestry.valid.IValidator#toString(org.apache.tapestry.form.IFormComponent, java.lang.Object)
     */
    public String toString(IFormComponent field, Object value)
    {
        if (value == null)
            return null;

        return value.toString();
    }

    private String buildPatternNotMatchedMessage(IFormComponent field, String patternString)
    {
        String templateMessage =
            getPattern(
                _patternNotMatchedMessage,
                "pattern-not-matched",
                field.getPage().getLocale());

        return formatString(templateMessage, field.getDisplayName(), patternString);
    }

    /**
     * @see org.apache.tapestry.valid.IValidator#toObject(org.apache.tapestry.form.IFormComponent, java.lang.String)
     */
    public Object toObject(IFormComponent field, String input) throws ValidatorException
    {
        if (checkRequired(field, input))
            return null;

        boolean matched = false;

        try
        {
            matched = getPatternDelegate().contains(_patternString, input);
        }
        catch (Throwable t)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "PatternValidator.pattern-match-error",
                    _patternString,
                    field.getDisplayName()),
                field,
                field.getLocation(),
                t);
        }

        if (!matched)
            throw new ValidatorException(
                buildPatternNotMatchedMessage(field, _patternString),
                ValidationConstraint.PATTERN_MISMATCH);

        return input;
    }

    /**
     * Allows for a custom implementation of the client side validation.
     */
    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    /**
     * @see org.apache.tapestry.valid.IValidator#renderValidatorContribution(org.apache.tapestry.form.IFormComponent, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (!isClientScriptingEnabled())
            return;

        Map symbols = new HashMap();

        if (isRequired())
            symbols.put("requiredMessage", buildRequiredMessage(field));

        symbols.put(
            "patternNotMatchedMessage",
            buildPatternNotMatchedMessage(field, getEscapedPatternString()));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

    /**
     * Returns the escaped sequence of the pattern string for rendering in the error message. 
     */
    public String getEscapedPatternString()
    {
        return getPatternDelegate().getEscapedPatternString(_patternString);
    }

    public String toString()
    {
        return "Pattern: "
            + _patternString
            + "; Script Path: "
            + _scriptPath
            + "; Pattern Delegate: "
            + _patternDelegate;
    }
}
