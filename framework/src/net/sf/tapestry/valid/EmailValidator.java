/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.valid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Simple validation of email strings, to enforce required, and minimum length
 *  (maximum length is enforced in the client browser, by setting a maximum input
 *  length on the text field).
 *
 *
 *  @author Malcolm Edgar
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class EmailValidator extends BaseValidator
{
    private int _minimumLength;

    private String _scriptPath = "/net/sf/tapestry/valid/EmailValidator.script";

    public EmailValidator()
    {
    }

    private EmailValidator(boolean required)
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

        input = input.trim();

        if (_minimumLength > 0 && input.length() < _minimumLength)
        {
            String errorMessage =
                getString(
                    "field-too-short",
                    field.getPage().getLocale(),
                    Integer.toString(_minimumLength),
                    field.getDisplayName());

            throw new ValidatorException(errorMessage, ValidationConstraint.MINIMUM_WIDTH, input);
        }

        if (input.length() > 0 && !isValidEmail(input))
        {
            String errorMessage =
                getString(
                    "invalid-email-format",
                    field.getPage().getLocale(),
                    field.getDisplayName());

            throw new ValidatorException(errorMessage, ValidationConstraint.EMAIL_FORMAT, input);
        }

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

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!isClientScriptingEnabled())
            return;

        Map symbols = new HashMap();

        Locale locale = field.getPage().getLocale();
        String displayName = field.getDisplayName();

        if (isRequired())
            symbols.put("requiredMessage", getString("field-is-required", locale, displayName));

        if (_minimumLength > 0)
            symbols.put(
                "minimumLengthMessage",
                getString(
                    "field-too-short",
                    locale,
                    Integer.toString(_minimumLength),
                    displayName));

        symbols.put("emailFormatMessage", getString("invalid-email-format", locale, displayName));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

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
     **/

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    /**
     *  Return true if the email format is valid.
     * 
     *  @param email the email string to validate
     *  @return true if the email format is valid
     */

    protected boolean isValidEmail(String email)
    {
        int atIndex = email.indexOf('@');

        if ((atIndex == -1) || (atIndex == 0) || (atIndex == email.length() - 1))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}