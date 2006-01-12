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
 * Annotation related to {@link org.apache.tapestry.annotations.InjectState};
 * injects a read-only boolean property that determines if a particular
 * Application State Object exists or not; this is useful when trying to avoid
 * creating it (which can help keep the application stateless as long as
 * possible).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectStateFlag {

    /**
     * The id of the Application State Object; the boolean accessor method to
     * which the annotation is attached will return true when the ASO exists,
     * false when it does not.
     */

    String value();
}
