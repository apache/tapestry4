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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.bean.IBeanInitializer;

/**
 * A specification of a helper bean for a component.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.4
 */

public class BeanSpecification extends LocatablePropertyHolder implements IBeanSpecification
{
    protected String _className;

    protected BeanLifecycle _lifecycle;

    /**
     * A List of {@link IBeanInitializer}.
     */

    protected List _initializers;

    /** @since 1.0.9 * */
    private String _description;

    /** @since 4.0 */

    private String _propertyName;
    
    public String getClassName()
    {
        return _className;
    }

    public BeanLifecycle getLifecycle()
    {
        return _lifecycle;
    }

    /**
     * @since 1.0.5
     */

    public void addInitializer(IBeanInitializer initializer)
    {
        if (_initializers == null)
            _initializers = new ArrayList();

        _initializers.add(initializer);
    }

    /**
     * Returns the {@link List}of {@link IBeanInitializer}s. The caller should not modify this
     * value!. May return null if there are no initializers.
     * 
     * @since 1.0.5
     */

    public List getInitializers()
    {
        return _initializers;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("BeanSpecification[");

        buffer.append(_className);
        buffer.append(", lifecycle ");
        buffer.append(_lifecycle.getName());

        if (_initializers != null && _initializers.size() > 0)
        {
            buffer.append(", ");
            buffer.append(_initializers.size());
            buffer.append(" initializers");
        }

        buffer.append(']');

        return buffer.toString();
    }

    public String getDescription()
    {
        return _description;
    }

    public void setDescription(String desc)
    {
        _description = desc;
    }

    /** @since 3.0 * */

    public void setClassName(String className)
    {
        this._className = className;
    }

    /** @since 3.0 * */

    public void setLifecycle(BeanLifecycle lifecycle)
    {
        this._lifecycle = lifecycle;
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
