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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ImageSubmit;
import org.apache.tapestry.form.Upload;

/**
 * Interface for a utility object that encapsulates the majority of the
 * {@link org.apache.tapestry.form.Form}'s behavior.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface FormSupport
{
    /**
     * Adds an additional event handler. The type determines when the handler will be invoked,
     * {@link FormEventType#SUBMIT}is most typical.
     */
    public void addEventHandler(FormEventType type, String functionName);

    /**
     * Adds a hidden field value to be stored in the form. This ensures that all of the &lt;input
     * type="hidden"&gt; (or equivalent) are grouped together, which ensures that the output HTML is
     * valid (ie. doesn't have &lt;input&gt; improperly nested with &lt;tr&gt;, etc.).
     * <p>
     * It is acceptible to add multiple hidden fields with the same name. They will be written in
     * the order they are received.
     */

    public void addHiddenValue(String name, String value);

    /**
     * Adds a hidden field value to be stored in the form. This ensures that all of the &lt;input
     * type="hidden"&gt; (or equivalent) are grouped together, which ensures that the output HTML is
     * valid (ie. doesn't have &lt;input&gt; improperly nested with &lt;tr&gt;, etc.).
     * <p>
     * It is acceptible to add multiple hidden fields with the same name. They will be written in
     * the order they are received.
     * 
     * @since 3.0
     */

    public void addHiddenValue(String name, String id, String value);

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes {@link #getElementId(IFormComponent, String)}with the component's id.
     */

    public String getElementId(IFormComponent component);

    /**
     * Constructs a unique identifier from the base id. If possible, the id is used as-is.
     * Otherwise, a unique identifier is appended to the id.
     * <p>
     * This method is provided simply so that some components (
     * {@link org.apache.tapestry.form.ImageSubmit}) have more specific control over their names.
     * <p>
     * Invokes {@link IFormComponent#setName(String)}with the result, as well as returning it.
     * 
     * @throws StaleLinkException
     *             if, when the form itself is rewinding, the element id allocated does not match
     *             the expected id (as allocated when the form rendered). This indicates that the
     *             state of the application has changed between the time the form renderred and the
     *             time it was submitted.
     */

    public String getElementId(IFormComponent component, String baseId);

    /**
     * Returns true if the form is rewinding (meaning, the form was the subject of the request
     * cycle).
     */

    public boolean isRewinding();

    /**
     * Invoked when the form is rendering.
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
     * components to pull data from the request and update page properties.
     */
    public void rewind();

    /**
     * May be invoked by a component to force the encoding type of the form to a particular value.
     * 
     * @see org.apache.tapestry.form.Upload
     * @throws ApplicationRuntimeException
     *             if the current encoding type is not null and doesn't match the provided encoding
     *             type
     */

    public void setEncodingType(String encodingType);
}