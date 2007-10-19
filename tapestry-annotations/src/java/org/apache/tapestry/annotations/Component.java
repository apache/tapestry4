// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used within a page or component class to define a contained component (which will
 * typically match up against a component reference in the template). This annotation is attached to
 * an accessor method.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * The component's id. Defaults to the property name if left unspecified.
     */

    String id() default "";

    /**
     * The component type. Defaults to the return type class name if left unspecified.
     */

    String type() default "";
    
    /**
     * The name of a previously defined component. 
     * The type and bindings of that component will be copied to this component. 
     * Either type or copy-of must be specified. 
     */

    String copyOf() default "";    

    /**
     * If true, then the component inherits informal parameters from its container.
     */

    boolean inheritInformalParameters() default false;

    /**
     * Bindings for the component. Each binding string is of the format
     * <code><em>name</em>=<em>binding reference</em></code>, where the binding reference is
     * the same kind of string (possibly with a prefix such as "ognl:" or "message:" as would appear
     * in a specification.
     * 
     * @Binding annotations.
     */

    String[] bindings() default {};

    /**
     * Inherited bindings bind a parameter of the component to a parameter
     * of the container. Each binding string is of the format
     * <code><em>parameter-name</em>=<em>container-parameter-name</em></code>, where the former is
     * the name of the component parameter and the latter is the name of the container parameter to bind
     * the parameter to.
     * In case both names are the same, it's possible to just use <code><em>parameter-name</em></code>.
     */

    String[] inheritedBindings() default {};
}
