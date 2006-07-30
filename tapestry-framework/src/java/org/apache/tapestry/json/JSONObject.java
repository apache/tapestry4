package org.apache.tapestry.json;

/*
 Copyright (c) 2002 JSON.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 The Software shall be used for Good, not Evil.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A JSONObject is an unordered collection of name/value pairs. Its external
 * form is a string wrapped in curly braces with colons between the names and
 * values, and commas between the values and names. The internal form is an
 * object having get() and opt() methods for accessing the values by name, and
 * put() methods for adding or replacing values by name. The values can be any
 * of these types: Boolean, JSONArray, JSONObject, Number, String, or the
 * JSONObject.NULL object.
 * <p>
 * The constructor can convert an external form string into an internal form
 * Java object. The toString() method creates an external form string.
 * <p>
 * A get() method returns a value if one can be found, and throws an exception
 * if one cannot be found. An opt() method returns a default value instead of
 * throwing an exception, and so is useful for obtaining optional values.
 * <p>
 * The generic get() and opt() methods return an object, which you can cast or
 * query for type. There are also typed get() and opt() methods that do type
 * checking and type coersion for you.
 * <p>
 * The texts produced by the toString() methods are very strict. The
 * constructors are more forgiving in the texts they will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing brace.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * <li>Keys can be followed by <code>=</code> or <code>=></code> as well as
 * by <code>:</code></li>
 * <li>Values can be followed by <code>;</code> as well as by <code>,</code></li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 * <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Line comments can begin with <code>#</code></li>
 * </ul>
 * 
 * @author JSON.org
 * @version 1
 */
public class JSONObject
{

    /**
     * It is sometimes more convenient and less ambiguous to have a NULL object
     * than to use Java's null value. JSONObject.NULL.equals(null) returns true.
     * JSONObject.NULL.toString() returns "null".
     */
    public static final Object NULL = new Null();

    /**
     * JSONObject.NULL is equivalent to the value that JavaScript calls null,
     * whilst Java's null is equivalent to the value that JavaScript calls
     * undefined.
     */
    private static final class Null
    {

        /**
         * There is only intended to be a single instance of the NULL object, so
         * the clone method returns itself.
         * CHECKSTYLE:OFF
         * @return NULL.
         */
        protected Object clone()
        {
            return this;
        }

        /**
         * A Null object is equal to the null value and to itself.
         * 
         * @param object
         *            An object to test for nullness.
         * @return true if the object parameter is the JSONObject.NULL object or
         *         null.
         */
        public boolean equals(Object object)
        {
            return object == null || object == this;
        }

        /**
         * Get the "null" string value.
         * 
         * @return The string "null".
         */
        public String toString()
        {
            return "null";
        }
    }

    /**
     * The hash map where the JSONObject's properties are kept.
     */
    private HashMap myHashMap;

    /**
     * Construct an empty JSONObject.
     */
    public JSONObject()
    {
        this.myHashMap = new LinkedHashMap();
    }

    /**
     * Construct a JSONObject from a subset of another JSONObject. An array of
     * strings is used to identify the keys that should be copied. Missing keys
     * are ignored.
     * 
     * @param jo
     *            A JSONObject.
     * @param sa
     *            An array of strings.
     */
    public JSONObject(JSONObject jo, String[] sa)
    {
        this();
        for(int i = 0; i < sa.length; i += 1)
        {
            putOpt(sa[i], jo.opt(sa[i]));
        }
    }

    /**
     * Construct a JSONObject from a JSONTokener.
     * 
     * @param x
     *            A JSONTokener object containing the source string.
     * @throws ParseException
     *             if there is a syntax error in the source string.
     */
    public JSONObject(JSONTokener x)
        throws ParseException
    {
        this();
        char c;
        String key;

        if (x.nextClean() != '{') { throw x
                .syntaxError("A JSONObject must begin with '{'"); }
        while(true)
        {
            c = x.nextClean();
            switch(c)
            {
            case 0:
                throw x.syntaxError("A JSONObject must end with '}'");
            case '}':
                return;
            default:
                x.back();
                key = x.nextValue().toString();
            }

            /*
             * The key is followed by ':'. We will also tolerate '=' or '=>'.
             */

            c = x.nextClean();
            if (c == '=')
            {
                if (x.next() != '>')
                {
                    x.back();
                }
            }
            else if (c != ':') { throw x
                    .syntaxError("Expected a ':' after a key"); }
            this.myHashMap.put(key, x.nextValue());

            /*
             * Pairs are separated by ','. We will also tolerate ';'.
             */

            switch(x.nextClean())
            {
            case ';':
            case ',':
                if (x.nextClean() == '}') { return; }
                x.back();
                break;
            case '}':
                return;
            default:
                throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    /**
     * Construct a JSONObject from a Map.
     * 
     * @param map
     *            A map object that can be used to initialize the contents of
     *            the JSONObject.
     */
    public JSONObject(Map map)
    {
        this.myHashMap = new HashMap(map);
    }

    /**
     * Construct a JSONObject from a string. This is the most commonly used
     * JSONObject constructor.
     * 
     * @param string
     *            A string beginning with <code>{</code>&nbsp;<small>(left
     *            brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *            brace)</small>.
     * @exception ParseException
     *                The string must be properly formatted.
     */
    public JSONObject(String string)
        throws ParseException
    {
        this(new JSONTokener(string));
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject accumulate(String key, Object value)
    {
        JSONArray a;
        Object o = opt(key);
        if (o == null)
        {
            put(key, value);
        }
        else if (o instanceof JSONArray)
        {
            a = (JSONArray) o;
            a.put(value);
        }
        else
        {
            a = new JSONArray();
            a.put(o);
            a.put(value);
            put(key, a);
        }
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public Object get(String key)
    {
        Object o = opt(key);
        if (o == null) { throw new NoSuchElementException("JSONObject["
                + quote(key) + "] not found."); }
        return o;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean getBoolean(String key)
    {
        Object o = get(key);
        if (o.equals(Boolean.FALSE)
                || (o instanceof String && ((String) o)
                        .equalsIgnoreCase("false")))
        {
            return false;
        }
        else if (o.equals(Boolean.TRUE)
                || (o instanceof String && ((String) o)
                        .equalsIgnoreCase("true"))) { return true; }
        throw new ClassCastException("JSONObject[" + quote(key)
                + "] is not a Boolean.");
    }

    /** 
     * {@inheritDoc}
     */
    public double getDouble(String key)
    {
        Object o = get(key);
        if (o instanceof Number) { return ((Number) o).doubleValue(); }
        if (o instanceof String) { return new Double((String) o).doubleValue(); }
        throw new NumberFormatException("JSONObject[" + quote(key)
                + "] is not a number.");
    }

    /**
     * Get the Map the holds that contents of the JSONObject.
     * 
     * @return The getHashMap.
     */
    Map getMap()
    {
        return this.myHashMap;
    }

    /** 
     * {@inheritDoc}
     */
    public int getInt(String key)
    {
        Object o = get(key);
        return o instanceof Number ? ((Number) o).intValue()
                : (int) getDouble(key);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray getJSONArray(String key)
    {
        Object o = get(key);
        if (o instanceof JSONArray) { return (JSONArray) o; }
        throw new NoSuchElementException("JSONObject[" + quote(key)
                + "] is not a JSONArray.");
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject getJSONObject(String key)
    {
        Object o = get(key);
        if (o instanceof JSONObject) { return (JSONObject) o; }
        throw new NoSuchElementException("JSONObject[" + quote(key)
                + "] is not a JSONObject.");
    }

    /** 
     * {@inheritDoc}
     */
    public String getString(String key)
    {
        return get(key).toString();
    }

    /** 
     * {@inheritDoc}
     */
    public boolean has(String key)
    {
        return this.myHashMap.containsKey(key);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isNull(String key)
    {
        return JSONObject.NULL.equals(opt(key));
    }

    /** 
     * {@inheritDoc}
     */
    public Iterator keys()
    {
        return this.myHashMap.keySet().iterator();
    }

    /** 
     * {@inheritDoc}
     */
    public int length()
    {
        return this.myHashMap.size();
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray names()
    {
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while(keys.hasNext())
        {
            ja.put(keys.next());
        }
        return ja.length() == 0 ? null : ja;
    }

    /**
     * Produce a string from a number.
     * 
     * @param n
     *            A Number
     * @return A String.
     * @exception ArithmeticException
     *                JSON can only serialize finite numbers.
     */
    public static String numberToString(Number n)
    {
        if ((n instanceof Float && (((Float) n).isInfinite() || ((Float) n)
                .isNaN()))
                || (n instanceof Double && (((Double) n).isInfinite() || ((Double) n)
                        .isNaN()))) { throw new ArithmeticException(
                "JSON can only serialize finite numbers."); }

        // Shave off trailing zeros and decimal point, if possible.
        
        String s = n.toString();
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0)
        {
            while(s.endsWith("0"))
            {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith("."))
            {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /** 
     * {@inheritDoc}
     */
    public Object opt(String key)
    {
        if (key == null) { throw new NullPointerException("Null key"); }
        return this.myHashMap.get(key);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean optBoolean(String key)
    {
        return optBoolean(key, false);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean optBoolean(String key, boolean defaultValue)
    {
        Object o = opt(key);
        if (o != null)
        {
            if (o.equals(Boolean.FALSE)
                    || (o instanceof String && ((String) o)
                            .equalsIgnoreCase("false")))
            {
                return false;
            }
            else if (o.equals(Boolean.TRUE)
                    || (o instanceof String && ((String) o)
                            .equalsIgnoreCase("true"))) { return true; }
        }
        return defaultValue;
    }

    /** 
     * {@inheritDoc}
     */
    public double optDouble(String key)
    {
        return optDouble(key, Double.NaN);
    }

    /** 
     * {@inheritDoc}
     */
    public double optDouble(String key, double defaultValue)
    {
        Object o = opt(key);
        if (o != null)
        {
            if (o instanceof Number) { return ((Number) o).doubleValue(); }
            try
            {
                return new Double((String) o).doubleValue();
            }
            catch (Exception e)
            {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /** 
     * {@inheritDoc}
     */
    public int optInt(String key)
    {
        return optInt(key, 0);
    }

    /** 
     * {@inheritDoc}
     */
    public int optInt(String key, int defaultValue)
    {
        Object o = opt(key);
        if (o != null)
        {
            if (o instanceof Number) { return ((Number) o).intValue(); }
            try
            {
                return Integer.parseInt((String) o);
            }
            catch (Exception e)
            {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray optJSONArray(String key)
    {
        Object o = opt(key);
        return o instanceof JSONArray ? (JSONArray) o : null;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject optJSONObject(String key)
    {
        Object o = opt(key);
        return o instanceof JSONObject ? (JSONObject) o : null;
    }

    /** 
     * {@inheritDoc}
     */
    public String optString(String key)
    {
        return optString(key, "");
    }

    /** 
     * {@inheritDoc}
     */
    public String optString(String key, String defaultValue)
    {
        Object o = opt(key);
        return o != null ? o.toString() : defaultValue;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject put(String key, boolean value)
    {
        put(key, Boolean.valueOf(value));
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject put(String key, double value)
    {
        put(key, new Double(value));
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject put(String key, int value)
    {
        put(key, new Integer(value));
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject put(String key, Object value)
    {
        if (key == null) { throw new NullPointerException("Null key."); }
        if (value != null)
        {
            this.myHashMap.put(key, value);
        }
        else
        {
            remove(key);
        }
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject putOpt(String key, Object value)
    {
        if (value != null)
        {
            put(key, value);
        }
        return this;
    }

    /**
     * @see {{@link #quote(String)}.
     * @param value
     * @return
     */
    public static String quote(char value)
    {
        return quote(new String(new char[]{value}));
    }
    
    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places.
     * 
     * @param string
     *            A String
     * @return A String correctly formatted for insertion in a JSON message.
     */
    public static String quote(String string)
    {
        if (string == null || string.length() == 0) { return "\"\""; }

        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String t;

        sb.append('"');
        for(i = 0; i < len; i += 1)
        {
            b = c;
            c = string.charAt(i);
            switch(c)
            {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                if (b == '<')
                {
                    sb.append('\\');
                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (c < ' ')
                {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                }
                else
                {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /** 
     * {@inheritDoc}
     */
    public Object remove(String key)
    {
        return this.myHashMap.remove(key);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray toJSONArray(JSONArray names)
    {
        if (names == null || names.length() == 0) { return null; }
        JSONArray ja = new JSONArray();
        for(int i = 0; i < names.length(); i += 1)
        {
            ja.put(this.opt(names.getString(i)));
        }
        return ja;
    }

    /** 
     * {@inheritDoc}
     */
    public String toString()
    {
        Iterator keys = keys();
        StringBuffer sb = new StringBuffer("{");

        while(keys.hasNext())
        {
            if (sb.length() > 1)
            {
                sb.append(',');
            }
            Object o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(':');
            sb.append(valueToString(this.myHashMap.get(o)));
        }
        sb.append('}');
        return sb.toString();
    }

    /** 
     * {@inheritDoc}
     */
    public String toString(int indentFactor)
    {
        return toString(indentFactor, 0);
    }

    /**
     * Make a prettyprinted JSON string of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @param indent
     *            The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    String toString(int indentFactor, int indent)
    {
        int i;
        int n = length();
        if (n == 0) { return "{}"; }
        Iterator keys = keys();
        StringBuffer sb = new StringBuffer("{");
        int newindent = indent + indentFactor;
        Object o;
        if (n == 1)
        {
            o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(": ");
            sb
                    .append(valueToString(this.myHashMap.get(o), indentFactor,
                            indent));
        }
        else
        {
            while(keys.hasNext())
            {
                o = keys.next();
                if (sb.length() > 1)
                {
                    sb.append(",\n");
                }
                else
                {
                    sb.append('\n');
                }
                for(i = 0; i < newindent; i += 1)
                {
                    sb.append(' ');
                }
                sb.append(quote(o.toString()));
                sb.append(": ");
                sb.append(valueToString(this.myHashMap.get(o), indentFactor,
                        newindent));
            }
            if (sb.length() > 1)
            {
                sb.append('\n');
                for(i = 0; i < indent; i += 1)
                {
                    sb.append(' ');
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * Make JSON string of an object value.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param value
     *            The value to be serialized.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    static String valueToString(Object value)
    {
        if (value == null || value.equals(null)) { return "null"; }
        if (value instanceof Number) { return numberToString((Number) value); }
        if (value instanceof Boolean || value instanceof JSONObject
                || value instanceof JSONArray
                || value instanceof JSONLiteral) { return value.toString(); }
        return quote(value.toString());
    }

    /**
     * Make a prettyprinted JSON string of an object value.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param value
     *            The value to be serialized.
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @param indent
     *            The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    static String valueToString(Object value, int indentFactor, int indent)
    {
        if (value == null || value.equals(null)) { return "null"; }
        if (value instanceof Number) { return numberToString((Number) value); }
        if (value instanceof Boolean) { return value.toString(); }
        if (value instanceof JSONObject) { return (((JSONObject) value)
                .toString(indentFactor, indent)); }
        if (value instanceof JSONArray) { return (((JSONArray) value).toString(
                indentFactor, indent)); }
        if (JSONLiteral.class.isAssignableFrom(value.getClass()))
            return value.toString();
        return quote(value.toString());
    }
}
