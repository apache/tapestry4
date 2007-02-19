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

import java.util.Map;
import java.util.WeakHashMap;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.enhance.EnhanceUtils;


/**
 * Default implementation of {@link PropertyChangeObserver} that creates a bytecode enhanced
 * proxy instance of {@link CglibPropertyChangeInterceptor} to notify Tapestry of changes
 * made to the first level public properties available on the observed object.
 */
public class CglibProxiedPropertyChangeObserverImpl implements PropertyChangeObserver
{
    
    /**
     * Holds a mapping of previously enhanced property class proxy {@link Factory} objects
     * keyed off of {@link IComponent#getIdPath()} + <code>propertyName</code>.
     */
    private Map _enhancedMap = new WeakHashMap();
    
    /**
     * Holds previously reflected {@link Class#getName()} values on any property instances
     * that don't have default constructors, which we can't enhance.
     */
    private Map _badMap = new WeakHashMap();
    
    public CglibProxiedPropertyChangeObserverImpl()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public Object observePropertyChanges(IComponent component, Object property, String propertyName)
    {
        Defense.notNull(component, "component");
        
        // can't proxy a null object
        if (property == null)
            return null;
        
        // if it's already proxied just return it
        
        if (Enhancer.isEnhanced(property.getClass()))
            return property;
        
        // possibly already did the expensive reflection on this type
        
        if (_badMap.get(property.getClass().getName()) != null)
            return property;
        
        Factory f = (Factory) _enhancedMap.get(component.getIdPath() + propertyName);
        
        if (f == null) {
            
            // some classes can't have their constructors detected until now..
            
            if (!EnhanceUtils.hasEmptyConstructor(property.getClass())) {
                
                _badMap.put(property.getClass().getName(), Boolean.TRUE);
                return property;
            }

            Object ret = Enhancer.create(property.getClass(), property.getClass().getInterfaces(), 
                    new ObservableMethodFilter(), 
                    new Callback[] { new LazyProxyDelegate(property), new CglibPropertyChangeInterceptor(component, property, propertyName)});
            
            f = (Factory)ret;
            _enhancedMap.put(component.getIdPath() + propertyName, f);
            
            return ret;
        }
        
        return f.newInstance(new Callback[] { new LazyProxyDelegate(property), new CglibPropertyChangeInterceptor(component, property, propertyName)});
    }
}
