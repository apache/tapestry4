//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.bean;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.util.pool.IPoolable;

/**
 *  A helper bean to assist with providing defaults for unspecified
 *  parameters.    It is initalized
 *  with an {@link IBinding} and a default value.  It's value property
 *  is either the value of the binding, but if the binding is null,
 *  or the binding returns null, the default value is returned.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class Default implements IPoolable
{
    private IBinding binding;
    private Object defaultValue;

    public void resetForPool()
    {
        binding = null;
        defaultValue = null;
    }

    public void setBinding(IBinding value)
    {
        binding = value;
    }

    public IBinding getBinding()
    {
        return binding;
    }

    public void setDefaultValue(Object value)
    {
        defaultValue = value;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    /**
     *  Returns the value of the binding.  However, if the binding is null, or the binding
     *  returns null, then the defaultValue is returned instead.
     *
     **/

    public Object getValue()
    {
        if (binding == null)
            return defaultValue;

        Object value = binding.getObject();

        if (value == null)
            return defaultValue;

        return value;
    }
    
    /** @since 3.0 **/
    
    public void discardFromPool()
    {
    }

}