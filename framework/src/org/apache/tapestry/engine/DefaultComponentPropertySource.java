//Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.LocalizedPropertySource;
import org.apache.tapestry.util.PropertyHolderPropertySource;

/**
 *  The default property source for component configuration properties.
 *  The search order this property source follows is the following:<br>
 *  <ul>
 *    <li>Component specification
 *    <li>Namespace (library) specification
 *    <li>the rest of the search path as defined in the application property source
 *  </ul>
 *
 *  <p>If a locale has been passed to this object, it will first search for 
 *  the localized versions of the requested property (e.g. property_en_us, 
 *  property_en) and will look at the property itself only at the end.  
 *  
 *  @author mb
 *  @version $Id$
 *  @since 3.1
 */
public class DefaultComponentPropertySource implements IPropertySource
{
    private IPropertySource _degatingPropertySource;

	/**
	 * Creates a new default component property source with no associate locale.
	 * The property source will only use the path specified in the class documentation.
	 * 
	 * @param component the component for which the properties will be looked up
	 * @param applicationPropertySource the property source for the application
	 */
	public DefaultComponentPropertySource(IComponent component, IPropertySource applicationPropertySource) {
		this(component, applicationPropertySource, null);
	}
	
	/**
	 * Creates a new default component property source that is associated with the given locale.
	 * The property source will search property path specified in the class documentation for 
	 * the localized versions of the property first.
	 * 
	 * @param component the component for which the properties will be looked up
	 * @param applicationPropertySource the property source for the application
	 * @param locale the locale to be used for localizing the properties
	 */
	public DefaultComponentPropertySource(IComponent component, IPropertySource applicationPropertySource, Locale locale) {
        DelegatingPropertySource source = new DelegatingPropertySource();

        // Search for the encoding property in the following order:
        // First search the component specification
        source.addSource(new PropertyHolderPropertySource(component.getSpecification()));

        // Then search its library specification
        source.addSource(new PropertyHolderPropertySource(component.getNamespace().getSpecification()));

        // Then search the rest of the standard path
        source.addSource(applicationPropertySource);

        if (locale != null)
            source = new LocalizedPropertySource(locale, source);
        
        _degatingPropertySource = source;
	}
	
	/**
	 * @see org.apache.tapestry.engine.IPropertySource#getPropertyValue(java.lang.String)
	 */
	public String getPropertyValue(String propertyName)
	{
		return _degatingPropertySource.getPropertyValue(propertyName);
	}
}
