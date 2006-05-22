// Copyright May 14, 2006 The Apache Software Foundation
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
 * Annotation used to connect an event on a component / page 
 * with a particular listener method. This is currently intended
 * to be used to connect client side events to listener methods but
 * may have uses elsewhere.
 * 
 * @author jkuhnert
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener
{
    /**
     * The unique {@link IComponent} ids of the targeted 
     * sources that this listener will be listening to events on. 
     */
    String[] targets() default {};
    
    /**
     * The unique html element ids to listen to the events on.
     */
    String[] elements() default {};
    
    /**
     * The list of events that should cause this listener to invoke. 
     * Ie <code>events = {"onClick", "onOptionSelect"}</code> etc..
     */
    String[] events();
}