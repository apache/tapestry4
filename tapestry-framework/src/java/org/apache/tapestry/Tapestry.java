// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.multipart.IMultipartDecoder;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.StringSplitter;

/**
 * A placeholder for a number of (static) methods that don't belong elsewhere, as well as a global
 * location for static constants.
 * 
 * @since 1.0.1
 * @author Howard Lewis Ship
 */

public final class Tapestry
{
    /**
     * The name ("direct") of a service that allows stateless behavior for an {@link
     * org.apache.tapestry.link.DirectLink} component.
     * <p>
     * This service rolls back the state of the page but doesn't rewind the the dynamic state of the
     * page the was the action service does, which is more efficient but less powerful.
     * <p>
     * An array of String parameters may be included with the service URL; these will be made
     * available to the {@link org.apache.tapestry.link.DirectLink} component's listener.
     */

    public static final String DIRECT_SERVICE = "direct";

    /**
     * Almost identical to the direct service, except specifically for handling
     * browser level events.
     * 
     * @since 4.1
     */
    
    public static final String DIRECT_EVENT_SERVICE = "directevent";
    
    /**
     * The name ("external") of a service that a allows {@link IExternalPage} to be selected.
     * Associated with a {@link org.apache.tapestry.link.ExternalLink} component.
     * <p>
     * This service enables {@link IExternalPage}s to be accessed via a URL. External pages may be
     * booked marked using their URL for future reference.
     * <p>
     * An array of Object parameters may be included with the service URL; these will be passed to
     * the {@link IExternalPage#activateExternalPage(Object[], IRequestCycle)} method.
     */

    public static final String EXTERNAL_SERVICE = "external";

    /**
     * The name ("page") of a service that allows a new page to be selected. Associated with a
     * {@link org.apache.tapestry.link.PageLink} component.
     * <p>
     * The service requires a single parameter: the name of the target page.
     */

    public static final String PAGE_SERVICE = "page";

    /**
     * The name ("home") of a service that jumps to the home page. A stand-in for when no service is
     * provided, which is typically the entrypoint to the application.
     */

    public static final String HOME_SERVICE = "home";

    /**
     * The name ("restart") of a service that invalidates the session and restarts the application.
     * Typically used just to recover from an exception.
     */

    public static final String RESTART_SERVICE = "restart";

    /**
     * The name ("asset") of a service used to access internal assets.
     */

    public static final String ASSET_SERVICE = "asset";

    /**
     * The name ("reset") of a service used to clear cached template and specification data and
     * remove all pooled pages. This is only used when debugging as a quick way to clear the out
     * cached data, to allow updated versions of specifications and templates to be loaded (without
     * stopping and restarting the servlet container).
     * <p>
     * This service is only available if the Java system property
     * <code>org.apache.tapestry.enable-reset-service</code> is set to <code>true</code>.
     */

    public static final String RESET_SERVICE = "reset";
    
    /**
     * Property name used to get the extension used for templates. This may be set in the page or
     * component specification, or in the page (or component's) immediate container (library or
     * application specification). Unlike most properties, value isn't inherited all the way up the
     * chain. The default template extension is "html".
     * 
     * @since 3.0
     */

    public static final String TEMPLATE_EXTENSION_PROPERTY = "org.apache.tapestry.template-extension";

    /**
     * The name of an {@link org.apache.tapestry.IRequestCycle} attribute in which the currently
     * rendering {@link org.apache.tapestry.components.ILinkComponent} is stored. Link components do
     * not nest.
     */

    public static final String LINK_COMPONENT_ATTRIBUTE_NAME = "org.apache.tapestry.active-link-component";

    /**
     * Suffix appended to a parameter name to form the name of a property that stores the binding
     * for the parameter.
     * 
     * @since 3.0
     */

    public static final String PARAMETER_PROPERTY_NAME_SUFFIX = "Binding";

    /**
     * Key used to obtain an extension from the application specification. The extension, if it
     * exists, implements {@link org.apache.tapestry.request.IRequestDecoder}.
     * 
     * @since 2.2
     */

    public static final String REQUEST_DECODER_EXTENSION_NAME = "org.apache.tapestry.request-decoder";

    /**
     * Name of optional application extension for the multipart decoder used by the application. The
     * extension must implement {@link org.apache.tapestry.multipart.IMultipartDecoder} (and is
     * generally a configured instance of
     * {@link IMultipartDecoder}).
     * 
     * @since 3.0
     */

    public static final String MULTIPART_DECODER_EXTENSION_NAME = "org.apache.tapestry.multipart-decoder";

    /**
     * Method id used to check that {@link IPage#validate(IRequestCycle)} is invoked.
     * 
     * @see #checkMethodInvocation(Object, String, Object)
     * @since 3.0
     */

    public static final String ABSTRACTPAGE_VALIDATE_METHOD_ID = "AbstractPage.validate()";

    /**
     * Method id used to check that {@link IPage#detach()} is invoked.
     * 
     * @see #checkMethodInvocation(Object, String, Object)
     * @since 3.0
     */

    public static final String ABSTRACTPAGE_DETACH_METHOD_ID = "AbstractPage.detach()";

    /**
     * Regular expression defining a simple property name. Used by several different parsers. Simple
     * property names match Java variable names; a leading letter (or underscore), followed by
     * letters, numbers and underscores.
     * 
     * @since 3.0
     */

    public static final String SIMPLE_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z]\\w*$";

    /**
     * Class name of an {@link ognl.TypeConverter}implementing class to use as a type converter for
     * {@link org.apache.tapestry.binding.ExpressionBinding}.
     */
    public static final String OGNL_TYPE_CONVERTER = "org.apache.tapestry.ognl-type-converter";

    /**
     * The version of the framework; this is updated for major releases.
     */

    public static final String VERSION = readVersion();

    private static final String UNKNOWN_VERSION = "Unknown";
    
    /**
     * Contains strings loaded from TapestryStrings.properties.
     * 
     * @since 1.0.8
     */

    private static ResourceBundle _strings;

    /**
     * A {@link Map}that links Locale names (as in {@link Locale#toString()}to {@link Locale}
     * instances. This prevents needless duplication of Locales.
     */

    private static final Map _localeMap = new HashMap();

    static
    {
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++)
        {
            _localeMap.put(locales[i].toString(), locales[i]);
        }
    }

    /**
     * Used for tracking if a particular super-class method has been invoked.
     */

    private static final ThreadLocal _invokedMethodIds = new ThreadLocal();

    
    /**
     * Prevent instantiation.
     */

    private Tapestry()
    {
    }

    /**
     * Copys all informal {@link IBinding bindings}from a source component to the destination
     * component. Informal bindings are bindings for informal parameters. This will overwrite
     * parameters (formal or informal) in the destination component if there is a naming conflict.
     */

    public static void copyInformalBindings(IComponent source, IComponent destination)
    {
        Collection names = source.getBindingNames();

        if (names == null)
            return;

        IComponentSpecification specification = source.getSpecification();
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
     * Gets the {@link Locale}for the given string, which is the result of
     * {@link Locale#toString()}. If no such locale is already registered, a new instance is
     * created, registered and returned.
     */

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
                case 1:

                    result = new Locale(terms[0], "");
                    break;

                case 2:

                    result = new Locale(terms[0], terms[1]);
                    break;

                case 3:

                    result = new Locale(terms[0], terms[1], terms[2]);
                    break;

                default:

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
     * Closes the stream (if not null), ignoring any {@link IOException}thrown.
     * 
     * @since 1.0.2
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
     * Gets a string from the TapestryStrings resource bundle. The string in the bundle is treated
     * as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     * @since 1.0.8
     */

    public static String format(String key, Object[] args)
    {
        if (_strings == null)
            _strings = ResourceBundle.getBundle("org.apache.tapestry.TapestryStrings");

        String pattern = _strings.getString(key);

        if (args == null)
            return pattern;

        return MessageFormat.format(pattern, args);
    }

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     * 
     * @since 3.0
     */

    public static String getMessage(String key)
    {
        return format(key, null);
    }

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     * 
     * @since 3.0
     */

    public static String format(String key, Object arg)
    {
        return format(key, new Object[]
        { arg });
    }

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     * 
     * @since 3.0
     */

    public static String format(String key, Object arg1, Object arg2)
    {
        return format(key, new Object[]
        { arg1, arg2 });
    }

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     * 
     * @since 3.0
     */

    public static String format(String key, Object arg1, Object arg2, Object arg3)
    {
        return format(key, new Object[]
        { arg1, arg2, arg3 });
    }

    /**
     * Invoked when the class is initialized to read the current version file.
     */

    private static String readVersion()
    {
        Properties props = new Properties();

        try
        {
            InputStream in = Tapestry.class.getResourceAsStream("version.properties");

            if (in == null)
                return UNKNOWN_VERSION;

            props.load(in);

            in.close();

            return props.getProperty("project.version", UNKNOWN_VERSION);
        }
        catch (IOException ex)
        {
            return UNKNOWN_VERSION;
        }

    }

    /**
     * Returns the size of a collection, or zero if the collection is null.
     * 
     * @since 2.2
     */

    public static int size(Collection c)
    {
        if (c == null)
            return 0;

        return c.size();
    }

    /**
     * Returns the length of the array, or 0 if the array is null.
     * 
     * @since 2.2
     */

    public static int size(Object[] array)
    {
        if (array == null)
            return 0;

        return array.length;
    }

    /**
     * Returns true if the Map is null or empty.
     * 
     * @since 3.0
     */

    public static boolean isEmpty(Map map)
    {
        return map == null || map.isEmpty();
    }

    /**
     * Returns true if the Collection is null or empty.
     * 
     * @since 3.0
     */

    public static boolean isEmpty(Collection c)
    {
        return c == null || c.isEmpty();
    }

    /**
     * Converts a {@link Map} to an even-sized array of key/value pairs. This may be useful when
     * using a Map as service parameters (with {@link org.apache.tapestry.link.DirectLink}.
     * Assuming the keys and values are simple objects (String, Boolean, Integer, etc.), then the
     * representation as an array will encode more efficiently (via
     * {@link org.apache.tapestry.util.io.DataSqueezerImpl} than serializing the Map and its
     * contents.
     * 
     * @return the array of keys and values, or null if the input Map is null or empty
     * @since 2.2
     */

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
     * Converts an even-sized array of objects back into a {@link Map}.
     * 
     * @see #convertMapToArray(Map)
     * @return a Map, or null if the array is null or empty
     * @since 2.2
     */

    public static Map convertArrayToMap(Object[] array)
    {
        if (array == null || array.length == 0)
            return null;

        if (array.length % 2 != 0)
            throw new IllegalArgumentException(getMessage("Tapestry.even-sized-array"));

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
     * Creates an exception indicating the binding value is null.
     * 
     * @since 3.0
     */

    public static BindingException createNullBindingException(IBinding binding)
    {
        return new BindingException(getMessage("null-value-for-binding"), binding);
    }

    /** @since 3.0 * */

    public static ApplicationRuntimeException createNoSuchComponentException(IComponent component,
            String id, Location location)
    {
        return new ApplicationRuntimeException(format("no-such-component", component
                .getExtendedId(), id), component, location, null);
    }

    /** @since 3.0 * */

    public static BindingException createRequiredParameterException(IComponent component,
            String parameterName)
    {
        return new BindingException(format("required-parameter", parameterName, component
                .getExtendedId()), component, null, component.getBinding(parameterName), null);
    }

    /** @since 3.0 * */

    public static ApplicationRuntimeException createRenderOnlyPropertyException(
            IComponent component, String propertyName)
    {
        return new ApplicationRuntimeException(format(
                "render-only-property",
                propertyName,
                component.getExtendedId()), component, null, null);
    }

    /**
     * Clears the list of method invocations.
     * 
     * @see #checkMethodInvocation(Object, String, Object)
     * @since 3.0
     */

    public static void clearMethodInvocations()
    {
        _invokedMethodIds.set(null);
    }

    /**
     * Adds a method invocation to the list of invocations. This is done in a super-class
     * implementations.
     * 
     * @see #checkMethodInvocation(Object, String, Object)
     * @since 3.0
     */

    public static void addMethodInvocation(Object methodId)
    {
        List methodIds = (List) _invokedMethodIds.get();

        if (methodIds == null)
        {
            methodIds = new ArrayList();
            _invokedMethodIds.set(methodIds);
        }

        methodIds.add(methodId);
    }

    /**
     * Checks to see if a particular method has been invoked. The method is identified by a methodId
     * (usually a String). The methodName and object are used to create an error message.
     * <p>
     * The caller should invoke {@link #clearMethodInvocations()}, then invoke a method on the
     * object. The super-class implementation should invoke {@link #addMethodInvocation(Object)} to
     * indicate that it was, in fact, invoked. The caller then invokes this method to validate that
     * the super-class implementation was invoked.
     * <p>
     * The list of method invocations is stored in a {@link ThreadLocal} variable.
     * 
     * @since 3.0
     */

    public static void checkMethodInvocation(Object methodId, String methodName, Object object)
    {
        List methodIds = (List) _invokedMethodIds.get();

        if (methodIds != null && methodIds.contains(methodId))
            return;

        throw new ApplicationRuntimeException(Tapestry.format(
                "Tapestry.missing-method-invocation",
                object.getClass().getName(),
                methodName));
    }

    /**
     * Method used by pages and components to send notifications about property changes.
     * 
     * @param component
     *            the component containing the property
     * @param propertyName
     *            the name of the property which changed
     * @param newValue
     *            the new value for the property
     * @since 3.0
     */
    public static void fireObservedChange(IComponent component, String propertyName, Object newValue)
    {
        ChangeObserver observer = component.getPage().getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(component, propertyName, newValue);

        observer.observeChange(event);
    }
}
