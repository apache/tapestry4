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

package com.primix.tapestry.util;

import java.util.*;

/**
 *  Used to split a string into substrings based on a single character
 *  delimiter.  A fast, simple version of
 *  {@link StringTokenizer}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class StringSplitter
{
	private char delimiter;

	public StringSplitter(char delimiter)
	{
		this.delimiter = delimiter;
	}

	public char getDelimiter()
	{
		return delimiter;
	}

	/**
	*  Splits a string on the delimter into an array of String
	*  tokens.  The delimiters are not included in the tokens.  Null
	*  tokens (caused by two consecutive delimiter) are reduced to an
	*  empty string. Leading delimiters are ignored.
	*
	*/

	public String[] splitToArray(String value)
	{
		char[] buffer;
		int i;
		String[] result;
		int resultCount = 0;
		int start;
		int length;
		String token;
		String[] newResult;
		boolean first = true;

		buffer = value.toCharArray();

		result = new String[3];

		start = 0;
		length = 0;

		for (i = 0; i < buffer.length; i++)
		{
			if (buffer[i] != delimiter)
			{
				length++;
				continue;
			}

			// This is used to ignore leading delimiter(s).

			if (length > 0 || !first)
			{
				token = new String(buffer, start, length);

				if (resultCount == result.length)
				{
					newResult = new String[result.length * 2];

					System.arraycopy(result, 0, newResult, 0, result.length);

					result = newResult;
				}

				result[resultCount++] = token;

				first = false;
			}

			start = i + 1;
			length = 0;
		}

		// Special case:  if the string contains no delimiters
		// then it isn't really split.  Wrap the input string
		// in an array and return.  This is a little optimization
		// to prevent a new String instance from being
		// created unnecessarily.

		if (start == 0 && length == buffer.length)
		{
			result = new String[1];
			result[0] = value;
			return result;
		}

		// If the string is all delimiters, then this
		// will result in a single empty token.

		token = new String(buffer, start, length);

		newResult = new String[resultCount + 1];
		System.arraycopy(result, 0, newResult, 0, resultCount);
		newResult[resultCount] = token;

		return newResult;
	}
}