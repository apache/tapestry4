// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.services;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;

import java.util.Locale;

/**
 * Encapsulates the logic for searching for component meta-data. Deployed as service
 * tapestry.props.ComponentPropertySource.
 *
 * <p>
 * TODO: Adjust name, since it now provides access to namespace properties as well as component
 * properties.
 * </p>
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ComponentPropertySource
{
    /**
     * Returns the property value for a particular named meta-data property of the component. The
     * search order is:
     * <ul>
     * <li>The component's specification</li>
     * <li>The specification of the application (or the library containing the component).</li>
     * <li>The chain of global property sources.</li>
     * </ul>
     *
     * @param component
     *          The {@link IComponent} to get the property of.
     * @param propertyName
     *          Key of the property.
     * 
     * @return the value of the given key, or null if not found.
     */

    String getComponentProperty(IComponent component, String propertyName);

    /**
     * Like {@link #getComponentProperty(IComponent, String)}, but the property name will be
     * localized to the component's current locale (determined from its page). Localizing the
     * property name means that a suffix may be appended to it. If the fully localized name is not
     * found, then the locale is generalized (i.e., from "en_UK" to "en" to nothing) until a match
     * is found.
     *
     * @param component
     *          The {@link IComponent} to get the property of.
     * @param locale
     *          The {@link Locale} to get properties for.
     * @param propertyName
     *          Key of the property.
     *
     * @return the value of the given property name, or null if not found.
     */
    String getLocalizedComponentProperty(IComponent component, Locale locale, String propertyName);

    /**
     * Returns the property value for a particular named meta-data property of the namespace. The
     * search order is:
     * 
     * <ul>
     * <li>The library or application specification for the namespace.</li>
     * <li>The chain of global property sources.</li>
     * </ul>
     *
     * @param namespace
     *          The namespace to get the property from.
     * @param propertyName
     *          The key of the property to get.
     * 
     * @return the value of the given key, or null if not found.
     */

    String getNamespaceProperty(INamespace namespace, String propertyName);

    /**
     * As with {@link #getLocalizedComponentProperty(IComponent, Locale, String)}, but with a
     * {@link INamespace}.
     *
     * @param namespace
     *          The namespace to get the property from.
     * @param locale
     *          {@link Locale} to filter the properties for.
     * @param propertyName
     *          The key of the property to get.
     *
     * @return The matching property, or null if not found.
     */

    String getLocalizedNamespaceProperty(INamespace namespace, Locale locale, String propertyName);
}
