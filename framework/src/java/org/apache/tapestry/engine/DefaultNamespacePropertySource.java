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

package org.apache.tapestry.engine;

import java.util.Locale;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.LocalizedPropertySource;
import org.apache.tapestry.util.PropertyHolderPropertySource;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DefaultNamespacePropertySource implements IPropertySource
{
    private IPropertySource _delegatingPropertySource;

    /**
     * Creates a new default component property source that is associated with the given locale. The
     * property source will search property path specified in the class documentation for the
     * localized versions of the property first.
     * 
     * @param namespace
     *            the namespace for which the properties will be looked up
     * @param applicationPropertySource
     *            the property source for the application
     * @param locale
     *            the locale to be used for localizing the properties
     */
    public DefaultNamespacePropertySource(INamespace namespace,
            IPropertySource applicationPropertySource, Locale locale)
    {
        DelegatingPropertySource source = new DelegatingPropertySource();

        // Search for the encoding property in the following order:
        // First search the component specification
        source.addSource(new PropertyHolderPropertySource(namespace.getSpecification()));

        // Then search the rest of the standard path
        source.addSource(applicationPropertySource);

        if (locale != null)
            source = new LocalizedPropertySource(locale, source);

        _delegatingPropertySource = source;
    }

    public String getPropertyValue(String propertyName)
    {
        return _delegatingPropertySource.getPropertyValue(propertyName);
    }

}