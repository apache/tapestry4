/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.valid;

import java.util.HashMap;
import java.util.Locale;
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