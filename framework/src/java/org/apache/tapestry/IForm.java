// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry;

import org.apache.tapestry.valid.IValidationDelegate;

/**
 * A generic way to access a component which defines an HTML form (or, perhaps, other similar
 * constructs, such as a WML {@link org.apache.tapestry.wml.Go}). This interface exists so that the
 * {@link IRequestCycle}can invoke the {@link #rewind(IMarkupWriter, IRequestCycle)}method (which
 * is used to deal with a Form that uses the direct service). In release 1.0.5, more responsibility
 * for forms was moved here.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.2
 */

public interface IForm extends IAction, FormBehavior
{

    /**
     * Attribute name used with the request cycle; allows other components to locate the IForm.
     * 
     * @since 1.0.5
     * @deprecated To be removed; use {@link TapestryUtils#FORM_ATTRIBUTE}instead.
     */

    public static final String ATTRIBUTE_NAME = TapestryUtils.FORM_ATTRIBUTE;

    /**
     * Invoked by the {@link IRequestCycle}to allow a form that uses the direct service, to respond
     * to the form submission.
     */

    public void rewind(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Returns the name of the form. The name is determined as the form component begins to render,
     * but is not reset (as a convienience for building client-side JavaScript event handlers).
     * 
     * @since 1.0.5
     */

    public String getName();

    /**
     * Returns the validation delegate for the form. Returns null if the form does not have a
     * delegate.
     * 
     * @since 1.0.8
     */

    public IValidationDelegate getDelegate();
    
    /**
     * Indicates whether or not client-side validation will be generated during render.
     * @return true, if client-side validation is enabled, false otherwise
     * @since 4.0
     */
    public boolean isClientValidationEnabled();
}