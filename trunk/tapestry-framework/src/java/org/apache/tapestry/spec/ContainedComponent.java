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

package org.apache.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a contained component. This includes the information needed to get the contained
 * component's specification, as well as any bindings for the component.
 * 
 * @author Howard Lewis Ship
 */

public class ContainedComponent extends LocatablePropertyHolder implements IContainedComponent
{
    private static final int MAP_SIZE = 3;
    
    protected Map _bindings;
    
    private String _type;

    private String _copyOf;

    private boolean _inheritInformalParameters;

    /** @since 4.0 */
    private String _propertyName;

    /**
     * Returns the named binding, or null if the binding does not exist.
     */

    public IBindingSpecification getBinding(String name)
    {
        if (_bindings == null)
            return null;

        return (IBindingSpecification) _bindings.get(name);
    }

    /**
     * Returns an umodifiable <code>Collection</code> of Strings, each the name of one binding for
     * the component.
     */

    public Collection getBindingNames()
    {
        if (_bindings == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableCollection(_bindings.keySet());
    }

    public String getType()
    {
        return _type;
    }

    public void setBinding(String name, IBindingSpecification spec)
    {
        if (_bindings == null)
            _bindings = new HashMap(MAP_SIZE);

        _bindings.put(name, spec);
    }

    public void setType(String value)
    {
        _type = value;
    }
    
    /**
     * Sets the String Id of the component being copied from. For use by IDE tools like Spindle.
     * 
     * @since 1.0.9
     */

    public void setCopyOf(String id)
    {
        _copyOf = id;
    }

    /**
     * Returns the id of the component being copied from. For use by IDE tools like Spindle.
     * 
     * @since 1.0.9
     */

    public String getCopyOf()
    {
        return _copyOf;
    }

    /**
     * Returns whether the contained component will inherit the informal parameters of its parent.
     * 
     * @since 3.0
     */
    public boolean getInheritInformalParameters()
    {
        return _inheritInformalParameters;
    }

    /**
     * Sets whether the contained component will inherit the informal parameters of its parent.
     * 
     * @since 3.0
     */
    public void setInheritInformalParameters(boolean value)
    {
        _inheritInformalParameters = value;
    }

    /** @since 4.0 */
    public String getPropertyName()
    {
        return _propertyName;
    }

    /** @since 4.0 */
    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName;
    }
}
