// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ITemplateComponent;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.binding.LiteralBinding;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.parse.CloseToken;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.parse.LocalizationToken;
import org.apache.tapestry.parse.OpenToken;
import org.apache.tapestry.parse.TemplateToken;
import org.apache.tapestry.parse.TextToken;
import org.apache.tapestry.parse.TokenType;
import org.apache.tapestry.services.BindingSource;
import org.apache.tapestry.services.TemplateSource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Contains the logic from {@link org.apache.tapestry.services.impl.ComponentTemplateLoaderImpl},
 * which creates one of these instances to process the request. This is necessary because the
 * service must be re-entrant (because templates can contain components that have templates).
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ComponentTemplateLoaderLogic
{
    private Log _log;

    private IPageLoader _pageLoader;

    private IRequestCycle _requestCycle;

    private ITemplateComponent _loadComponent;

    private BindingSource _bindingSource;

    private IComponent[] _stack;

    private int _stackx;

    private IComponent _activeComponent = null;

    private Set _seenIds = new HashSet();

    public ComponentTemplateLoaderLogic(Log log, IPageLoader pageLoader, BindingSource bindingSource)
    {
        _log = log;
        _pageLoader = pageLoader;
        _bindingSource = bindingSource;
    }

    public void loadTemplate(IRequestCycle requestCycle, ITemplateComponent loadComponent,
            ComponentTemplate template)
    {
        _requestCycle = requestCycle;
        _loadComponent = loadComponent;

        process(template);
    }

    private void process(ComponentTemplate template)
    {
        int count = template.getTokenCount();

        _stack = new IComponent[count];

        for (int i = 0; i < count; i++)
        {
            TemplateToken token = template.getToken(i);

            TokenType type = token.getType();

            if (type == TokenType.TEXT)
            {
                process((TextToken) token);
                continue;
            }

            if (type == TokenType.OPEN)
            {
                process((OpenToken) token);
                continue;
            }

            if (type == TokenType.CLOSE)
            {
                process((CloseToken) token);
                continue;
            }

            if (type == TokenType.LOCALIZATION)
            {
                process((LocalizationToken) token);
                continue;
            }
        }

        // This is also pretty much unreachable, and the message is kind of out
        // of date, too.

        if (_stackx != 0)
            throw new ApplicationRuntimeException(Tapestry
                    .getMessage("BaseComponent.unbalance-open-tags"), _loadComponent, null, null);

        checkAllComponentsReferenced();
    }

    /**
     * Adds the token (which implements {@link IRender}) to the active component (using
     * {@link IComponent#addBody(IRender)}), or to this component
     * {@link BaseComponent#addOuter(IRender)}.
     * <p>
     * A check is made that the active component allows a body.
     */

    private void process(TextToken token)
    {
        if (_activeComponent == null)
        {
            _loadComponent.addOuter(token);
            return;
        }

        if (!_activeComponent.getSpecification().getAllowBody())
            throw createBodylessComponentException(_activeComponent);

        _activeComponent.addBody(token);
    }

    private void process(OpenToken token)
    {
        String id = token.getId();
        IComponent component = null;
        String componentType = token.getComponentType();

        if (componentType == null)
            component = getEmbeddedComponent(id);
        else
        {
            checkForDuplicateId(id, token.getLocation());

            component = createImplicitComponent(id, componentType, token.getLocation());
        }

        // Make sure the template contains each component only once.

        if (_seenIds.contains(id))
            throw new ApplicationRuntimeException(ImplMessages.multipleComponentReferences(
                    _loadComponent,
                    id), _loadComponent, token.getLocation(), null);

        _seenIds.add(id);

        if (_activeComponent == null)
            _loadComponent.addOuter(component);
        else
        {
            // Note: this code may no longer be reachable (because the
            // template parser does this check first).

            if (!_activeComponent.getSpecification().getAllowBody())
                throw createBodylessComponentException(_activeComponent);

            _activeComponent.addBody(component);
        }

        addTemplateBindings(component, token);

        _stack[_stackx++] = _activeComponent;

        _activeComponent = component;
    }

    private void checkForDuplicateId(String id, Location location)
    {
        if (id == null)
            return;

        IContainedComponent cc = _loadComponent.getSpecification().getComponent(id);

        if (cc != null)
            throw new ApplicationRuntimeException(ImplMessages.dupeComponentId(id, cc),
                    _loadComponent, location, null);
    }

    private IComponent createImplicitComponent(String id, String componentType, Location location)
    {
        IComponent result = _pageLoader.createImplicitComponent(
                _requestCycle,
                _loadComponent,
                id,
                componentType,
                location);

        return result;
    }

    private IComponent getEmbeddedComponent(String id)
    {
        return _loadComponent.getComponent(id);
    }

    private void process(CloseToken token)
    {
        // Again, this is pretty much impossible to reach because
        // the template parser does a great job.

        if (_stackx <= 0)
            throw new ApplicationRuntimeException(ImplMessages.unbalancedCloseTags(),
                    _loadComponent, token.getLocation(), null);

        // Null and forget the top element on the stack.

        _stack[_stackx--] = null;

        _activeComponent = _stack[_stackx];
    }

    private void process(LocalizationToken token)
    {
        IRender render = new LocalizedStringRender(_loadComponent, token);

        if (_activeComponent == null)
            _loadComponent.addOuter(render);
        else
            _activeComponent.addBody(render);
    }

    /**
     * Adds bindings based on attributes in the template.
     */

    private void addTemplateBindings(IComponent component, OpenToken token)
    {
        IComponentSpecification spec = component.getSpecification();

        Map attributes = token.getAttributesMap();

        if (attributes != null)
        {
            Iterator i = attributes.entrySet().iterator();

            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry) i.next();

                String name = (String) entry.getKey();
                String value = (String) entry.getValue();

                String description = ImplMessages.templateParameterName(name);

                IBinding binding = _bindingSource.createBinding(
                        _loadComponent,
                        description,
                        value,
                        token.getLocation());

                addBinding(component, spec, name, binding);
            }
        }

        // if the component defines a templateTag parameter and
        // there is no established binding for that parameter,
        // add a static binding carrying the template tag

        if (spec.getParameter(TemplateSource.TEMPLATE_TAG_PARAMETER_NAME) != null
                && component.getBinding(TemplateSource.TEMPLATE_TAG_PARAMETER_NAME) == null)
        {
            IBinding binding = _bindingSource.createBinding(
                    component,
                    TemplateSource.TEMPLATE_TAG_PARAMETER_NAME,
                    token.getTag(),
                    token.getLocation());

            addBinding(component, spec, TemplateSource.TEMPLATE_TAG_PARAMETER_NAME, binding);
        }
    }

    /**
     * Adds an expression binding, checking for errors related to reserved and informal parameters.
     * <p>
     * It is an error to specify expression bindings in both the specification and the template.
     */

    private void addBinding(IComponent component, IComponentSpecification spec, String name,
            IBinding binding)
    {

        // If matches a formal parameter name, allow it to be set
        // unless there's already a binding.

        boolean valid = validate(component, spec, name, binding);

        if (valid)
            component.setBinding(name, binding);
    }

    private boolean validate(IComponent component, IComponentSpecification spec, String name,
            IBinding binding)
    {
        // TODO: This is ugly! Need a better/smarter way, even if we have to extend BindingSource
        // to tell us.

        boolean literal = binding instanceof LiteralBinding;

        boolean isFormal = (spec.getParameter(name) != null);

        if (isFormal)
        {
            if (component.getBinding(name) != null)
            {
                // Literal bindings in the template that conflict with bound parameters
                // from the spec are silently ignored.

                if (literal)
                    return false;

                throw new ApplicationRuntimeException(ImplMessages.dupeTemplateBinding(
                        name,
                        component,
                        _loadComponent), component, binding.getLocation(), null);
            }

            return true;
        }

        if (!spec.getAllowInformalParameters())
        {
            // Again; if informal parameters are disallowed, ignore literal bindings, as they
            // are there as placeholders or for WYSIWYG.

            if (literal)
                return false;

            throw new ApplicationRuntimeException(ImplMessages.templateBindingForInformalParameter(
                    _loadComponent,
                    name,
                    component), component, binding.getLocation(), null);
        }

        // If the name is reserved (matches a formal parameter
        // or reserved name, caselessly), then skip it.

        if (spec.isReservedParameterName(name))
        {
            // Final case for literals: if they conflict with a reserved name, they are ignored.
            // Again, there for WYSIWYG.

            if (literal)
                return false;

            throw new ApplicationRuntimeException(ImplMessages.templateBindingForReservedParameter(
                    _loadComponent,
                    name,
                    component), component, binding.getLocation(), null);
        }

        return true;
    }

    private void checkAllComponentsReferenced()
    {
        // First, contruct a modifiable copy of the ids of all expected components
        // (that is, components declared in the specification).

        Map components = _loadComponent.getComponents();

        Set ids = components.keySet();

        // If the seen ids ... ids referenced in the template, matches
        // all the ids in the specification then we're fine.

        if (_seenIds.containsAll(ids))
            return;

        // Create a modifiable copy. Remove the ids that are referenced in
        // the template. The remainder are worthy of note.

        ids = new HashSet(ids);
        ids.removeAll(_seenIds);

        _log.error(ImplMessages.missingComponentSpec(_loadComponent, ids));

    }

    private ApplicationRuntimeException createBodylessComponentException(IComponent component)
    {
        return new ApplicationRuntimeException(ImplMessages.bodylessComponent(), component, null,
                null);
    }
}