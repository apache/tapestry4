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
package org.apache.tapestry.record;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;


/**
 * Class responsible for watching changes on a specific property instance attached to a component. 
 * 
 * Used by the default {@link PropertyChangeObserver} service.
 */
public class CglibPropertyChangeInterceptor implements MethodInterceptor, ObservedProperty
{
    private Object _property;
    
    private transient IComponent _component;
    private String _propertyName;
    
    /**
     * Creates a new property change observer for the specified component / property.
     * 
     * @param component
     *          The component that owns this property.
     * @param property
     *          The actual property object, may be null.
     * @param propertyName
     *          The name of the property.
     */
    public CglibPropertyChangeInterceptor(IComponent component, Object property, String propertyName)
    {
        _property = property;
        _component = component;
        _propertyName = propertyName;
    }
    
    public Object getCGProperty()
    {
        return _property;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable
    {
        if (method.getDeclaringClass() == ObservedProperty.class)
            return getCGProperty();
        
        if (_component.getPage().getChangeObserver() != null
                && !_component.getPage().getChangeObserver().isLocked()) {
            
            Tapestry.fireObservedChange(_component, _propertyName, _property);
        }
        
        // invoke the method being called either way
        
        return proxy.invoke(_property, args);
    }

}
