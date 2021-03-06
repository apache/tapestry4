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
import org.apache.hivemind.Location;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.valid.ValidationConstants;

/**
 * Common interface extended by {@link org.apache.tapestry.IForm}&nbsp;and
 * {@link org.apache.tapestry.form.FormSupport}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface FormBehavior
{
    /**
     * Adds an additional event handler. The type determines when the handler will be invoked,
     * {@link FormEventType#SUBMIT}is most typical.
     *
     * @param type
     *          Type of event to add.
     * @param functionName
     *          Name of the javascript function being added.
     * 
     * @deprecated Wiring of form event handlers is now managed on the client side. This method may
     *             be removed in Tapestry 4.1.2.
     */
    void addEventHandler(FormEventType type, String functionName);

    /**
     * Adds a hidden field value to be stored in the form. This ensures that all of the &lt;input
     * type="hidden"&gt; (or equivalent) are grouped together, which ensures that the output HTML is
     * valid (ie. doesn't have &lt;input&gt; improperly nested with &lt;tr&gt;, etc.).
     * <p>
     * It is acceptible to add multiple hidden fields with the same name. They will be written in
     * the order they are received.
     *
     * @param name
     *          The name of the hidden input.
     * @param value
     *          The value to store in the hidden field.
     */

    void addHiddenValue(String name, String value);

    /**
     * Adds a hidden field value to be stored in the form. This ensures that all of the &lt;input
     * type="hidden"&gt; (or equivalent) are grouped together, which ensures that the output HTML is
     * valid (ie. doesn't have &lt;input&gt; improperly nested with &lt;tr&gt;, etc.).
     * <p>
     * It is acceptible to add multiple hidden fields with the same name. They will be written in
     * the order they are received.
     *
     * @param name
     *          The name of the hidden input.
     * @param id
     *          The id of the hidden input - should almost always be the same as the name.
     * @param value
     *          The value to store in the hidden field.
     * @since 3.0
     */

    void addHiddenValue(String name, String id, String value);

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes {@link #getElementId(IFormComponent, String)}with the component's id.
     * <p>
     *
     * <p>Note: yes, some confusion on naming here. This is the form element id, which should be (for
     * Tapestry purposes) unique within the rendered form. The {@link IFormComponent#getClientId()}
     * is different, and should be unique within the rendered page.
     * </p>
     *
     * @param component
     *          The component to get the unique id of.
     * @return The unique id for this component, to be used in rendering name="id" type elements.
     *
     */

    String getElementId(IFormComponent component);

    /**
     * Constructs a unique identifier from the base id. If possible, the id is used as-is.
     * Otherwise, a unique identifier is appended to the id.
     * <p>
     * This method is provided simply so that some components (
     * {@link org.apache.tapestry.form.ImageSubmit}) have more specific control over their names.
     * <p>
     * Invokes {@link IFormComponent#setName(String)}with the result, as well as returning it.
     *
     * @param component
     *          The component to generate an element id for.
     * @param baseId
     *          The basic id of the component.
     * @return The form specific unique identifier for the given element.  May be the same as the baseId
     *          if this is the first render of that specific component.
     * @throws StaleLinkException
     *             if, when the form itself is rewinding, the element id allocated does not match
     *             the expected id (as allocated when the form rendered). This indicates that the
     *             state of the application has changed between the time the form renderred and the
     *             time it was submitted.
     */

    String getElementId(IFormComponent component, String baseId);
    
    /**
     * Used internally to "peek" at what the next generated client id will be for the 
     * given component when it renders. Similar to the logic found in {@link IRequestCycle#peekUniqueId(String)}.
     *
     * @param component
     *          The component to determine the next client id for.
     * 
     * @return The next possible client ID for the component.
     */
    String peekClientId(IFormComponent component);
    
    /**
     * Returns true if the form is rewinding (meaning, the form was the subject of the request
     * cycle).
     *
     * @return True if the form is rewinding, false otherwise.
     */

    boolean isRewinding();

    /**
     * May be invoked by a component to force the encoding type of the form to a particular value.
     *
     * @param encodingType
     *          The encoding type to set.
     * @see org.apache.tapestry.form.Upload
     * @throws ApplicationRuntimeException
     *             if the current encoding type is not null and doesn't match the provided encoding
     *             type
     */

    void setEncodingType(String encodingType);

    /**
     * Pre-renders the specified field, buffering the result for later use by
     * {@link #wasPrerendered(IMarkupWriter, IComponent)}. Typically, it is a
     * {@link org.apache.tapestry.valid.FieldLabel}&nbsp;component that pre-renders an associated
     * field. This little dance is necessary to properly support field labels inside loops, and to
     * handle the portlet action/render request cycle.
     * 
     * @param writer
     *            the markup writer (from which a nested markup writer is obtained)
     * @param field
     *            the field to pre-render. The field is responsible for invoking
     *            {@link #wasPrerendered(IMarkupWriter, IComponent)}.
     * @param location
     *            an optional location (of the FieldLabel component) used when reporting errors.
     */
    void prerenderField(IMarkupWriter writer, IComponent field, Location location);

    /**
     * Invoked by a form control component (a field) that may have been pre-rendered. If the field
     * was pre-rendered, then the buffered output is printed into the writer and true is returned.
     * Otherwise, false is returned.
     *
     * @param writer
     *          The markup writer to render with. (may be ignored during dynamic requests)
     * @param field
     *          The component to check for pre-rendering.
     * 
     * @return true if the field was pre-rendered and should do nothing during its render phase,
     *         false if the field should continue as normal.
     */
    boolean wasPrerendered(IMarkupWriter writer, IComponent field);

    /**
     * Invoked to check if a particular component has been pre-rendered.
     *
     * @param field
     *          The component to check for pre-rendering. (Such as is done by {@link org.apache.tapestry.valid.FieldLabel}.
     * 
     * @return True if the component was pre-rendered, false otherwise.
     */
    boolean wasPrerendered(IComponent field);

    /**
     * Adds a deferred runnable, an object to be executed either before the &lt;/form&gt; tag is
     * rendered (when rendering), or before the form's listener is invoked (when rewinding).
     * Runnables are executed in the order in which they are added.
     * 
     * @param runnable
     *            the object to execute (which may not be null)
     */

    void addDeferredRunnable(Runnable runnable);

    /**
     * Registers a field for automatic focus. The goal is for the first field that is in error to
     * get focus; failing that, the first required field; failing that, any field.
     * 
     * @param field
     *            the field requesting focus
     * @param priority
     *            a priority level used to determine whether the registered field becomes the focus
     *            field. Constants for this purpose are defined in {@link ValidationConstants}.
     * @since 4.0
     */

    void registerForFocus(IFormComponent field, int priority);

    /**
     * The javascript object profile being built by this context to validate/translate
     * form values.
     * @return {@link JSONObject} profile.
     */
    JSONObject getProfile();
    
    /**
     * Sets a flag denoting whether or not an {@link IFormComponent} field has been
     * updated according to the logic defined in 
     * {@link org.apache.tapestry.services.ResponseBuilder#updateComponent(String)}.
     * 
     * <p>
     * Currently this flag is used during ajax/json responses so that cooperating 
     * {@link ResponseBuilder}s can be worked with to ensure form state is properly
     * updated on the client. Specifically, that the hidden form input fields and 
     * any associated validation profiles are updated.
     * </p>
     * 
     * @param value 
     *          The value to set.
     */
    void setFormFieldUpdating(boolean value);
    
    /**
     * Checks to see if a form field has been updated. 
     * 
     * @see #setFormFieldUpdating(boolean)
     * @return True if any form field was updated.
     */
    boolean isFormFieldUpdating();
}
