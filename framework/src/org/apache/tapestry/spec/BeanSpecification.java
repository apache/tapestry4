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

package org.apache.tapestry.spec;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.bean.IBeanInitializer;

/**
 *  A specification of a helper bean for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanSpecification extends LocatablePropertyHolder implements IBeanSpecification
{
    protected String className;
    protected BeanLifecycle lifecycle;

    /** @since 1.0.9 **/
    private String description;

    /**
     *  A List of {@link IBeanInitializer}.
     *
     **/

    protected List initializers;

    public String getClassName()
    {
        return className;
    }

    public BeanLifecycle getLifecycle()
    {
        return lifecycle;
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void addInitializer(IBeanInitializer initializer)
    {
        if (initializers == null)
            initializers = new ArrayList();

        initializers.add(initializer);
    }

    /**
     *  Returns the {@link List} of {@link IBeanInitializer}s.  The caller
     *  should not modify this value!.  May return null if there
     *  are no initializers.
     *
     *  @since 1.0.5
     *
     **/

    public List getInitializers()
    {
        return initializers;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("BeanSpecification[");

        buffer.append(className);
        buffer.append(", lifecycle ");
        buffer.append(lifecycle.getName());

        if (initializers != null && initializers.size() > 0)
        {
            buffer.append(", ");
            buffer.append(initializers.size());
            buffer.append(" initializers");
        }

        buffer.append(']');

        return buffer.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }

    /** @since 3.0 **/

    public void setClassName(String className)
    {
        this.className = className;
    }
    
    /** @since 3.0 **/
    
    public void setLifecycle(BeanLifecycle lifecycle)
    {
        this.lifecycle = lifecycle;
    }

}