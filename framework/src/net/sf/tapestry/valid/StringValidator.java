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
    private static final class StaticStringValidator extends StringValidator
    {
        private static final String UNSUPPORTED_MESSAGE = "Changes to property values are not allowed.";

        private StaticStringValidator(boolean required)
        {
            super(required);
        }

        /** @throws UnsupportedOperationException **/

        public void setMinimumLength(int minimumLength)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }

        /** @throws UnsupportedOperationException **/

        public void setRequired(boolean required)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }
        
        /** @throws UnsupportedOperationException **/
                
        public void setClientScriptingEnabled(boolean clientScriptingEnabled)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }
   }

    /**
     *  Returns a shared instance of a StringValidator with the required flag set.
     *  The instance is not modifiable.
     *
     **/

    public static final StringValidator REQUIRED = new StaticStringValidator(true);

    /**
     *  Returns a shared instance of a StringValidator with the required flag cleared.
     *  The instance is not modifiable.
     *
     **/

    public static final StringValidator OPTIONAL = new StaticStringValidator(false);

    private int _minimumLength;

    public StringValidator()
    {
    }

    private StringValidator(boolean required)
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

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimumLength > 0))
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

        processValidatorScript("/net/sf/tapestry/valid/StringValidator.script", cycle, field, symbols);
    }

}