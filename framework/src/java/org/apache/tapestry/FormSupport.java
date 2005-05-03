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

package org.apache.tapestry;

import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.Form;

/**
 * Interface for a utility object that encapsulates the majority of the
 * {@link org.apache.tapestry.form.Form}'s behavior.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface FormSupport extends FormBehavior
{

    /**
     * Invoked when the form is rendering. This should only be invoked by the {@link Form}
     * &nbsp;component.
     * 
     * @param method
     *            the HTTP method ("get" or "post")
     * @param informalParametersRenderer
     *            object that will render informal parameters
     * @param link
     *            The link to which the form will submit (encapsulating the URL and the query
     *            parameters)
     */
    public void render(String method, IRender informalParametersRenderer, ILink link);

    /**
     * Invoked to rewind the form, which renders the body of the form, allowing form element
     * components to pull data from the request and update page properties. This should only be
     * invoked by the {@link Form}&nbsp;component.
     */
    public void rewind();
}