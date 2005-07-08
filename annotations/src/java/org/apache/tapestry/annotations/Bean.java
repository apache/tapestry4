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

import org.apache.tapestry.spec.BeanLifecycle;

/**
 * Annotation used to <em>define</em> new managed beans, including limited/lightweight
 * initialization. For complex initialiation, the XML specification is necessary.
 * <p>
 * One of the advantages is that, on the XML side, it is always necessary to provide complete class
 * names; here on the Java/annotation side, we can leverage imports.
 * <p>
 * The managed bean will have a name that matches the property name; this allows such a bean to be
 * referenced via the "bean:" binding prefix, or via
 * {@link org.apache.tapestry.IComponent#getBeans()}.
 * <p>
 * This annotation adds a new {@link org.apache.tapestry.spec.IBeanSpecification} to the
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    /**
     * The Java class to instantiate. The default is Object.class; if a non-default value is
     * specified, that will be the class to instantiate; otherwise, the bean class will be
     * determined from the property type. Generally, this annotation is only used when the property
     * type is an interface (or abstract base class).
     */

    Class value() default Object.class;

    /**
     * Optional initializer string for the bean, as <em>lightweight initialization</em> (a list of
     * properties and values).
     */

    String initializer() default "";

    /**
     * The lifecycle of the bean, defaults to Lifecycle.REQUEST.
     * 
     * @see BeanLifecycle
     */

    Lifecycle lifecycle() default Lifecycle.REQUEST;
}