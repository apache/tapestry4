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

import java.util.List;

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.bean.IBeanInitializer;
import org.apache.tapestry.util.IPropertyHolder;

/**
 *  A specification of a helper bean for a component.
 *
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IBeanSpecification extends IPropertyHolder, ILocationHolder, ILocatable
{
    public abstract String getClassName();
    public abstract BeanLifecycle getLifecycle();
    /**
     *  @since 1.0.5
     *
     **/
    public abstract void addInitializer(IBeanInitializer initializer);
    /**
     *  Returns the {@link List} of {@link IBeanInitializer}s.  The caller
     *  should not modify this value!.  May return null if there
     *  are no initializers.
     *
     *  @since 1.0.5
     *
     **/
    public abstract List getInitializers();
    public abstract String toString();
    public abstract String getDescription();
    public abstract void setDescription(String desc);
    /** @since 3.0 **/
    public abstract void setClassName(String className);
    /** @since 3.0 **/
    public abstract void setLifecycle(BeanLifecycle lifecycle);
}