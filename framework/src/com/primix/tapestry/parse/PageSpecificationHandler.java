package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import java.util.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.xml.*;
/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Processes the &lt;page&gt; element within an &lt;application&gt;
 *  element.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class PageSpecificationHandler extends AbstractElementHandler
{
	private ApplicationSpecification applicationSpecification;
	private PageSpecification pageSpecification;
	private String name;

	/**
	*  Invokes {@link
	*  ApplicationSpecification#setPageSpecification(String,
	*  PageSpecification)}
	*/

		public void endElement()
	throws AssemblerException
	{
		applicationSpecification.setPageSpecification(name, pageSpecification);
	}

	/**
	*  Returns this <code>PageDefinitionHandler</code>.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	public PageSpecification getPageSpecification()
	{
		return pageSpecification;
	}

	public void reset()
	{
		characterData = null;
		name = null;
		applicationSpecification = null;
		pageSpecification = null;
	}

	/**
	*  Sets the buffer size for the page.  This is expressed as a
	*  string.  The string specifies the size of the buffer, either
	*  in bytes or in kilobytes by appending a 'k'.
	*
	*  The exact value accepted is defined by the regular expression:
	*  [0-9]+ *(k|K)
	*
	*  @throws IllegalArgumentException if the string is not formatted acceptibly
	*/

		public void setBufferSize(String value)
	{
		int bytes = 0;
		char[] digits;
		char digit;
		int i;
		boolean requireDigit = true;
		boolean acceptModifier = false;
		boolean acceptDigit = true;
		boolean invalid = false;

		// Leading, trailing whitespace is already trimmed away.

		digits = value.toCharArray();

		for (i = 0; i < digits.length; i++)
		{
			digit = digits[i];

			if (digit >= '0' && digit <= '9')
			{
				if (!acceptDigit)
				{
					invalid = true;
					break;
				}

				bytes = (10 * bytes) + (digit - '0');

				acceptModifier = true;
				requireDigit = false;
				continue;
			}

			acceptDigit = false;

			if (requireDigit)
			{
				invalid = true;
				break;
			}

			// One or more spaces allowed between the base quantity and the modifier

			if (digit == ' ')
			{
				acceptDigit = false;
				continue;
			}

			if (!acceptModifier)
			{
				invalid = true;
				break;
			}


			if (digit == 'k' || digit == 'K')
			{
				bytes *= 1024;

				// Continue loop, but this should be the last letter.

				continue;
			}

			// Unrecognized character

			invalid = true;
			break;
		}


		if (invalid)
			throw new IllegalArgumentException("Invalid buffer size specification: '" +
				value + "'.");

		pageSpecification.setBufferSize(bytes);
	}

	public void setName(String value)
	{
		name = value;
	}

	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler("buffer-size", new PropertyElementHandler("bufferSize"));
		addHandler("specification-path", new PropertyElementHandler("pageSpecification.specificationPath"));
		// handler for properties ...
	}

	/**
	* Expects the parentFocus to be a {@link ApplicationSpecification}.
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		name = null;
		pageSpecification = new PageSpecification();

		applicationSpecification = (ApplicationSpecification)parentFocus;
	}
}

