package com.primix.servlet;

/*
 * ServletUtils - Support library for improved Servlets and JavaServer Pages.
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 * included    with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

import java.awt.Color;
import java.util.BitSet;
import javax.servlet.http.HttpServletRequest;

/**
 * A set of functions used when building URLs or encoding URL attributes.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class HTMLUtils
{

	/**
	 * Identifies which characters are safe in a URL, and do not need any encoding.
	 *
	 */

	private static BitSet safe;
	static
	{
		int i;
		safe = new BitSet(256);
		
		for (i = 'a'; i <= 'z'; i++)
			safe.set(i);
			
		for (i = 'A'; i <= 'Z'; i++)
			safe.set(i);
			
		for (i = '0'; i <= '9'; i++)
			safe.set(i);
			
		safe.set('.');
		safe.set('-');
		safe.set('_');
		safe.set('*');
	}

	/**
	 * Used to quickly convert a 8 bit character value to a hex string.
	 */

	private static final char HEX[] = {'0', '1', '2', '3', '4', '5', '6', 
		'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
/**
 *  Prevent instantiation.
 *
 */

private HTMLUtils()
{
}
/**
 * Builds a URL from a base name, adding query elements as necessary. It performs
 * escaping of the values in the query string, such as converting spaces to '+',
 * much like <code>URLEncoder.encode()</code>. <p>If there's
 * any doubt about the client's ability to use cookies, this should be passed
 * through <code>HttpServletResponse.encodeURL()</code> before inclusion on the generated page.
 * @param basePath The main component of the URL, such as "/servlets/SomeClass". This is often
 * obtained from <code>HttpRequest.getServletPath()</code>.
 *
 * @param pathInfo An array of values. Each value is appended to the basePath and preceded with
 * a forward slash. Each element is encoded. The array may be empty or
 * null.
 *
 * @param query An array of values used to form the query portion of the
 * URL, which comes after the base path and any pathInfo segments.
 * The even numbered elements (starting with element zero) are the names of query parameters
 * the odd
 * numbered elements are the parameter values. The array must have an even number
 * of elements, be empty, or be null.
 *
 * @return The final URL with proper URL encoding of all query parameters.
 */

public static String buildURL(String basePath, String[] pathInfo, String[] query)
{
	StringBuffer buffer;
	char sep = '?';
	boolean encodeFirst = false;
	String parameterName;
	String value;
	int i;

	// Start with the base path
	
	buffer = new StringBuffer(basePath);
	
	// If there's any path info, add them, seperated
	// by slashes.
	
	if (pathInfo != null)
	{
		for (i = 0; i < pathInfo.length; i++)
		{
			buffer.append('/');
			encode(buffer, pathInfo[i], false);
		}
	}
	
	// If there's any query strings, pair them off as parameters
	// and values.
	
	if (query != null)
	{
		for (i = 0; i < query.length;)
		{
			parameterName = query[i++];

			// Will throw an exception if there isn't an even number of
			// elements in the query array.

			value = query[i++];

			// Append a seperator from the path or the previous parameter/value
			// pair.
			
			buffer.append(sep);
			
			encode(buffer, parameterName, encodeFirst);
			buffer.append('=');
			encode(buffer, value, false);

			// Becuase the next parameter name will follow an '&' we want to encode
			// the first character as a hex value to prevent it from resembling
			// an HTML entity.

			sep = '&';
			encodeFirst = true;
		}
	}

	return buffer.toString();
}
private static int convertHex(char ch)
{
	if (ch >= '0' && ch <= '9')
		return ch - '0';

	if (ch >= 'a' && ch <= 'f')
		return (ch - 'a') + 10;

	if (ch >= 'A' && ch <= 'F')
		return (ch - 'A') + 10;

	throw new IllegalArgumentException("Invalid hex character '" + ch + "'.");
}
/**
 *  Decodes an x-www-form-urlencoded string.
 *
 *  @param raw The encoded string.
 *  @return The decoded string.  Returns the empty string if raw is null.
 *  @throws IllegalArgumentException if the string is not properly encoded.
 *
 */

public static String decode(String raw)
{
	StringBuffer buffer;
	int i, length;
	char ch;
	int digit;
	char value;
	boolean invalid = false;
	
	if (raw == null)
		return "";
		
	length = raw.length();
	buffer = new StringBuffer(length);
	
	try
	{
		for (i = 0; i < length; i++)
		{
			ch = raw.charAt(i);

			// A '+' is the encoding of a space.
			
			if (ch == '+')
			{
				buffer.append(' ');
				continue;
			}

			// These characters are not encoded
			
			if ((ch >= 'a' && ch <= 'z') || 
				(ch >= 'A' && ch <= 'Z') || 
				(ch >= '0' && ch <= '9'))
			{
				buffer.append(ch);
				continue;
			}

			// Otherwise, the only legit character is the '%', which leads
			// off two hex digits for the character in question.

			if (ch != '%')
			{
				invalid = true;
				break;
			}

			// Advance and get the high order digit.

			ch = raw.charAt(++i);
			digit = convertHex(ch);
			value = (char) (digit << 4);

			// Advance and get the low order digit

			ch = raw.charAt(++i);
			digit = convertHex(ch);
			value += digit;

			// Append the character

			buffer.append(value);
		}
	}
	catch (IllegalArgumentException e)
	{
		// Thrown by convertHex() if the character following a '%' is not a hex digit.
		
		invalid = true;
	}

	// Catch exceptions when string too short after '%'

	catch (IndexOutOfBoundsException e)
	{
		invalid = true;
	}

	if (invalid)
		throw new IllegalArgumentException("'" + raw + 
			"' is not a valid x-www-form-urlencoded String.");

	return buffer.toString();
}
/**
 * Applies URL encoding to the value, appending the result to the
 * <code>StringBuffer</code>.
 * <p>Why would you want to encode the first character of a string? Well,
 * <em>IE 4.0</em>
 * has a bug or feature where it would convert HTML entities <i>inside</i> a URL.
 * If a query parameter happened to have a name that looked like an HTML entity,
 * it got compressed down to a single character (even if it <em>didn't end in a
 * semicolon</em>). The end result is that the URL is invalid. This is addressed
 * by encoding the first character after each ampersand so that it is impossible
 * to generate an entity. Any ampersands in a query parameter name or value
 * will be encoded as a hex value so the only real ampersands will be those
 * seperating query parameters.
 *
 */

protected static void encode(StringBuffer buffer, String value, boolean encodeFirst)
{
	int i, length;
	char ch;
	boolean encode;
	length = value.length();
	
	for (i = 0; i < length; i++)
	{
		// Reduce any Unicode characters to just the low byte.

		ch = (char) (value.charAt(i) & 0xff);

		// The space is a special case.

		if (ch == ' ')
		{
			buffer.append('+');
			continue;
		}

		// Encode if not a safe character, or if the first and encodeFirst is
		// set.

		if (i == 0 && encodeFirst)
			encode = true;
		else
			encode = !safe.get(ch);
			
		if (encode)
		{
			// Express the character as a two digit hex constant.

			buffer.append('%');
			buffer.append(HEX[ch >> 4]);
			buffer.append(HEX[ch & 0x0F]);
		}
		else
			buffer.append(ch);

	} // for i

}
/**
 * Encodes a <code>Color</code> in the standard HTML format:
 * a pound sign ('#'), followed by six hex digits for
 * specifying the red, green and blue components of the color.
 *
 */

public static String encodeColor(Color color)
{
	StringBuffer buffer;
	int component;
	
	buffer = new StringBuffer(7);
	buffer.append('#');

	// Red

	component = color.getRed();
	buffer.append(HEX[component >> 4]);
	buffer.append(HEX[component & 0x0F]);

	// Green

	component = color.getGreen();
	buffer.append(HEX[component >> 4]);
	buffer.append(HEX[component & 0x0F]);

	// Blue
	component = color.getBlue();
	buffer.append(HEX[component >> 4]);
	buffer.append(HEX[component & 0x0F]);
	
	return buffer.toString();
}
/**
 * Does some easy checks to turn a path into an absolute URL. We assume
 * <ul>
 * <li>The presense of a colon means the path is complete already (any other colons
 * in the URI portion should have been converted to %3A).
 *
 * <li>A leading pair of forward slashes means the path is simply missing
 * the scheme.
 * <li>Otherwise, we assemble the scheme, server, port (if non-zero) and the path
 * as given.
 * </ul>
 *
 * <p>The Servlet 2.2 API claims to do this, and do it right. This is just
 * a stopgap.
 *
 */

public static String getAbsoluteURL(String path, String scheme, String server, int port)
{
	StringBuffer buffer = new StringBuffer();

	// Though, really, what does a leading colon with no scheme before it
	// mean?

	if (path.indexOf(':') >= 0)
		return path;

	// Should check the length here, first.

	if (path.substring(0, 1).equals("//"))
	{
		buffer.append(scheme);
		buffer.append(':');
		buffer.append(path);
		return buffer.toString();
	}
	
	buffer.append(scheme);
	buffer.append("://");
	buffer.append(server);
	
	if (port > 0)
	{
		buffer.append(':');
		buffer.append(port);
	}
	
	if (path.charAt(0) != '/')
		buffer.append('/');
	
	buffer.append(path);
	
	return buffer.toString();
}
public static String getAbsoluteURL(String path, HttpServletRequest request)
{
	String server;
	int port;
	String scheme;
	scheme = request.getScheme();
	server = request.getServerName();
	port = request.getServerPort();

	// Keep things simple ... port 80 is accepted as the
	// standard port for http so it can be ommitted.

	if (scheme.equals("http") && port == 80)
		port = 0;
		
	return getAbsoluteURL(path, scheme, server, port);
}
}
