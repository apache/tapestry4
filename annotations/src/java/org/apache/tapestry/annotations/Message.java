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
 * An annotation that may be attached to any method that returns a String. Acts as a wrapper around
 * {@link org.apache.hivemind.Messages}, converting any parameters into message arguments.
 * <p>
 * In many cases, the message key will be deduced from the method name (if not provided):
 * <ul>
 * <li>A prefix of "get" is stripped off, and the leading character converted to lower case
 * <li>Case changes are converted to dashes, i.e., "fooBar" becomes "foo-bar".
 * </ul>
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    /**
     * The message key to use. If no such value is defined, it is derived from the method name.
     */

    String value() default "";
}
