/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
 
package net.sf.tapestry.valid;

import net.sf.tapestry.*;
import net.sf.tapestry.Tapestry;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  Abstract base class for {@link IValidator}.  Supports a required and locale property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public abstract class BaseValidator implements IValidator
{
	private boolean required;
	private Locale locale;
	private ResourceBundle strings;
	
	/**
	 *  Standard constructor.  Leaves locale as null and required as false.
	 * 
	 **/
	
	public BaseValidator()
	{
	}
	
	protected BaseValidator(boolean required)
	{
		this.required = required;
	}
	
	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public boolean isRequired()
	{
		return required;
	}
	
	public void setRequired(boolean required)
	{
		this.required = required;
	}


	/**
	 *  Gets a string from the standard resource bundle.  The string in the bundle
	 *  is treated as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
	 * 
	 **/
	
	protected String getString(String key, Object[] args)
	{
		if (strings == null)
		{
			Locale bundleLocale = locale;
			
			if (bundleLocale == null)
				bundleLocale = Locale.getDefault();
				
			strings = ResourceBundle.getBundle("com.primix.tapestry.valid.ValidationStrings", bundleLocale);
		}
		
		String pattern = strings.getString(key);
		
		return MessageFormat.format(pattern, args);
	}
	
	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 **/
	
	protected String getString(String key, Object arg)
	{
		return getString(key, new Object[] { arg });
	}
	
	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 **/

	protected String getString(String key, Object arg1, Object arg2)
	{
		return getString(key, new Object[] { arg1, arg2 });
	}
	
	/**
	 *  Invoked to check if the value is null.  If the value is null (or empty),
	 *  but the required flag is set, then this method throws a {@link TranslatorException}.
	 *  Otherwise, returns true if the value is null.
	 * 
	 **/
	
	protected boolean checkRequired(IField field, String value)
	throws ValidatorException
	{
		boolean isNull = Tapestry.isNull(value);
		
		if (required && isNull)
			throw new ValidatorException(
			getString("field-is-required", field.getDisplayName()),
			ValidationConstraint.REQUIRED,
			null);
			
		return isNull;
	}
}

