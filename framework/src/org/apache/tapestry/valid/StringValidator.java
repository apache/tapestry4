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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;

/**
 *  Simple validation of strings, to enforce required, and minimum length
 *  (maximum length is enforced in the client browser, by setting a maximum input
 *  length on the text field).
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class StringValidator extends BaseValidator
{
    private int _minimumLength;

    private String _minimumLengthMessage;

    /** @since 2.2 **/

    private String _scriptPath = "/org/apache/tapestry/valid/StringValidator.script";

    public StringValidator()
    {
    }

    private StringValidator(boolean required)
    {
        super(required);
    }

    public String toString(IFormComponent field, Object value)
    {
        if (value == null)
            return null;

        return value.toString();
    }

    public Object toObject(IFormComponent field, String input) throws ValidatorException
    {
        if (checkRequired(field, input))
            return null;

        if (_minimumLength > 0 && input.length() < _minimumLength)
            throw new ValidatorException(
                buildMinimumLengthMessage(field),
                ValidationConstraint.MINIMUM_WIDTH);

        return input;
    }

    public int getMinimumLength()
    {
        return _minimumLength;
    }

    public void setMinimumLength(int minimumLength)
    {
        _minimumLength = minimumLength;
    }

    /** 
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimumLength > 0))
            return;

        Map symbols = new HashMap();

        if (isRequired())
            symbols.put("requiredMessage", buildRequiredMessage(field));

        if (_minimumLength > 0)
            symbols.put("minimumLengthMessage", buildMinimumLengthMessage(field));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

    /**
     *  @since 2.2
     * 
     **/

    public String getScriptPath()
    {
        return _scriptPath;
    }

    /**
     *  Allows a developer to use the existing validation logic with a different client-side
     *  script.  This is often sufficient to allow application-specific error presentation
     *  (perhaps by using DHTML to update the content of a &lt;span&gt; tag, or to use
     *  a more sophisticated pop-up window than <code>window.alert()</code>).
     * 
     *  @since 2.2
     * 
     **/

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    /** @since 3.0 */
    public String getMinimumLengthMessage()
    {
        return _minimumLengthMessage;
    }

    /** 
     * Overrides the <code>field-too-short</code> bundle key.
     * Parameter {0} is the minimum length.
     * Parameter {1} is the display name of the field.
     * 
     * @since 3.0
     */

    public void setMinimumLengthMessage(String string)
    {
        _minimumLengthMessage = string;
    }

    /** @since 3.0 */

    protected String buildMinimumLengthMessage(IFormComponent field)
    {
        String pattern =
            getPattern(_minimumLengthMessage, "field-too-short", field.getPage().getLocale());

        return formatString(pattern, Integer.toString(_minimumLength), field.getDisplayName());
    }

}