//  Copyright 2004 The Apache Software Foundation
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.binding.StaticBinding;
import org.apache.tapestry.binding.StringBinding;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.parse.AttributeType;
import org.apache.tapestry.parse.CloseToken;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.parse.LocalizationToken;
import org.apache.tapestry.parse.OpenToken;
import org.apache.tapestry.parse.TemplateAttribute;
import org.apache.tapestry.parse.TemplateToken;
import org.apache.tapestry.parse.TextToken;
import org.apache.tapestry.parse.TokenType;
import org.apache.tapestry.services.TemplateSource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 *  Utility class instantiated by {@link org.apache.tapestry.BaseComponent} to
 *  process the component's {@link org.apache.tapestry.parse.ComponentTemplate template},
 *  which involves working through the nested structure of the template and hooking
 *  the various static template blocks and components together using
 *  {@link IComponent#addBody(IRender)} and 
 *  {@link org.apache.tapestry.BaseComponent#addOuter(IRender)}.
 *
 *  @author Howard Lewis Ship
 *  @since 3.0
 */

public class BaseComponentTemplateLoader
{
    private static final Log LOG = LogFactory.getLog(BaseComponentTemplateLoader.class);

    private IPageLoader _pageLoader;
    private IRequestCycle _requestCycle;
    private BaseComponent _loadComponent;
    private IPageSource _pageSource;
    private ComponentTemplate _template;
    private IComponent[] _stack;
    private int _stackx = 0;
    private IComponent _activeComponent = null;
    private Set _seenIds = new HashSet();

    /**
     *  A class used with invisible localizations.  Constructed
     *  from a {@link TextToken}.
     */

    private static class LocalizedStringRender implements IRender
    {
        private IComponent _component;
        private String _key;
        private Map _attributes;
        private String _value;
        private boolean _raw;

        private LocalizedStringRender(IComponent component, LocalizationToken token)
        {
            _component = component;
            _key = token.getKey();
            _raw = token.isRaw();
            _attributes = token.getAttributes();
        }

        public void render(IMarkupWriter writer, IRequestCycle cycle)
        {
            if (cycle.isRewinding())
                return;

            if (_attributes != null)
            {
                writer.begin("span");

                Iterator i = _attributes.entrySet().iterator();

                while (i.hasNext())
                {
                    Map.Entry entry = (Map.Entry) i.next();
                    String attributeName = (String) entry.getKey();
                    String attributeValue = (String) entry.getValue();

                    writer.attribute(attributeName, attributeValue);
                }
            }

            if (_value == null)
                _value = _component.getMessage(_key);

            if (_raw)
                writer.printRaw(_value);
            else
                writer.print(_value);

            if (_attributes != null)
                writer.end();
        }

        public String toString()
        {
            ToStringBuilder builder = new ToStringBuilder(this);

            builder.append("component", _component);
            builder.append("key", _key);
            builder.append("raw", _raw);
            builder.append("attributes", _attributes);

            return builder.toString();
        }

    }

    public BaseComponentTemplateLoader(
        IRequestCycle requestCycle,
        IPageLoader pageLoader,
        BaseComponent loadComponent,
        ComponentTemplate template,
        IPageSource pageSource)
    {
        _requestCycle = requestCycle;
        _pageLoader = pageLoader;
        _loadComponent = loadComponent;
        _template = template;
        _pageSource = pageSource;

        _stack = new IComponent[template.getTokenCount()];
    }

    public void process()
    {
        int count = _template.getTokenCount();

        for (int i = 0; i < count; i++)
        {
            TemplateToken token = _template.getToken(i);

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
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("BaseComponent.unbalance-open-tags"),
                _loadComponent,
                null,
                null);

        checkAllComponentsReferenced();
    }

    /**
     *  Adds the token (which implements {@link IRender})
     *  to the active component (using {@link IComponent#addBody(IRender)}),
     *  or to this component {@link BaseComponent#addOuter(IRender)}.
     * 
     *  <p>
     *  A check is made that the active component allows a body.
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
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "BaseComponent.multiple-component-references",
                    _loadComponent.getExtendedId(),
                    id),
                _loadComponent,
                token.getLocation(),
                null);

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
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "BaseComponentTemplateLoader.dupe-component-id",
                    id,
                    location,
                    cc.getLocation()),
                _loadComponent,
                location,
                null);
    }

    private IComponent createImplicitComponent(String id, String componentType, Location location)
    {
        IComponent result =
            _pageLoader.createImplicitComponent(
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
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("BaseComponent.unbalanced-close-tags"),
                _loadComponent,
                token.getLocation(),
                null);

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
     *  Adds bindings based on attributes in the template.
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
                TemplateAttribute attribute = (TemplateAttribute) entry.getValue();
                AttributeType type = attribute.getType();

                if (type == AttributeType.OGNL_EXPRESSION)
                {
                    addExpressionBinding(
                        component,
                        spec,
                        name,
                        attribute.getValue(),
                        token.getLocation());
                    continue;
                }

                if (type == AttributeType.LOCALIZATION_KEY)
                {
                    addStringBinding(
                        component,
                        spec,
                        name,
                        attribute.getValue(),
                        token.getLocation());
                    continue;
                }

                if (type == AttributeType.LITERAL)
                    addStaticBinding(
                        component,
                        spec,
                        name,
                        attribute.getValue(),
                        token.getLocation());
            }
        }

        // if the component defines a templateTag parameter and 
        // there is no established binding for that parameter, 
        // add a static binding carrying the template tag  
        if (spec.getParameter(TemplateSource.TEMPLATE_TAG_PARAMETER_NAME) != null
            && component.getBinding(TemplateSource.TEMPLATE_TAG_PARAMETER_NAME) == null)
        {
            addStaticBinding(
                component,
                spec,
                TemplateSource.TEMPLATE_TAG_PARAMETER_NAME,
                token.getTag(),
                token.getLocation());
        }

    }

    /**
     *  Adds an expression binding, checking for errors related
     *  to reserved and informal parameters.
     *
     *  <p>It is an error to specify expression 
     *  bindings in both the specification
     *  and the template.
     */

    private void addExpressionBinding(
        IComponent component,
        IComponentSpecification spec,
        String name,
        String expression,
        Location location)
    {

        // If matches a formal parameter name, allow it to be set
        // unless there's already a binding.

        boolean isFormal = (spec.getParameter(name) != null);

        if (isFormal)
        {
            if (component.getBinding(name) != null)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.dupe-template-expression",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);
        }
        else
        {
            if (!spec.getAllowInformalParameters())
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.template-expression-for-informal-parameter",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);

            // If the name is reserved (matches a formal parameter
            // or reserved name, caselessly), then skip it.

            if (spec.isReservedParameterName(name))
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.template-expression-for-reserved-parameter",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);
        }

        IBinding binding =
            new ExpressionBinding(
                _pageSource.getClassResolver(),
                _loadComponent,
                expression,
                location);

        component.setBinding(name, binding);
    }

    /**
      *  Adds an expression binding, checking for errors related
      *  to reserved and informal parameters.
      *
      *  <p>It is an error to specify expression 
      *  bindings in both the specification
      *  and the template.
      */

    private void addStringBinding(
        IComponent component,
        IComponentSpecification spec,
        String name,
        String localizationKey,
        Location location)
    {
        // If matches a formal parameter name, allow it to be set
        // unless there's already a binding.

        boolean isFormal = (spec.getParameter(name) != null);

        if (isFormal)
        {
            if (component.getBinding(name) != null)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.dupe-string",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);
        }
        else
        {
            if (!spec.getAllowInformalParameters())
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.template-expression-for-informal-parameter",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);

            // If the name is reserved (matches a formal parameter
            // or reserved name, caselessly), then skip it.

            if (spec.isReservedParameterName(name))
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "BaseComponent.template-expression-for-reserved-parameter",
                        name,
                        component.getExtendedId(),
                        _loadComponent.getExtendedId()),
                    component,
                    location,
                    null);
        }

        IBinding binding = new StringBinding(_loadComponent, localizationKey, location);

        component.setBinding(name, binding);
    }

    /**
     *  Adds a static binding, checking for errors related
     *  to reserved and informal parameters.
     * 
     *  <p>
     *  Static bindings that conflict with bindings in the
     *  specification are quietly ignored.
     */

    private void addStaticBinding(
        IComponent component,
        IComponentSpecification spec,
        String name,
        String staticValue,
        Location location)
    {

        if (component.getBinding(name) != null)
            return;

        // If matches a formal parameter name, allow it to be set
        // unless there's already a binding.

        boolean isFormal = (spec.getParameter(name) != null);

        if (!isFormal)
        {
            // Skip informal parameters if the component doesn't allow them.

            if (!spec.getAllowInformalParameters())
                return;

            // If the name is reserved (matches a formal parameter
            // or reserved name, caselessly), then skip it.

            if (spec.isReservedParameterName(name))
                return;
        }

        IBinding binding = new StaticBinding(staticValue, location);

        component.setBinding(name, binding);
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

        // Create a modifiable copy.  Remove the ids that are referenced in
        // the template.  The remainder are worthy of note.

        ids = new HashSet(ids);
        ids.removeAll(_seenIds);

        int count = ids.size();

        String key =
            (count == 1)
                ? "BaseComponent.missing-component-spec-single"
                : "BaseComponent.missing-component-spec-multi";

        StringBuffer buffer =
            new StringBuffer(Tapestry.format(key, _loadComponent.getExtendedId()));

        Iterator i = ids.iterator();
        int j = 1;

        while (i.hasNext())
        {
            if (j == 1)
                buffer.append(' ');
            else
                if (j == count)
                {
                    buffer.append(' ');
                    buffer.append(Tapestry.getMessage("BaseComponent.and"));
                    buffer.append(' ');
                }
                else
                    buffer.append(", ");

            buffer.append(i.next());

            j++;
        }

        buffer.append('.');

        LOG.error(buffer.toString());
    }

    protected ApplicationRuntimeException createBodylessComponentException(IComponent component)
    {
        return new ApplicationRuntimeException(
            Tapestry.getMessage("BaseComponentTemplateLoader.bodyless-component"),
            component,
            null,
            null);
    }
}
