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
     * The component type.
     */

    String type();

    /**
     * If true, then the component inherits informal parameters from its container.
     */

    boolean inheritInformalParameters() default false;

    /**
     * Bindings for the component. Each binding string is of the format
     * <code><em>name</em>=<em>binding refernce</em></code>, where the binding reference is
     * the same kind of string (possibly with a prefix such as "ognl:" or "message:" as would appear
     * in a specification.
     * 
     * @Binding annotations.
     */

    String[] bindings() default {};
}
