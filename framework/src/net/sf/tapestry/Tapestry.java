package net.sf.tapestry;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.StringSplitter;

/**
 *  A placeholder for a number of (static) methods that don't belong elsewhere.
 *
 *  @since 1.0.1
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public final class Tapestry
{
    /**
     *  Prevent instantiation.
     *
     **/

    private Tapestry()
    {
    }

    /**
     *  The version of the framework; this is updated for major releases.
     *
     **/

    public static final String VERSION = readVersion();

    /**
     *  Contains strings loaded from TapestryStrings.properties.
     * 
     *  @since 1.0.8
     * 
     **/

    private static ResourceBundle _strings;

    /**
     *  A {@link Map} that links Locale names (as in {@link Locale#toString()} to
     *  {@link Locale} instances.  This prevents needless duplication
     *  of Locales.
     *
     **/

    private static final Map _localeMap = new HashMap();

    static {
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++)
        {
            _localeMap.put(locales[i].toString(), locales[i]);
        }
    }

    /**
     *  A {@link net.sf.tapestry.util.AdaptorRegistry} used to coerce arbitrary objects
     *  to boolean values.
     *
     *  @see #evaluateBoolean(Object)
     **/

    private static final AdaptorRegistry _booleanAdaptors = new AdaptorRegistry();

    private static abstract class BoolAdaptor
    {
        /**
         *  Implemented by subclasses to coerce an object to a boolean.
         *
         **/

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
        _booleanAdaptors.register(Boolean.class, new BooleanAdaptor());
        _booleanAdaptors.register(Number.class, new NumberAdaptor());
        _booleanAdaptors.register(Collection.class, new CollectionAdaptor());
        _booleanAdaptors.register(String.class, new StringAdaptor());

        // Register a default, catch-all adaptor.

        _booleanAdaptors.register(Object.class, new BoolAdaptor()
        {
            public boolean coerce(Object value)
            {
                return true;
            }
        });
    }

    /**
     *  {@link AdaptorRegistry} used to extract an {@link Iterator} from
     *  an arbitrary object.
     *
     **/

    private static AdaptorRegistry _iteratorAdaptors = new AdaptorRegistry();

    private abstract static class IteratorAdaptor
    {
        /**
         *  Coeerces the object into an {@link Iterator}.
         *
         **/

        abstract public Iterator coerce(Object value);
    }

    static {
        _iteratorAdaptors.register(Iterator.class, new IteratorAdaptor()
        {
            public Iterator coerce(Object value)
            {
                return (Iterator) value;
            }
        });

        _iteratorAdaptors.register(Collection.class, new IteratorAdaptor()
        {
            public Iterator coerce(Object value)
            {
                Collection c = (Collection) value;

                if (c.size() == 0)
                    return null;

                return c.iterator();
            }
        });

        _iteratorAdaptors.register(Object.class, new IteratorAdaptor()
        {
            public Iterator coerce(Object value)
            {
                return Collections.singleton(value).iterator();
            }
        });
    }

    /**
     *  Returns true if the value is null or empty (is the empty string,
     *  or contains only whitespace).
     *
     **/

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
     **/

    public static void copyInformalBindings(IComponent source, IComponent destination)
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
     *		<td>True if contains any elements (non-zero size), false otherwise.</td>
     *		</tr>
     *	<tr>
     *		<td>{@link String}</td>
     *		<td>True if contains any non-whitespace characters, false otherwise.</td>
     *		</tr>
     *	<tr>
     *		<td>Any array type</td>
     *		<td>True if contains any elements (non-zero length), false otherwise.</td>
     *	<tr>
     *</table>
     *
     * <p>Any other non-null object evaluates to true.
     *
     **/

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

        BoolAdaptor adaptor = (BoolAdaptor) _booleanAdaptors.getAdaptor(valueClass);

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
     * <tr><td>Any other</td> <td>{@link Iterator} for singleton collection returned</td> </tr>
     * <tr><td>null</td> <td>null returned</td> </tr>
     * </table>
     *
     **/

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

        IteratorAdaptor adaptor = (IteratorAdaptor) _iteratorAdaptors.getAdaptor(valueClass);

        return adaptor.coerce(value);
    }

    /**
     *  Gets the {@link Locale} for the given string, which is the result
     *  of {@link Locale#toString()}.  If no such locale is already registered,
     *  a new instance is created, registered and returned.
     *
     *
     **/

    public static Locale getLocale(String s)
    {
        Locale result = null;

        synchronized (_localeMap)
        {
            result = (Locale) _localeMap.get(s);
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

                    throw new IllegalArgumentException("Unable to convert '" + s + "' to a Locale.");
            }

            synchronized (_localeMap)
            {
                _localeMap.put(s, result);
            }

        }

        return result;

    }

    /** 
     *  Closes the stream (if not null), ignoring any {@link IOException} thrown.
     *
     *  @since 1.0.2
     *
     **/

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
        if (_strings == null)
            _strings = ResourceBundle.getBundle("net.sf.tapestry.TapestryStrings");

        String pattern = _strings.getString(key);

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

    private static final String UNKNOWN_VERSION = "Unknown";

    /**
     *  Invoked when the class is initialized to read the current version file.
     * 
     **/

    private static final String readVersion()
    {
        Properties props = new Properties();

        try
        {
            InputStream in = Tapestry.class.getResourceAsStream("Version.properties");

            if (in == null)
                return UNKNOWN_VERSION;

            props.load(in);

            in.close();

            return props.getProperty("framework.version", UNKNOWN_VERSION);
        }
        catch (IOException ex)
        {
            return UNKNOWN_VERSION;
        }

    }

    /**
     *  Returns the size of a collection, or zero if the collection is null.
     * 
     *  @since 2.2
     * 
     **/

    public static int size(Collection c)
    {
        if (c == null)
            return 0;

        return c.size();
    }

    /**
     *  Returns the length of the array, or 0 if the array is null.
     * 
     *  @since 2.2
     * 
     **/

    public static int size(Object[] array)
    {
        if (array == null)
            return 0;

        return array.length;
    }
    
    /**
     *  Returns true if the Map is null or empty.
     * 
     *  @since 2.4
     * 
     **/
    
    public static boolean isEmpty(Map map)
    {
        return map == null || map.isEmpty();
    }
    
    /**
     *  Converts a {@link Map} to an even-sized array of key/value
     *  pairs.  This may be useful when using a Map as service parameters
     *  (with {@link net.sf.tapestry.link.DirectLink}.  Assuming the keys
     *  and values are simple objects (String, Boolean, Integer, etc.), then
     *  the representation as an array will encode more efficiently
     *  (via {@link net.sf.tapestry.util.io.DataSqueezer} than
     *  serializing the Map and its contents.
     * 
     *  @return the array of keys and values, or null if the input
     *  Map is null or empty
     * 
     *  @since 2.2
     **/
    
    public static Object[] convertMapToArray(Map map)
    {
        if (isEmpty(map))
            return null;
            
        Set entries = map.entrySet();
        
        Object[] result = new Object[2 * entries.size()];
        int x = 0;
        
        Iterator i = entries.iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry)i.next();
            
            result[x++] = entry.getKey();
            result[x++] = entry.getValue();
        }
        
        return result;
    }
    
    /**
     *  Converts an even-sized array of objects back 
     *  into a {@link Map}.
     * 
     *  @see #convertMapToArray(Map)
     *  @return a Map, or null if the array is null or empty
     *  @since 2.2
     * 
     **/
    
    public static Map convertArrayToMap(Object[] array)
    {
        if (array == null || array.length == 0)
            return null;
            
         if (array.length % 2 != 0)
         throw new IllegalArgumentException(
         getString("Tapestry.even-sized-array"));
         
         Map result = new HashMap();
         
         int x = 0;
         while (x < array.length)
         {
            Object key = array[x++];
            Object value = array[x++];
            
            result.put(key, value);
         }
         
         return result;    
    }
}