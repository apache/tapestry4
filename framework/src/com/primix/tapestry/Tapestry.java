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

package com.primix.tapestry;

import com.primix.tapestry.spec.*;
import com.primix.tapestry.util.*;
import java.text.MessageFormat;
import java.util.*;
import java.io.*;

/**
 *  A placeholder for a number of (static) methods that don't belong elsewhere.
 *
 *  @since 1.0.1
 *  @version $Id$
 *  @author Howard Ship
 */

public final class Tapestry
{
	/**
	 *  Prevent instantiation.
	 *
	 */

	private Tapestry()
	{
	}

	/**
	 *  The version of the framework; this is updated for major releases.
	 *
	 */

	public static final String VERSION = "1.0.8";


	/**
	 *  Contains strings loaded from TapestryStrings.properties.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	private static ResourceBundle strings;

	/**
	 *  A {@link Map} that links Locale names (as in {@link Locale#toString()} to
	 *  {@link Locale} instances.  This prevents needless duplication
	 *  of Locales.
	 *
	 */

	private static final Map localeMap = new HashMap();

	static {
		Locale[] locales = Locale.getAvailableLocales();
		for (int i = 0; i < locales.length; i++)
		{
			localeMap.put(locales[i].toString(), locales[i]);
		}
	}

	/**
	 *  A {@link Decorator} used to coerce arbitrary objects
	 *  to boolean values.
	 *
	 *  @see #evaluateBoolean(Object)
	 */

	private static final Decorator booleanDecorator = new Decorator();

	private static abstract class BoolAdaptor
	{
		/**
		 *  Implemented by subclasses to coerce an object to a boolean.
		 *
		 */

		public abstract boolean coerce(Object value);
	}

	private static class BooleanAdaptor extends BoolAdaptor
	{
		public boolean coerce(Object value)
		{
			Boolean b = (Boolean) value;

			return b.booleanValue();
		}
	}

	private static class NumberAdaptor extends BoolAdaptor
	{
		public boolean coerce(Object value)
		{
			Number n = (Number) value;

			return n.intValue() > 0;
		}
	}

	private static class CollectionAdaptor extends BoolAdaptor
	{
		public boolean coerce(Object value)
		{
			Collection c = (Collection) value;

			return c.size() > 0;
		}
	}

	private static class StringAdaptor extends BoolAdaptor
	{
		public boolean coerce(Object value)
		{
			String s = (String) value;

			if (s.length() == 0)
				return false;

			char[] data = s.toCharArray();

			try
			{
				for (int i = 0;; i++)
				{
					char ch = data[i];
					if (!Character.isWhitespace(ch))
						return true;
				}
			}
			catch (IndexOutOfBoundsException ex)
			{
				return false;
			}
		}
	}

	static {
		booleanDecorator.register(Boolean.class, new BooleanAdaptor());
		booleanDecorator.register(Number.class, new NumberAdaptor());
		booleanDecorator.register(Collection.class, new CollectionAdaptor());
		booleanDecorator.register(String.class, new StringAdaptor());

		// Register a default, catch-all adaptor.

		booleanDecorator.register(Object.class, new BoolAdaptor()
		
		{
			public boolean coerce(Object value)
			{
				return true;
			}
		});
	}

	/**
	 *  {@link Decorator} used to extract an {@link Iterator} from
	 *  an arbitrary object.
	 *
	 */

	private static Decorator iteratorDecorator = new Decorator();

	private abstract static class IteratorAdaptor
	{
		/**
		 *  Coeerces the object into an {@link Iterator}.
		 *
		 */

		abstract public Iterator coerce(Object value);
	}

	static {
		iteratorDecorator.register(Iterator.class, new IteratorAdaptor()
		
		{
			public Iterator coerce(Object value)
			{
				return (Iterator) value;
			}
		});

		iteratorDecorator.register(Collection.class, new IteratorAdaptor()
		
		{
			public Iterator coerce(Object value)
			{
				Collection c = (Collection) value;

				if (c.size() == 0)
					return null;

				return c.iterator();
			}
		});

		iteratorDecorator.register(Object.class, new IteratorAdaptor()
		
		{
			public Iterator coerce(Object value)
			{
				return null;
			}
		});
	}

	/**
	 *  Returns true if the value is null or empty (is the empty string,
	 *  or contains only whitespace).
	 *
	 */

	public static boolean isNull(String value)
	{
		if (value == null)
			return true;

		if (value.length() == 0)
			return true;

		return value.trim().length() == 0;
	}

	/**
	 *  Copys all informal {@link IBinding bindings} from a source component
	 *  to the destination component.  Informal bindings are bindings for
	 *  informal parameters.  This will overwrite parameters (formal or
	 *  informal) in the
	 *  destination component if there is a naming conflict.
	 *
	 *
	 */

	public static void copyInformalBindings(
		IComponent source,
		IComponent destination)
	{
		Collection names = source.getBindingNames();

		if (names == null)
			return;

		ComponentSpecification specification = source.getSpecification();
		Iterator i = names.iterator();

		while (i.hasNext())
		{
			String name = (String) i.next();

			// If not a formal parameter, then copy it over.

			if (specification.getParameter(name) == null)
			{
				IBinding binding = source.getBinding(name);

				destination.setBinding(name, binding);
			}
		}
	}

	/**
	 *  Evaluates an object to determine its boolean value.
	 *
	 *  <table border=1>
	 *	<tr> <th>Class</th> <th>Test</th> </tr>
	 *  <tr>
	 *		<td>{@link Boolean}</td>
	 *		<td>Self explanatory.</td>
	 *	</tr>
	 *	<tr> <td>{@link Number}</td>
	 *		<td>True if non-zero, false otherwise.</td>
	 *		</tr>
	 *	<tr>
	 *		<td>{@link Collection}</td>
	 *		<td>True if contains any elements, false otherwise.</td>
	 *		</tr>
	 *	<tr>
	 *		<td>{@link String}</td>
	 *		<td>True if contains any non-whitespace characters, false otherwise.</td>
	 *		</tr>
	 *	<tr>
	 *		<td>Any array type</td>
	 *		<td>True if contains any elements, false otherwise.</td>
	 *	<tr>
	 *</table>
	 *
	 * <p>Any other non-null object evaluates to true.
	 *
	 */

	public static boolean evaluateBoolean(Object value)
	{
		if (value == null)
			return false;

		Class valueClass = value.getClass();
		if (valueClass.isArray())
		{
			Object[] array = (Object[]) value;

			return array.length > 0;
		}

		BoolAdaptor adaptor = (BoolAdaptor) booleanDecorator.getAdaptor(valueClass);

		return adaptor.coerce(value);
	}

	/**
	 *  Converts an Object into an {@link Iterator}, following some basic rules.
	 *
	 *  <table border=1>
	 * 	<tr><th>Input Class</th> <th>Result</th> </tr>
	 * <tr><td>Object array</td> <td>Converted to a {@link List} and iterator returned.
	 * null returned if the array is empty.</td>
	 * </tr>
	 * <tr><td>{@link Iterator}</td> <td>Returned as-is.</td>
	 * <tr><td>{@link Collection}</td> <td>Iterator returned, or null
	 *  if the Collection is empty</td> </tr>
	 * <tr><td>Any other</td> <td>null returned</td> </tr>
	 * <tr><td>null</td> <td>null returned</td> </tr>
	 * </table>
	 *
	 */

	public static Iterator coerceToIterator(Object value)
	{
		if (value == null)
			return null;

		Class valueClass = value.getClass();
		if (valueClass.isArray())
		{
			Object[] array = (Object[]) value;

			if (array.length == 0)
				return null;

			List l = Arrays.asList(array);

			return l.iterator();
		}

		IteratorAdaptor adaptor =
			(IteratorAdaptor) iteratorDecorator.getAdaptor(valueClass);

		return adaptor.coerce(value);
	}

	/**
	 *  Gets the {@link Locale} for the given string, which is the result
	 *  of {@link Locale#toString()}.  If no such locale is already registered,
	 *  a new instance is created, registered and returned.
	 *
	 *
	 */

	public static Locale getLocale(String s)
	{
		Locale result = null;

		synchronized (localeMap)
		{
			result = (Locale) localeMap.get(s);
		}

		if (result == null)
		{
			StringSplitter splitter = new StringSplitter('_');
			String[] terms = splitter.splitToArray(s);

			switch (terms.length)
			{
				case 1 :

					result = new Locale(terms[0], "");
					break;

				case 2 :

					result = new Locale(terms[0], terms[1]);
					break;

				case 3 :

					result = new Locale(terms[0], terms[1], terms[2]);
					break;

				default :

					throw new IllegalArgumentException(
						"Unable to convert '" + s + "' to a Locale.");
			}

			synchronized (localeMap)
			{
				localeMap.put(s, result);
			}

		}

		return result;

	}

	/** 
	 *  Closes the stream (if not null), ignoring any {@link IOException} thrown.
	 *
	 *  @since 1.0.2
	 *
	 */

	public static void close(InputStream stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			}
			catch (IOException ex)
			{
				// Ignore.
			}
		}
	}
		
	/**
	 *  Gets a string from the TapestryStrings resource bundle.  
	 *  The string in the bundle
	 *  is treated as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public static String getString(String key, Object[] args)
	{
		if (strings == null)
			strings = ResourceBundle.getBundle("com.primix.tapestry.TapestryStrings");
		
		String pattern = strings.getString(key);
		
		if (args == null)
			return pattern;
		
		return MessageFormat.format(pattern, args);
	}
	

	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 *  @since 1.0.8
	 **/
	
	public static String getString(String key)
	{
		return getString(key, null);
	}
	
	
	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 *  @since 1.0.8
	 **/
	
	public static String getString(String key, Object arg)
	{
		return getString(key, new Object[] { arg });
	}
	
	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 *  @since 1.0.8
	 * 
	 **/

	public static String getString(String key, Object arg1, Object arg2)
	{
		return getString(key, new Object[] { arg1, arg2 });
	}

	/**
	 *  Convienience method for invoking {@link #getString(String, Object[])}.
	 * 
	 *  @since 1.0.8
	 * 
	 **/

	public static String getString(String key, Object arg1, Object arg2, Object arg3)
	{
		return getString(key, new Object[] { arg1, arg2, arg3 });
	}

}