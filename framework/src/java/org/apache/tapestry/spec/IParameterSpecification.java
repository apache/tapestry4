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

import org.apache.hivemind.LocationHolder;

/**
 * Defines a formal parameter to a component. An <code>IParameterSpecification</code> is contained
 * by a {@link IComponentSpecification}.
 * <p>
 * TBD: Identify arrays in some way.
 * 
 * @author glongman@intelligentworks.com
 */
public interface IParameterSpecification extends LocationHolder
{
    /**
     * Returns the class name of the expected type of the parameter. The default value is
     * <code>java.lang.Object</code> which matches anything.
     */
    public String getType();

    /**
     * Returns true if the parameter is required by the component. The default is false, meaning the
     * parameter is optional.
     */
    public boolean isRequired();

    public void setRequired(boolean value);

    /**
     * Sets the type of value expected for the parameter. This can be left blank to indicate any
     * type.
     */
    public void setType(String value);

    /**
     * Returns the documentation for this parameter.
     * 
     * @since 1.0.9
     */
    public String getDescription();

    /**
     * Sets the documentation for this parameter.
     * 
     * @since 1.0.9
     */
    public void setDescription(String description);

    /**
     * Sets the property name (of the component class) to connect the parameter to.
     */
    public void setPropertyName(String propertyName);

    /**
     * Returns the name of the JavaBeans property to connect the parameter to.
     */
    public String getPropertyName();

    /**
     * Returns the default value for the parameter (or null if the parameter has no default value).
     * Required parameters may not have a default value. The default value is a
     * <em>binding locator</em>(a prefixed value, as with a binding element).
     */
    public String getDefaultValue();

    /**
     * Sets the default value of the JavaBeans property if no binding is provided
     */
    public void setDefaultValue(String defaultValue);

    /**
     * Sets the default binding type, used when a parameter is bound without specifying an explicit
     * binding prefix. May be null, in which case the default binding type is determined by whether
     * the parameter is bound in an HTML template or in a page or component specification.
     * 
     * @see org.apache.tapestry.binding.BindingConstants
     * @see org.apache.tapestry.binding.BindingUtils#getDefaultBindingType(IComponentSpecification,
     *      String, String)
     * @since 4.0
     */

    public void setDefaultBindingType(String bindingType);

    public String getDefaultBindingType();

}