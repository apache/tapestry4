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
 * Allows a property to be marked as persistent (and, optionally, provide control over how the
 * property is persisted).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Persist {

    /**
     * The persistence strategy, defaulting to "session" (meaning, in the HttpSession, as a session
     * attribute). Tapestry includes another strategy, "client", and additional strategies can be
     * defined.
     */
    String value() default "session";
}
