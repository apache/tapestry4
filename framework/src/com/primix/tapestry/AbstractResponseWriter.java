package com.primix.tapestry;

import java.io.*;
import javax.servlet.ServletOutputStream;
import java.util.*;
import java.text.Format;
import java.text.NumberFormat;

/*
 * Tapestry Web Application Framework
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
 * Abstract base class implementing the {@link IResponseWriter} interface.
 * This class is used to create a Generic Tag Markup Language (GTML) output.   
 * It is more sophisticated than <code>PrintWriter</code> in that it maintains   
 * a concept hierarchy of open GTML tags. It also supplies a number of other
 * of the features that are useful when creating GTML.
 *
 * Elements are started with the {@link #begin(String)} 
 * or {@link #beginEmpty(String)}
 * methods. Once they are started, attributes for the elements may be set with
 * the various <code>attribute()</code> methods. The element is closed off
 * (i.e., the closing '&gt;' character is written) when any other method
 * is invoked (exception: methods which do not produce output, such as
 * {@link #flush()}). The <code>end()</code> methods end an element,
 * writing an GTML close tag to the output.
 *
 * <p>TBD:
 * <ul>
 * <li>Support XML and XHTML
 *  <li>What to do with Unicode characters with a value greater than 255?
 * </ul>
 *
 * <p>This class is derived from the original class 
 * <code>com.primix.servlet.HTMLWriter</code>
 * part of the <b>ServletUtils</b> framework available from
 * <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant 
 * Java Tree</a>.
 *
 * @version $Id$
 * @author Howard Ship, David Solis
 * @since 0.2.9
 */

public abstract class AbstractResponseWriter
implements IResponseWriter
{
	/**
	* The underlying {@link PrintWriter} that output is sent to.  
	*/

	protected PrintWriter writer;

	/**
	* Indicates whether a tag is open or not. A tag is opened by
	* {@link #begin(String)} or {@link #beginEmpty(String)}.
	* It stays open while calls to the <code>attribute()</code>
	* methods are made. It is closed
	* (the '&gt;' is written) when any other method is invoked.
	*
	*/

	protected boolean openTag = false;

	/**
	* A Stack of Strings used to track the active tag elements. Elements are active
	* until the corresponding close tag is written.  The {@link #push(String)} method
	* adds elements to the stack, {@link #pop()} removes them.
	*
	*/

	private Stack activeElementStack;

	/**
	* The depth of the open tag stack.
	* @see #activeElementStack
	*
	*/

	private int depth = 0;

	private char[] buffer;

	private String[] entities;

	/**
	 *  Implemented in concrete subclasses to provide a mapping from characters
	 *  to entities.  The offset in the array corresponds to the
	 *  character, the String is the literal text to insert into the
	 *  output stream as a replacement.  A value of null (which is prevalent)
	 *  means no subsititution.  We're using this as a fast-lookup on a single
	 *  character, otherwise we might use a {@link Map}.
	 *
	 */

	abstract protected String[] getEntities();

	private boolean[] safe;

	/**
	 *  Implemented in concrete subclasses to provide an indication of which
	 *  characters are 'safe' to insert directly into the response.  The index
	 *  into the array is the character, if the value at the index is false (or the
	 *  index out of range), then the character is escaped.
	 *
	 */

	abstract protected boolean[] getSafe();

	/**
	 *  Implemented in concrete subclasses to identify the MIME content type
	 *  produced by this response writer.
	 *
	 */

	abstract public String getContentType();

	abstract public IResponseWriter getNestedWriter();

	/**
	*  Protected constructor, needed by to construct a nested response writer.
	*
	*  <p>Invokes {@link #getEntities()} and {@link #getSafe()} to determine the
	*  necessary translatons for the response writer.
	*
	*/

	protected AbstractResponseWriter()
	{
		entities = getEntities();
		safe = getSafe();
	}

	/**
	*  Sends output to the stream.  Internally, an instance of <code>PrintWriter</code>
	*  is created, which will be closed when the <code>HTMLResponseWriter</code>
	*  is closed.
	*
	*/

	public AbstractResponseWriter(OutputStream stream)
	{
		this();
		writer = new PrintWriter(stream);
	}


	/**
	* Simply prints the attribute name. This is used for
	* idempotent attributes, such as 'disabled' in an
	* &lt;input&gt;.
	*
	* <p>TBD: Check that name is legal.
	*
	* @throws IllegalStateException if there is no open tag.
	*/

	public void attribute(String name)
	{
		checkTagOpen();

		writer.print(' ');
		writer.print(name);
	}

	/**
	* Writes an integer attribute into the currently open tag.
	*
	* <p>TBD: Validate that name is legal.
	*
	* @throws IllegalStateException if there is no open tag.
	*
	*/

	public void attribute(String name, int value)
	{
		checkTagOpen();

		writer.print(' ');
		writer.print(name);
		writer.print("=\"");
		writer.print(value);
		writer.print('"');
	}

	/**
	* Writes an attribute into the most recently opened tag. This must be called after
	* {@link #begin(String)}
	* and before any other kind of writing (which closes the tag).
	*
	* <p>The value may be null, in which case this method behaves the same as
	* {@link #attribute(String)}.
	*
	* <p>Troublesome characters in the value are converted to thier GTML entities, much
	* like a <code>print()</code> method, with the following exceptions:
	*  <ul>
	*  <li>The double quote (&quot;) is converted to &amp;quot;
	*  <li>The ampersand (&amp;) is passed through unchanged
	*  </ul>
	*
	* @throws IllegalStateException if there is no open tag.
	*/


	public void attribute(String name, String value)
	{
		checkTagOpen();
		int length;

		writer.print(' ');

		// Could use a check here that name contains only valid characters

		writer.print(name);
		if (value == null)
			return;

		length = value.length();

		if (buffer == null ||
			buffer.length < length)
			buffer = new char[length];

		value.getChars(0, length, buffer, 0);

		// Have to assume that ANY attribute could be a URL and allow the ampersand
		// as legit.

		writer.print("=\"");
		safePrint(buffer, 0, length, true);
		writer.print('"');
	}

	/**
	* Closes any existing tag then starts a new element. The new element is pushed
	* onto the active element stack.
	*/

	public void begin(String name)
	{
		if (openTag)
			closeTag();

		push(name);

		writer.print('<');
		writer.print(name);

		openTag = true;
	}

	/**
	* Starts an element that will not later be matched with an <code>end()</code>
	* call. This is useful for elements such as &lt;hr;&gt; or &lt;br&gt; that
	* do not need closing tags.
	*
	*/

	public void beginEmpty(String name)
	{
		if (openTag)
			closeTag();

		writer.print('<');
		writer.print(name);

		openTag = true;
	}

	/**
	* Invokes <code>checkError()</code> on the
	*  <code>PrintWriter</code> used to format output.
	*/

	public boolean checkError()
	{
		return writer.checkError();
	}

	private void checkTagOpen()
	{
		if (!openTag)
			throw new IllegalStateException("A tag must be open before attributes " +
				"may be set in an IResponseWriter.");
	}

	/**
	* Closes this <code>IResponseWriter</code>. Any active elements are closed. The
	* {@link PrintWriter} is then  sent {@link PrintWriter#close()}.
	*
	*/

	public void close()
	{
		String name;

		if (openTag)
			closeTag();

		// Close any active elements.

		while (depth > 0)
		{
			name = pop();
			writer.print("</");
			writer.print(name);
			writer.print('>');
		}

		writer.close();

		writer = null;
		activeElementStack = null;
		buffer = null;
	}

	/**
	* Closes the most recently opened element by writing the '&gt;' that ends
	* it. Once this is invoked, the <code>attribute()</code> methods
	* may not be used until a new element is opened with {@link #begin(String)} or
	* or {@link #beginEmpty(String)}.
	*/

	public void closeTag()
	{
		writer.print('>');

		openTag = false;
	}

	/**
	* Writes an GTML comment. Any open tag is first closed. 
	* The method takes care of
	* providing the <code>&lt;!--</code> and <code>--&gt;</code>, 
	* including a blank line after the close of the comment.
	*
	* <p>Most characters are valid inside an GTML comment, so no check
	* of the contents is made (much like {@link #printRaw(String)}.
	*
	*/

	public void comment(String value)
	{
		if (openTag)
			closeTag();

		writer.print("<!-- ");
		writer.print(value);
		writer.println(" -->");
	}

	/**
	* Ends the element most recently started by {@link #begin(String)}. 
	* The name of the tag
	* is popped off of the active element stack and used to form an GTML close tag.
	*
	* <p>TBD: Error checking for the open element stack empty.
	*/

	public void end()
	{
		String name;

		if (openTag)
			closeTag();

		name = pop();

		writer.print("</");
		writer.print(name);
		writer.print('>');
	}

	/**
	* Ends the most recently started element with the given name. This will
	* also end any other intermediate elements. This is very useful for easily
	* ending a table or even an entire page.
	*
	* <p>TBD: Error check if the name matches nothing on the open tag stack.
	*/

	public void end(String name)
	{
		String tagName;

		if (openTag)
			closeTag();

		while (true)
		{
			tagName = pop();

			writer.print("</");
			writer.print(tagName);
			writer.print('>');

			if (tagName.equals(name))
				break;
		}
	}

	/**
	* Forwards <code>flush()</code> to this <code>AbstractResponseWriter</code>'s 
	* <code>PrintWriter</code>.
	*
	*/

	public void flush()
	{
		writer.flush();
	}

	/**
	*  Removes the top element from the active element stack and returns it.
	*
	*/

	protected final String pop()
	{
		String result;

		result = (String)activeElementStack.pop();
		depth--;

		return result;
	}

	/**
	*
	* The primary <code>print()</code> method, used by most other methods.
	*
	* <p>Prints the character array, first closing any open tag. Problematic characters
	* ('&lt;', '&gt;' and '&amp;') are converted to their
	* GTML entities.
	*
	* <p>All 'unsafe' characters are properly converted to either a named
	* or numeric GTML entity.  This can be somewhat expensive, so use
	* {@link #printRaw(char[], int, int)} if the data to print is guarenteed
	* to be safe.
	*
	* <p>Does <em>nothing</em> if <code>data</code> is null.
	*
	* <p>Closes any open tag.
	*
	*/

	public void print(char[] data, int offset, int length)
	{
		if (data == null)
			return;

		if (openTag)
			closeTag();

		safePrint(data, offset, length, false);
	}

	/**
	* Prints a single character. If the character is not a 'safe' character,
	* such as '&lt;', then it's GTML entity (named or numeric) is printed instead.
	*
	* <p>Closes any open tag.
	*
	*/

	public void print(char value)
	{
		String entity = null;

		if (openTag)
			closeTag();

		if (value < safe.length &&
			safe[value])
		{
			writer.print(value);
			return;
		}

		if (value < entities.length)
			entity = entities[value];

		if (entity != null)
		{
			writer.print(entity);
			return;
		}

		// Not a well-known entity.  Print it's numeric equivalent.  Note:  this omits
		// the leading '0', but most browsers (IE 5.0) don't seem to mind.  Is this a bug?

		writer.print("&#" + (int)value + ";");		
	}

	/**
	* Prints an integer.
	*
	* <p>Closes any open tag.
	*
	*/

	public void print(int value)
	{
		if (openTag)
			closeTag();

		writer.print(value);
	}

	/**
	* Invokes {@link #print(char[], int, int)} to print the string.  Use
	* {@link #printRaw(String)} if the character data is known to be safe.
	*
	* <p>Does <em>nothing</em> if <code>value</code> is null.
	*
	* <p>Closes any open tag.
	*
	* @see #print(char[], int, int)
	*
	*/

	public void print(String value)
	{
		char[] data;
		int length;

		if (value == null)
			return;

		length = value.length();

		if (buffer == null ||
			buffer.length < length)
			buffer = new char[length];

		value.getChars(0, length, buffer, 0);

		print(buffer, 0, length);
	}

	/**
	* Closes the open tag (if any), then prints a line seperator to the output stream.
	*
	*/

	public void println()
	{
		if (openTag)
			closeTag();

		writer.println();
	}

	/**
	* Prints and portion of an output buffer to the stream.
	* No escaping of invalid GTML elements is done, which
	* makes this more effecient than <code>print()</code>. 
	* Does <em>nothing</em> if <code>buffer</code>
	* is null.
	*
	* <p>Closes any open tag.
	*
	*/

	public void printRaw(char[] buffer, int offset, int length)
	{
		if (buffer == null)
			return;

		if (openTag)
			closeTag();

		writer.write(buffer, offset, length);
	}

	/**
	* Prints output to the stream. No escaping of invalid GTML elements is done, which
	* makes this more effecient than <code>print()</code>. Does <em>nothing</em> 
	* if <code>value</code>
	* is null.
	*
	* <p>Closes any open tag.
	*
	*/

	public void printRaw(String value)
	{
		if (value == null)
			return;

		if (openTag)
			closeTag();

		writer.print(value);
	}

	/**
	*  Adds an element to the active element stack.
	*
	*/

	protected final void push(String name)
	{
		if (activeElementStack == null)
			activeElementStack = new Stack();

		activeElementStack.push(name);

		depth++;
	}

	/**
	* Internal support for safe printing.  Ensures that all characters emitted
	* are safe: either valid GTML characters or GTML entities (named or numeric).
	*/

	private void safePrint(char[] data, int offset, int length, boolean isURL)
	{
		int i;
		int start;
		char ch;
		int safelength = 0;
		String entity;
		boolean isSafe;

		start = offset;

		for (i = 0; i < length; i++)
		{
			ch = data[offset + i];

			// Ignore safe characters.  In an URL, ampersands
			// are OK but quotes are not.  Outside a URL, ampersands
			// won't be in safe[], but quotes will.

			isSafe = (ch < safe.length && safe[ch]);

			if (isURL)
			{
				if (ch == '&')
					isSafe = true;
				else
					if (ch == '"') 
					isSafe = false;
			}

			if (isSafe)
			{
				safelength++;
				continue;
			}

			// Write the safe stuff.

			if (safelength > 0)
				writer.write(data, start, safelength);

			entity = null;

			if (ch < entities.length)
				entity = entities[ch];

			if (entity == null)
				entity = "&#" + (int)ch + ";";

			writer.print(entity);

			start = offset + i + 1;
			safelength = 0;
		}

		if (safelength > 0)
			writer.write(data, start, safelength);
	}

}


