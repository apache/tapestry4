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

import org.apache.tapestry.ILocationHolder;

/**
 *  Defines a transient or persistant property of a component or page.  
 *  A {@link org.apache.tapestry.engine.IComponentClassEnhancer} uses this information
 *  to create a subclass with the necessary instance variables and methods.  
 *  
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IPropertySpecification extends ILocationHolder
{
    public abstract String getInitialValue();
    public abstract String getName();
    public abstract boolean isPersistent();
    public abstract String getType();
    public abstract void setInitialValue(String initialValue);
    /**
     *  Sets the name of the property.  This should not be changed
     *  once this IPropertySpecification is added to
     *  a {@link org.apache.tapestry.spec.IComponentSpecification}.
     * 
     **/
    public abstract void setName(String name);
    public abstract void setPersistent(boolean persistant);
    public abstract void setType(String type);
}