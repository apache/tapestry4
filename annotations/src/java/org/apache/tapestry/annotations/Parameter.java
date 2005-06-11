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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a parameter for the component.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */

@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    /**
     * If true, then the parameter is required, and must be bound (there is no guarantee that a
     * non-null value will be bound however, so the component may have to perform additonal checks).
     * The default value, false, means the parameter is optional.
     */

    boolean required() default false;

    /**
     * The default binding type, used when the parameter is bound without an explicit binding
     * prefix. Note that this default binding will apply to the {@link #defaultValue}.
     */

    String defaultBinding() default "";

    /**
     * The default value for the binding, as a binding reference.
     */

    String defaultValue() default "";

    /**
     * If true (the default), then the binding will cache its value while the component is
     * renderering. In some cases, it is desirable to force the binding to be re-evaluated every
     * time the parameter property is accessed, in which case cache should be set to false.
     */

    boolean cache() default true;

    /**
     * An optional list of alternate names for the parameter. The parameter may be bound using its
     * true name or any alias (but not both!), but use of aliases will generate deprecation
     * warnings.
     */

    String aliases() default "";
    
    /**
     * The name of the parameter.  If not specified, it will match the property name.  Note that this
     * is backwards from the logic in the XML, where the parameter name is specified and the property name
     * matches.
     * 
     */
    
    String name() default "";
}
