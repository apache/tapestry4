// Copyright 2005, 2006 The Apache Software Foundation
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
 * Injects a compiled {@link org.apache.tapestry.IScript}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectScript {

    /**
     * The path, relative to the specification (if it exists), or the template (if it does not),j of
     * the script file.
     */

    String value();
}
