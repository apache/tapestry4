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
 
 package com.primix.tapestry.valid;

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
 */

public class StringValidator extends BaseValidator
{
	private int minimumLength;

	public String toString(IField field, Object value)
	{
		if (value == null)
			return null;

		return value.toString();
	}

	public Object toObject(IField field, String input)
		throws ValidatorException
	{
		if (checkRequired(field, input))
			return input;

		if (minimumLength > 0 && input.length() < minimumLength)
		{
			String errorMessage =
				getString(
					"number-too-small",
					field.getDisplayName(),
					Integer.toString(minimumLength));

			throw new ValidatorException(
				errorMessage,
				ValidationConstraint.MINIMUM_WIDTH,
				input);
		}

		return input;
	}

	public int getMinimumLength()
	{
		return minimumLength;
	}

	public void setMinimumLength(int minimumLength)
	{
		this.minimumLength = minimumLength;
	}

}