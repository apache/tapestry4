/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
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

import javax.servlet.ServletContext;

import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.util.AdaptorRegistry;
import org.apache.tapestry.util.StringSplitter;

/**
 *  A placeholder for a number of (static) methods that don't belong elsewhere, as well
 *  as a global location for static constants.
 *
 *  @since 1.0.1
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public final class Tapestry
{
    /**
     *  Name of a request attribute used with the
     *  {@link #TAGSUPPORT_SERVICE} service.  The attribute
     *  defines the underlying service to for which a URL will be generated.
     * 
     *  @since 2.4
     * 
     **/

    public final static String TAG_SUPPORT_SERVICE_ATTRIBUTE =
        "org.apache.tapestry.tagsupport.service";

    /**
     *  Name of a request attribute used with the
     *  {@link #TAGSUPPORT_SERVICE} service.  The attribute
     *  defines an array of objects to be converted into
     *  service parameters (i.e., for use with the
     *  {@link #EXTERNAL_SERVICE}).
     * 
     *  @since 2.4
     * 
     **/

    public final static String TAG_SUPPORT_PARAMETERS_ATTRIBUTE =
        "org.apache.tapestry.tagsupport.parameters";

    /**
     *  Service used to support rendering of JSP tags.  tagsupport is provided
     *  with a service and service parameters via request attributes
     *  and creates a URI from the result, which is output to the response.
     * 
     *  @since 2.4
     * 
     **/

    public static final String TAGSUPPORT_SERVICE = "tagsupport";

    /**
     *  The name ("action") of a service that allows behavior to be associated with
     *  an {@link IAction} component, such as {@link ActionLink} or {@link Form}.
     *  
     *  <p>This service is used with actions that are tied to the
     *  dynamic state of the page, and which require a rewind of the page.
     *
     **/

    public final static String ACTION_SERVICE = "action";

    /**
     *  The name ("direct") of a service that allows stateless behavior for an {@link
     *  DirectLink} component.
     *
     *  <p>This service rolls back the state of the page but doesn't
     *  rewind the the dynamic state of the page the was the action
     *  service does, which is more efficient but less powerful.
     *
     *  <p>An array of String parameters may be included with the
     *  service URL; these will be made available to the {@link DirectLink}
     *  component's listener.
     *
     **/

    public final static String DIRECT_SERVICE = "direct";

    /**
     *  The name ("external") of a service that a allows {@link IExternalPage} to be selected.
     *  Associated with a {@link ExternalLink} component.
     *
     *  <p>This service enables {@link IExternalPage}s to be accessed via a URL.
     *  External pages may be booked marked using their URL for future reference.
     *
     *  <p>An array of Object parameters may be included with the
     *  service URL; these will be passed to the 
     *  {@link IExternalPage#activateExternalPage(Object[], IRequestCycle)} method.
     *
     **/

    public final static String EXTERNAL_SERVICE = "external";

    /**
     *  The name ("page") of a service that allows a new page to be selected.
     *  Associated with a {@link PageLink} component.
     *
     *  <p>The service requires a single parameter:  the name of the target page.
     **/

    public final static String PAGE_SERVICE = "page";

    /**
     *  The name ("home") of a service that jumps to the home page.  A stand-in for
     *  when no service is provided, which is typically the entrypoint
     *  to the application.
     *
     **/

    public final static String HOME_SERVICE = "home";

    /**
     *  The name ("restart") of a service that invalidates the session and restarts
     *  the application.  Typically used just
     *  to recover from an exception.
     *
     **/

    public static final String RESTART_SERVICE = "restart";

    /**
     *  The name ("asset") of a service used to access internal assets.
     *
     **/

    public static final String ASSET_SERVICE = "asset";

    /**
     *  The name ("reset") of a service used to clear cached template
     *  and specification data and remove all pooled pages.
     *  This is only used when debugging as
     *  a quick way to clear the out cached data, to allow updated
     *  versions of specifications and templates to be loaded (without
     *  stopping and restarting the servlet container).
     *
     *  <p>This service is only available if the Java system property
     *  <code>org.apache.tapestry.enable-reset-service</code>
     *  is set to <code>true</code>.
     * 
     **/

    public static final String RESET_SERVICE = "reset";

    /**
     *  Query parameter that identfies the service for the
     *  request.
     *
     *  @since 1.0.3
     * 
     **/

    public static final String SERVICE_QUERY_PARAMETER_NAME = "service";

    /**
     *  The query parameter for application specific parameters to the
     *  service (this is used with the direct service).  Each of these
     *  values is encoded with {@link java.net.URLEncoder#encode(String)} before
     *  being added to the URL.  Multiple values are handle by repeatedly
     *  establishing key/value pairs (this is a change from behavior in 
     *  2.1 and earlier).
     *
     *  @since 1.0.3
     * 
     **/

    public static final String PARAMETERS_QUERY_PARAMETER_NAME = "sp";

    /**
     *  Property name used to get the extension used for templates.  This
     *  may be set in the page or component specification, or in the page (or
     *  component's) immediate container (library or application specification).
     *  Unlike most properties, value isn't inherited all the way up the chain.
     *  The default template extension is "html".
     * 
     *  @since 2.4
     * 
     **/

    public static final String TEMPLATE_EXTENSION_PROPERTY =
        "org.apache.tapestry.template-extension";

    /**
     *  The default extension for templates, "html".
     * 
     *  @since 2.4
     * 
     **/

    public static final String DEFAULT_TEMPLATE_EXTENSION = "html";

    /**
     *  The name of an {@link org.apache.tapestry.IRequestCycle} attribute in which the
     *  currently rendering {@link org.apache.tapestry.components.ILinkComponent}
     *  is stored.  Link components do not nest.
     *
     **/

    public static final String LINK_COMPONENT_ATTRIBUTE_NAME =
        "org.apache.tapestry.active-link-component";

    /**
     *  Suffix appended to a parameter name to form the name of a property that stores the
     *  binding for the parameter.
     * 
     *  @since 2.4
     * 
     **/

    public static final String PARAMETER_PROPERTY_NAME_SUFFIX = "Binding";

    /**
     *  Name of application extension used to resolve page and component
     *  specifications that can't be located by the normal means.  The
     *  extension must implement 
     *  {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate}.
     * 
     *  @since 2.4
     * 
     **/

    public static final String SPECIFICATION_RESOLVER_DELEGATE_EXTENSION_NAME =
        "org.apache.tapestry.specification-resolver-delegate";

    /**
     *  Name of application extension used to resolve page and component
     *  templates that can't be located by the normal means.
     *  The extension must implement
     *  {@link org.apache.tapestry.engine.ITemplateSourceDelegate}.
     * 
     *  @since 2.4
     * 
     **/

    public static final String TEMPLATE_SOURCE_DELEGATE_EXTENSION_NAME =
        "org.apache.tapestry.template-source-delegate";

    /**
     *   Key used to obtain an extension from the application specification.  The extension,
     *   if it exists, implements {@link org.apache.tapestry.request.IRequestDecoder}.
     * 
     *   @since 2.2
     * 
     **/

    public static final String REQUEST_DECODER_EXTENSION_NAME =
        "org.apache.tapestry.request-decoder";

    /**
     *  Name of optional application extension for the multipart decoder
     *  used by the application.  The extension must implement
     *  {@link org.apache.tapestry.multipart.IMultipartDecoder}
     *  (and is generally a configured instance of
     *  {@link org.apache.tapestry.multipart.DefaultMultipartDecoder}).
     * 
     *  @since 2.4
     * 
     **/

    public static final String MULTIPART_DECODER_EXTENSION_NAME =
        "org.apache.tapestry.multipart-decoder";

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
     *  A {@link org.apache.tapestry.util.AdaptorRegistry} used to coerce arbitrary objects
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

    private static class DefaultIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            return (Iterator) value;
        }

    }

    private static class CollectionIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            Collection c = (Collection) value;

            if (c.size() == 0)
                return null;

            return c.iterator();
        }
    }

    private static class ObjectIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            return Collections.singleton(value).iterator();
        }
    }

    private static class ObjectArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            Object[] array = (Object[]) value;

            if (array.length == 0)
                return null;

            return Arrays.asList(array).iterator();
        }
    }

    private static class BooleanArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            boolean[] array = (boolean[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(array[i] ? Boolean.TRUE : Boolean.FALSE);

            return l.iterator();
        }
    }

    private static class ByteArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            byte[] array = (byte[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Byte(array[i]));

            return l.iterator();
        }
    }

    private static class CharArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            char[] array = (char[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Character(array[i]));

            return l.iterator();
        }
    }

    private static class ShortArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            short[] array = (short[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Short(array[i]));

            return l.iterator();
        }
    }

    private static class IntArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            int[] array = (int[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Integer(array[i]));

            return l.iterator();
        }
    }

    private static class LongArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            long[] array = (long[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Long(array[i]));

            return l.iterator();
        }
    }

    private static class FloatArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            float[] array = (float[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Float(array[i]));

            return l.iterator();
        }
    }

    private static class DoubleArrayIteratorAdaptor extends IteratorAdaptor
    {
        public Iterator coerce(Object value)
        {
            double[] array = (double[]) value;

            if (array.length == 0)
                return null;

            List l = new ArrayList(array.length);

            for (int i = 0; i < array.length; i++)
                l.add(new Double(array[i]));

            return l.iterator();
        }
    }

    static {
        _iteratorAdaptors.register(Iterator.class, new DefaultIteratorAdaptor());
        _iteratorAdaptors.register(Collection.class, new CollectionIteratorAdaptor());
        _iteratorAdaptors.register(Object.class, new ObjectIteratorAdaptor());
        _iteratorAdaptors.register(Object[].class, new ObjectArrayIteratorAdaptor());
        _iteratorAdaptors.register(boolean[].class, new BooleanArrayIteratorAdaptor());
        _iteratorAdaptors.register(byte[].class, new ByteArrayIteratorAdaptor());
        _iteratorAdaptors.register(char[].class, new CharArrayIteratorAdaptor());
        _iteratorAdaptors.register(short[].class, new ShortArrayIteratorAdaptor());
        _iteratorAdaptors.register(int[].class, new IntArrayIteratorAdaptor());
        _iteratorAdaptors.register(long[].class, new LongArrayIteratorAdaptor());
        _iteratorAdaptors.register(float[].class, new FloatArrayIteratorAdaptor());
        _iteratorAdaptors.register(double[].class, new DoubleArrayIteratorAdaptor());
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
     *		<td>Any Object array type</td>
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
     * <tr><td>array</td> <td>Converted to a {@link List} and iterator returned.
     * null returned if the array is empty.  This works with both object arrays and
     *  arrays of scalars. </td>
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

        IteratorAdaptor adaptor = (IteratorAdaptor) _iteratorAdaptors.getAdaptor(value.getClass());

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

                    throw new IllegalArgumentException(
                        "Unable to convert '" + s + "' to a Locale.");
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
            _strings = ResourceBundle.getBundle("org.apache.tapestry.TapestryStrings");

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
     *  Returns true if the Collection is null or empty.
     * 
     *  @since 2.4
     * 
     **/

    public static boolean isEmpty(Collection c)
    {
        return c == null || c.isEmpty();
    }

    /**
     *  Converts a {@link Map} to an even-sized array of key/value
     *  pairs.  This may be useful when using a Map as service parameters
     *  (with {@link org.apache.tapestry.link.DirectLink}.  Assuming the keys
     *  and values are simple objects (String, Boolean, Integer, etc.), then
     *  the representation as an array will encode more efficiently
     *  (via {@link org.apache.tapestry.util.io.DataSqueezer} than
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
            Map.Entry entry = (Map.Entry) i.next();

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
            throw new IllegalArgumentException(getString("Tapestry.even-sized-array"));

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

    /**
     *  Returns the application root location, which is in the
     *  {@link javax.servlet.ServletContext}, based on
     *  the {@link javax.servlet.http.HttpServletRequest#getServletPath() servlet path}.
     * 
     *  @since 2.4
     * 
     **/

    public static IResourceLocation getApplicationRootLocation(IRequestCycle cycle)
    {
        RequestContext context = cycle.getRequestContext();
        ServletContext servletContext = context.getServlet().getServletContext();
        String servletPath = context.getRequest().getServletPath();

        // Could strip off the servlet name (i.e., "app" in "/app") but
        // there's no need.

        return new ContextResourceLocation(servletContext, servletPath);
    }

    /**
     *  Given a Class, creates a presentable name for the class, even if the
     *  class is a scalar type or Array type.
     * 
     **/

    public static String getClassName(Class subject)
    {
        if (subject.isArray())
            return getClassName(subject.getComponentType()) + "[]";

        return subject.getName();
    }

    /**
     *  Selects the first {@link org.apache.tapestry.Location} in an array of objects.
     *  Skips over nulls.  The objects may be instances of
     *  {Location or {@link org.apache.tapestry.ILocatable}.  May return null
     *  if no Location found found.
     * 
     **/

    public static Location findLocation(Object[] locations)
    {
        for (int i = 0; i < locations.length; i++)
        {
            Object location = locations[i];

            if (location == null)
                continue;

            if (location instanceof Location)
                return (Location) location;

            if (location instanceof ILocatable)
            {
                ILocatable locatable = (ILocatable) location;
                Location result = locatable.getLocation();

                if (result != null)
                    return result;
            }
        }

        return null;
    }

    /**
     *  Creates an exception indicating the binding value is null.
     * 
     *  @since 2.4 
     * 
     **/

    public static BindingException createNullBindingException(IBinding binding)
    {
        return new BindingException(getString("null-value-for-binding"), binding);
    }

    /** @since 2.4 **/

    public static ApplicationRuntimeException createNoSuchComponentException(
        IComponent component,
        String id)
    {
        return new ApplicationRuntimeException(
            getString("no-such-component", component.getExtendedId(), id),
            component);
    }

    /** @since 2.4 **/

    public static BindingException createRequiredParameterException(
        IComponent component,
        String parameterName)
    {
        return new BindingException(
            getString("required-parameter", parameterName, component.getExtendedId()),
            component,
            component.getBinding(parameterName),
            null);
    }

    /** @since 2.4 **/

    public static ApplicationRuntimeException createRenderOnlyPropertyException(
        IComponent component,
        String propertyName)
    {
        return new ApplicationRuntimeException(
            getString("render-only-property", propertyName, component.getExtendedId()),
            component);
    }
}