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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.hivemind.Location;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.spec.PropertySpecification;

/**
 * Allow a property to be marked as persistent, and (optionally) allows a strategy to be set. Works
 * by adding a new {@link org.apache.tapestry.spec.IPropertySpecification} to the
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * <p>
 * The {@link org.apache.tapestry.annotations.Persist} annotation may <em>optionally</em> by
 * accompanied by a {@link org.apache.tapestry.annotations.InitialValue} annotation; this sets the
 * initial value, as a binding reference, used to initialize and re-initialize the property.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.annotations.Persist
 */
public class PersistAnnotationWorker implements MethodAnnotationEnhancementWorker
{
	/**
	 * Application property that gives the default property persistence strategy to use for properties annotated by @Persist
	 */
	public static final String DEFAULT_PROPERTY_PERSISTENCE_STRATEGY = "org.apache.tapestry.default-property-persistence-strategy";

	private IPropertySource _propertySource;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        Persist p = method.getAnnotation(Persist.class);
        InitialValue iv = method.getAnnotation(InitialValue.class);

        String propertyName = AnnotationUtils.getPropertyName(method);
	    String defaultStrategy = _propertySource.getPropertyValue(DEFAULT_PROPERTY_PERSISTENCE_STRATEGY);
        String stategy = p.value().length() == 0 ? defaultStrategy : p.value();

        IPropertySpecification pspec = new PropertySpecification();

        pspec.setName(propertyName);
        pspec.setPersistence(stategy);
        pspec.setLocation(location);
        pspec.setInitialValue(iv == null ? null : iv.value());

        spec.addPropertySpecification(pspec);
    }

	public void setPropertySource(IPropertySource propertySource)
	{
		_propertySource = propertySource;
	}
}
