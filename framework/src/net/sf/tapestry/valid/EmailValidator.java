//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

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

    public String toString(IField field, Object value)
    {
        if (value == null)
            return null;

        return value.toString();
    }

    public Object toObject(IField field, String input) throws ValidatorException
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

        if (input.length() > 0 && ! isValidEmail(input))
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

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
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
                getString("field-too-short", locale, Integer.toString(_minimumLength), displayName));

        symbols.put("emailFormatMessage",
                    getString("invalid-email-format", locale, displayName));

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
        
        if ((atIndex == -1) || (atIndex == 0) || (atIndex == email.length() -1))
        {
            return false;    
        }
        else
        {
            return true;    
        }
    }
}