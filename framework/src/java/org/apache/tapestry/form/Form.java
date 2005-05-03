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

package org.apache.tapestry.form;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.FormSupport;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ActionServiceParameter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.web.WebResponse;

/**
 * Component which contains form element components. Forms use the action or direct services to
 * handle the form submission. A Form will wrap other components and static HTML, including form
 * components such as {@link TextArea},{@link TextField},{@link Checkbox}, etc. [ <a
 * href="../../../../../ComponentReference/Form.html">Component Reference </a>]
 * <p>
 * When a form is submitted, it continues through the rewind cycle until <em>after</em> all of its
 * wrapped elements have renderred. As the form component render (in the rewind cycle), they will be
 * updating properties of the containing page and notifying thier listeners. Again: each form
 * component is responsible not only for rendering HTML (to present the form), but for handling it's
 * share of the form submission.
 * <p>
 * Only after all that is done will the Form notify its listener.
 * <p>
 * Starting in release 1.0.2, a Form can use either the direct service or the action service. The
 * default is the direct service, even though in earlier releases, only the action service was
 * available.
 * 
 * @author Howard Lewis Ship, David Solis
 */

public abstract class Form extends AbstractComponent implements IForm, IDirect
{
    private String _name;

    private FormSupport _formSupport;

    private class RenderInformalParameters implements IRender
    {
        public void render(IMarkupWriter writer, IRequestCycle cycle)
        {
            renderInformalParameters(writer, cycle);
        }
    }

    private IRender _renderInformalParameters;

    /**
     * Returns the currently active {@link IForm}, or null if no form is active. This is a
     * convienience method, the result will be null, or an instance of {@link IForm}, but not
     * necessarily a <code>Form</code>.
     * 
     * @deprecated Use {@link TapestryUtils#getForm(IRequestCycle, IComponent)}&nbsp;instead.
     */

    public static IForm get(IRequestCycle cycle)
    {
        return (IForm) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    /**
     * Indicates to any wrapped form components that they should respond to the form submission.
     * 
     * @throws ApplicationRuntimeException
     *             if not rendering.
     */

    public boolean isRewinding()
    {
        if (!isRendering())
            throw Tapestry.createRenderOnlyPropertyException(this, "rewinding");

        return _formSupport.isRewinding();
    }

    /**
     * Injected.
     * 
     * @since 4.0
     */

    public abstract IEngineService getDirectService();

    /**
     * Injected.
     * 
     * @since 4.0
     */

    public abstract IEngineService getActionService();

    /**
     * Returns true if this Form is configured to use the direct service.
     * <p>
     * This is derived from the direct parameter, and defaults to true if not bound.
     * 
     * @since 1.0.2
     */

    public abstract boolean isDirect();

    /**
     * Returns true if the stateful parameter is bound to a true value. If stateful is not bound,
     * also returns the default, true.
     * 
     * @since 1.0.1
     */

    public boolean getRequiresSession()
    {
        return isStateful();
    }

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes
     * {@link #getElementId(org.apache.tapestry.form.IFormComponent, java.lang.String)}with the
     * component's id.
     * 
     * @since 1.0.2
     */

    public String getElementId(IFormComponent component)
    {
        return _formSupport.getElementId(component, component.getId());
    }

    /**
     * Constructs a unique identifier from the base id. If possible, the id is used as-is.
     * Otherwise, a unique identifier is appended to the id.
     * <p>
     * This method is provided simply so that some components ({@link ImageSubmit}) have more
     * specific control over their names.
     * 
     * @since 1.0.3
     */

    public String getElementId(IFormComponent component, String baseId)
    {
        return _formSupport.getElementId(component, baseId);
    }

    /**
     * Returns the name generated for the form. This is used to faciliate components that write
     * JavaScript and need to access the form or its contents.
     * <p>
     * This value is generated when the form renders, and is not cleared. If the Form is inside a
     * {@link org.apache.tapestry.components.Foreach}, this will be the most recently generated
     * name for the Form.
     * <p>
     * This property is exposed so that sophisticated applications can write JavaScript handlers for
     * the form and components within the form.
     * 
     * @see AbstractFormComponent#getName()
     */

    public String getName()
    {
        return _name;
    }

    /** @since 3.0 * */

    protected void prepareForRender(IRequestCycle cycle)
    {
        super.prepareForRender(cycle);

        TapestryUtils.storeForm(cycle, this);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _formSupport = null;

        TapestryUtils.removeForm(cycle);

        IValidationDelegate delegate = getDelegate();

        if (delegate != null)
            delegate.setFormComponent(null);

        super.cleanupAfterRender(cycle);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String actionId = cycle.getNextActionId();

        _formSupport = newFormSupport(writer, cycle);

        if (isRewinding())
        {
            _formSupport.rewind();

            IActionListener listener = getListener();

            if (listener != null)
                listener.actionTriggered(this, cycle);

            // Abort the rewind render.

            throw new RenderRewoundException(this);
        }

        // Note: not safe to invoke getNamespace() in Portlet world
        // except during a RenderRequest.

        String baseName = isDirect() ? constructFormNameForDirectService(cycle)
                : constructFormNameForActionService(actionId);

        _name = baseName + getResponse().getNamespace();

        if (_renderInformalParameters == null)
            _renderInformalParameters = new RenderInformalParameters();

        ILink link = getLink(cycle, actionId);

        _formSupport.render(getMethod(), _renderInformalParameters, link);
    }

    /**
     * Construct a form name for use with the action service. This implementation returns "Form"
     * appended with the actionId.
     * 
     * @since 4.0
     */

    protected String constructFormNameForActionService(String actionId)
    {
        return "Form" + actionId;
    }

    /**
     * Constructs a form name for use with the direct service. This implementation bases the form
     * name on the form component's id (but ensures it is unique). Remember that Tapestry assigns an
     * "ugly" id if an explicit component id is not provided.
     * 
     * @since 4.0
     */

    private String constructFormNameForDirectService(IRequestCycle cycle)
    {
        return cycle.getUniqueId(getId());
    }

    /**
     * Returns a new instance of {@link FormSupportImpl}.
     */

    protected FormSupport newFormSupport(IMarkupWriter writer, IRequestCycle cycle)
    {
        return new FormSupportImpl(writer, cycle, this);
    }

    /**
     * Adds an additional event handler.
     * 
     * @since 1.0.2
     */

    public void addEventHandler(FormEventType type, String functionName)
    {
        _formSupport.addEventHandler(type, functionName);
    }

    /**
     * Simply invokes {@link #render(IMarkupWriter, IRequestCycle)}.
     * 
     * @since 1.0.2
     */

    public void rewind(IMarkupWriter writer, IRequestCycle cycle)
    {
        render(writer, cycle);
    }

    /**
     * Method invoked by the direct service.
     * 
     * @since 1.0.2
     */

    public void trigger(IRequestCycle cycle)
    {
        cycle.rewindForm(this);
    }

    /**
     * Builds the EngineServiceLink for the form, using either the direct or action service.
     * 
     * @since 1.0.3
     */

    private ILink getLink(IRequestCycle cycle, String actionId)
    {
        if (isDirect())
        {
            Object parameter = new DirectServiceParameter(this);
            return getDirectService().getLink(cycle, parameter);
        }

        // I'd love to pull out support for the action service entirely!

        Object parameter = new ActionServiceParameter(this, actionId);

        return getActionService().getLink(cycle, parameter);
    }

    /** Injected */

    public abstract WebResponse getResponse();

    /**
     * delegate parameter, which has a default (starting in release 4.0).
     */

    public abstract IValidationDelegate getDelegate();

    /** listener parameter, may be null */
    public abstract IActionListener getListener();

    /** method parameter */
    public abstract String getMethod();

    /** stateful parameter */
    public abstract boolean isStateful();

    public void setEncodingType(String encodingType)
    {
        _formSupport.setEncodingType(encodingType);
    }

    /** @since 3.0 */

    public void addHiddenValue(String name, String value)
    {
        _formSupport.addHiddenValue(name, value);
    }

    /** @since 3.0 */

    public void addHiddenValue(String name, String id, String value)
    {
        _formSupport.addHiddenValue(name, id, value);
    }

    public void prerenderField(IMarkupWriter writer, IComponent field, Location location)
    {
        _formSupport.prerenderField(writer, field, location);
    }

    public boolean wasPrerendered(IMarkupWriter writer, IComponent field)
    {
        return _formSupport.wasPrerendered(writer, field);
    }
}