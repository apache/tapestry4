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
 * A class-level annotation that identifies a class as a component. Note that values defined by this
 * annotation will <strong>override</strong> corresponding values in the XML component
 * specification. At this time it is still necessary to have a component specification, even if it
 * is empty (this limitation may be lifted before the final 4.0 release).
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentClass {

    /**
     * If true (the default), then the defined component will allow and use it's body. Otherwise the
     * body is discarded (which may cause errors if the body contains components).
     */

    boolean allowBody() default true;

    /**
     * If true (the default), then the component accepts informal parameters. Generally, informal
     * parameters become additional attributes of the element rendered by this component.
     */

    boolean allowInformalParameters() default true;

    /**
     * A comma-seperated list of parameter names that can not be bound informally. These represent
     * attributes generated internally by the component, and this is used to prevent name conflicts.
     * Comparison of informal parameter name against reserved parameter name is caseless. Note also
     * that all formal parameters are automatically part of the list of reserved parameters.
     */
    String reservedParameters() default "";

}
