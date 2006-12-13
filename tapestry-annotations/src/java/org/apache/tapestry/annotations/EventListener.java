// Copyright May 14, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.tapestry.IComponent;

/**
 * Annotation used to connect an event on a component / page with a particular listener method. This
 * is currently intended to be used to connect client side events to listener methods but may have
 * uses elsewhere.
 * 
 * @author jkuhnert
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener
{

    /**
     * The unique {@link IComponent} ids of the targeted sources that this listener will be
     * listening to events on.
     */
    String[] targets() default {};

    /**
     * The unique html element ids to listen to the events on.
     */
    String[] elements() default {};

    /**
     * The list of events that should cause this listener to invoke. Ie
     * <code>events = {"onClick", "onOptionSelect"}</code> etc..
     */
    String[] events();

    /**
     * The form id of the form that should have its data submitted when one of the specified events
     * is triggered.
     * 
     * @return The form name (or id of component) to submit when event is triggered.
     */
    String submitForm() default "";

    /**
     * Whether or not to perform form validation if the {@link #submitForm()} parameter has been set.
     * Default is false.
     * 
     * @return Whether or not to validate the form.
     */
    boolean validateForm() default false;
    
    /**
     * Controls whether or not any forms being submitted as part of this event will request focus 
     * as per normal form semantics. The default is false.
     * 
     * @return True if the form should get focus, false otherwise. The default is false.
     */
    boolean focus() default false;
    
    /**
     * If used in conjunction with {@link #submitForm()}, will either submit the form normally or
     * asynchronously. Default is asyncrhonous.
     * 
     * @return True if form should be submitted asynchronously, false otherwise.
     */
    boolean async() default true;
}
