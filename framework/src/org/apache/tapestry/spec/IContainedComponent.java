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

import java.util.Collection;

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.util.IPropertyHolder;

/**
 * Defines a contained component.  This includes the information needed to
 * get the contained component's specification, as well as any bindings
 * for the component.

 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IContainedComponent extends IPropertyHolder, ILocationHolder, ILocatable
{
    /**
     *  Returns the named binding, or null if the binding does not
     *  exist.
     *
     **/
    public abstract IBindingSpecification getBinding(String name);
    /**
     *  Returns an umodifiable <code>Collection</code>
     *  of Strings, each the name of one binding
     *  for the component.
     *
     **/
    public abstract Collection getBindingNames();
    public abstract String getType();
    public abstract void setBinding(String name, IBindingSpecification spec);
    public abstract void setType(String value);
    /**
     * 	Sets the String Id of the component being copied from.
     *  For use by IDE tools like Spindle.
     * 
     *  @since 1.0.9
     **/
    public abstract void setCopyOf(String id);
    /**
     * 	Returns the id of the component being copied from.
     *  For use by IDE tools like Spindle.
     * 
     *  @since 1.0.9
     **/
    public abstract String getCopyOf();

    /**
     * Returns whether the contained component will inherit 
     * the informal parameters of its parent. 
     * 
     * @since 3.0
     **/
    public abstract boolean getInheritInformalParameters();

    /**
     * Sets whether the contained component will inherit 
     * the informal parameters of its parent. 
     * 
     * @since 3.0
     */
    public abstract void setInheritInformalParameters(boolean value);
}