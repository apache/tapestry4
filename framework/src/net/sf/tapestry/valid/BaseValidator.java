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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.tapestry.Tapestry;

/**
 *  Abstract base class for {@link IValidator}.  Supports a required and locale property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public abstract class BaseValidator implements IValidator
{
    private boolean _required;

    /**
     *  Standard constructor.  Leaves locale as system default and required as false.
     * 
     **/

    public BaseValidator()
    {
    }

    protected BaseValidator(boolean required)
    {
        this._required = required;
    }

    public boolean isRequired()
    {
        return _required;
    }

    public void setRequired(boolean required)
    {
        this._required = required;
    }

    /**
     *  Gets a string from the standard resource bundle.  The string in the bundle
     *  is treated as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object[] args)
    {
        ResourceBundle strings = ResourceBundle.getBundle("net.sf.tapestry.valid.ValidationStrings", locale);

        String pattern = strings.getString(key);

        return MessageFormat.format(pattern, args);
    }


    /**
     *  Convienience method for invoking {@link #getString(String, Locale, Object[])}.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object arg)
    {
        return getString(key, locale, new Object[] { arg });
    }

    /**
     *  Convienience method for invoking {@link #getString(String, Locale, Object[])}.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object arg1, Object arg2)
    {
        return getString(key, locale, new Object[] { arg1, arg2 });
    }

    /**
     *  Invoked to check if the value is null.  If the value is null (or empty),
     *  but the required flag is set, then this method throws a {@link ValidatorException}.
     *  Otherwise, returns true if the value is null.
     * 
     **/

    protected boolean checkRequired(IField field, String value) throws ValidatorException
    {
        boolean isNull = Tapestry.isNull(value);

        if (_required && isNull)
            throw new ValidatorException(
                getString("field-is-required", field.getPage().getLocale(), field.getDisplayName()),
                ValidationConstraint.REQUIRED,
                null);

        return isNull;
    }
}