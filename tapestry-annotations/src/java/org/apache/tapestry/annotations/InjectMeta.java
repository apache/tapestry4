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
 * Creates an {@link org.apache.tapestry.spec.InjectSpecification} for a &lt;meta&gt; data value,
 * and adds it to the {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectMeta {

    /**
     * The meta key to inject. If no such value is defined, it is derived from the method name.
     */

    String value() default "";
}
