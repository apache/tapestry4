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
 *  Analyzes an exception, creating one or more 
 *  {@link ExceptionDescription}s
 *  from it.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


package com.primix.tapestry.util.exception;

import java.lang.reflect.*;
import java.beans.*;
import com.primix.tapestry.util.*;
import com.primix.tapestry.util.prop.*;
import java.util.*;
import java.io.*;
import gnu.regexp.*;

public class ExceptionAnalyzer
{
	private List exceptionDescriptions;
	private List propertyDescriptions;
	private CharArrayWriter writer;

	private static final int LIST_SIZE = 3;

	private boolean exhaustive = false;
	
	private RE re;

	private String PATTERN = "\\s+(at\\s+)?(\\w.+\\(.*\\))";

	// This pattern matches just the interesting lines from the stack trace.
	// It skips the class name and description at the front, some blank lines
	// at the end.  The matches ignore leading and trailing whitespace.
	// The pattern says:  after some leading whitespace, search for
	// a word character and extract form there, through and opening paren
	// to a close paren.
	// Sun's JVM tends to put the text 'at' at the beginning of lines.

	/**
	 *  If true, then stack trace is extracted for each exception.  If false,
	 *  the default, then stack trace is extracted for only the deepest exception.
	 *
	 */
	 
	public boolean isExhaustive()
	{
		return exhaustive;
	}
	
	public void setExhaustive(boolean value)
	{
		exhaustive = value;
	}

	/**
	*  Analyzes the exceptions.  This builds an {@link ExceptionDescription} for the
	*  exception.  It also looks for a non-null <code>Throwable</code>
	*  property.  If one exists, then a second <code>ExceptionDescription</code>
	*  is created.  This continues until no more nested exceptions can be found.
	*
	*  <p>The description includes a set of name/value properties 
	*  (as {@link ExceptionProperty}) object.  This list contains all
	*  non-null properties that are not, themselves, <code>Throwable</code>.
	*
	*  <p>The name is the display name (not the logical name) of the property.  The value
	*  is the <code>toString()</code> value of the property.
	*
	*  Only properties defined in subclasses of <code>Throwable</code> are included.
	*
	*  <p>A future enhancement will be to alphabetically sort the properties by name.
	*/

	public ExceptionDescription[] analyze(Throwable exception)
	{
		ExceptionDescription[] result;

		try
		{
			if (re == null)
				re = new RE(PATTERN, RE.REG_ICASE);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Could not setup regular expression.");
		}

		if (writer == null)
			writer = new CharArrayWriter();

		if (propertyDescriptions == null)
			propertyDescriptions = new ArrayList(LIST_SIZE);

		if (exceptionDescriptions == null)
			exceptionDescriptions = new ArrayList(LIST_SIZE);

		while (exception != null)
		{
			exception = buildDescription(exception);		
		}

		result = new ExceptionDescription[exceptionDescriptions.size()];
		result = (ExceptionDescription[])exceptionDescriptions.toArray(result);

		exceptionDescriptions.clear();
		propertyDescriptions.clear();

		writer.reset();

		// We never actually close() the writer which is bad ... I'm expecting that
		// the finalize() method will close them, or that they don't need to
		// close.

		return result;
	}
    
	protected Throwable buildDescription(Throwable exception)
	{
		BeanInfo info;
		Class exceptionClass;
		ExceptionProperty property;
		PropertyDescriptor[] descriptors;
		PropertyDescriptor descriptor;
		Throwable next = null;
		int i;
		String name;
		Object value;
		Class type;
		Method method;
		ExceptionProperty[] properties;
		ExceptionDescription description;
		String stringValue;
		String message;
		String[] stackTrace = null;

		propertyDescriptions.clear();

		message = exception.getMessage();
		exceptionClass = exception.getClass();

		// Get properties, ignoring those in Throwable and higher
		// (including the 'message' property).

		try
		{
			info = Introspector.getBeanInfo(exceptionClass, Throwable.class);
		}
		catch (IntrospectionException e)
		{
			return null;
		}

		descriptors = info.getPropertyDescriptors();

		for (i = 0; i < descriptors.length; i++)
		{
			descriptor = descriptors[i];

			name = descriptor.getName();

			type = descriptor.getPropertyType();

			method = descriptor.getReadMethod();
			if (method == null)
				continue;

			try
			{
				value = method.invoke(exception, null);
			}
			catch (Exception e)
			{
				continue;
			}

			if (value == null)
				continue;

			// Some annoying exceptions duplicate the message property
			// (I'm talking to YOU SAXParseException), so just edit that out.

			if (message != null &&
				message.equals(value))
				continue;

			// Skip Throwables ... but the first non-null
			// found is the next exception.  We kind of count
			// on there being no more than one Throwable
			// property per Exception.

			if (value instanceof Throwable)
			{
				if (next == null)
					next = (Throwable)value;

				continue;
			}

			stringValue = value.toString().trim();

			if (stringValue.length() == 0)
				continue;

				property = new ExceptionProperty(
				descriptor.getDisplayName(), value.toString());

			propertyDescriptions.add(property);			
		}

		// Would be nice to sort the properties here.

		properties = new ExceptionProperty[propertyDescriptions.size()];

		// If exhaustive, or in the deepest exception (where there's no next)
		// the extract the stack trace.
		
		if (next == null || exhaustive)
			stackTrace = getStackTrace(exception);

		description = new ExceptionDescription(
			exceptionClass.getName(), 
			message,
			(ExceptionProperty[])propertyDescriptions.toArray(properties), 
			stackTrace);

		exceptionDescriptions.add(description);

		return next;
	}
    
	/**
	*  Gets the stack trace for the exception, and converts it into an array of strings.
	*
	*  <p>This involves parsing the 
	*   string generated indirectly from
	*  <code>Throwable.printStackTrace(PrintWriter)</code>.
	*
	*  <p>Different JVMs format the exception in different ways.  The regular expressions
	*  used here seem to work well for the Sun JVM (1.2.2) and the IBM JVM (1.1.6).
	*
	*  <p>A possible expansion would be more flexibility in defining the pattern
	*  used.  Hopefully all 'mainstream' JVMs are close enough for this to continue
	* working.
	*
	*/

	protected String[] getStackTrace(Throwable exception)
	{
		String[] result;
		char[] traceString;
		PrintWriter printWriter;
		REMatch[] matches;
		int i;

		writer.reset();

		printWriter = new PrintWriter(writer);

		exception.printStackTrace(printWriter);

		printWriter.close();

		traceString = writer.toCharArray();

		matches = re.getAllMatches(traceString);

		result = new String[matches.length];
		for (i = 0; i < matches.length; i++)
		{
			// This first substring is the optional word 'at' (Sun's JVMs
			// emit this).  The second match is the intesting part.

			result[i] = matches[i].toString(2);	
		}

		return result;
	}

    /**
     *  Produces a text based exception report to the provided stream.
     *
     */

    public void reportException(Throwable exception, PrintStream stream)
    {
        int i;
        int j;
        ExceptionDescription[] descriptions;
        ExceptionProperty[] properties;
        String[] stackTrace;
        String message;

	    descriptions = analyze(exception);

	    for (i = 0; i < descriptions.length; i++)
	    {
		    message = descriptions[i].getMessage();

		    if (message == null)
			    stream.println(descriptions[i].getExceptionClassName());
		    else
			    stream.println(descriptions[i].getExceptionClassName() + ": " +
				    descriptions[i].getMessage());

		    properties = descriptions[i].getProperties();

		    for (j = 0; j < properties.length; j++)
			    stream.println("   " + properties[j].getName() + ": " +
				    properties[j].getValue());

		    // Just show the stack trace on the deepest exception.

		    if (i + 1 == descriptions.length)
		    {
			    stackTrace = descriptions[i].getStackTrace();

			    for (j = 0; j < stackTrace.length; j++)
				    stream.println(stackTrace[j]);
		    }
		    else
			    stream.println();
	    }
	}

}

