package net.sf.tapestry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.binding.ExpressionBinding;
import net.sf.tapestry.parse.CloseToken;
import net.sf.tapestry.parse.ComponentTemplate;
import net.sf.tapestry.parse.LocalizationToken;
import net.sf.tapestry.parse.OpenToken;
import net.sf.tapestry.parse.TemplateToken;
import net.sf.tapestry.parse.TextToken;
import net.sf.tapestry.parse.TokenType;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Utility class instantiated by {@link net.sf.tapestry.BaseComponent} to
 *  process the component's {@link net.sf.tapestry.parse.ComponentTemplate template},
 *  which involves working through the nested structure of the template and hooking
 *  the various static template blocks and components together using
 *  {@link IComponent#addBody(IRender)} and 
 *  {@link net.sf.tapestry.BaseComponent#addOuter(IRender)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

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
     * 
     * 
     **/

    private static class LocalizedStringRender implements IRender
    {
        private IComponent _component;
        private String _key;
        private Map _attributes;
        private boolean _raw;

        private LocalizedStringRender(IComponent component, LocalizationToken token)
        {
            _component = component;
            _key = token.getKey();
            _raw = token.isRaw();
            _attributes = token.getAttributes();
        }

        public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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

            String value = _component.getString(_key);

            if (_raw)
                writer.printRaw(value);
            else
                writer.print(value);

            if (_attributes != null)
                writer.end();
        }

        public String toString()
        {
            ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

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
    BaseComponent loadComponent, ComponentTemplate template, IPageSource pageSource)
    {
        _requestCycle = requestCycle;
        _pageLoader = pageLoader;
        _loadComponent = loadComponent;
        _template = template;
        _pageSource = pageSource;

        _stack = new IComponent[template.getTokenCount()];
    }

    public void process() throws PageLoaderException
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
            throw new PageLoaderException(Tapestry.getString("BaseComponent.unbalance-open-tags"), _loadComponent);

        checkAllComponentsReferenced();
    }

    /**
     *  Adds the token (which implements {@link IRender})
     *  to the active component (using {@link IComponent#addBody(IRender)}),
     *  or to this component {@link #addOuter(IRender)}.
     * 
     *  <p>
     *  A check is made that the active component allows a body.
     * 
     **/

    private void process(TextToken token) throws PageLoaderException
    {
        if (_activeComponent == null)
        {
            _loadComponent.addOuter(token);
            return;
        }

        if (!_activeComponent.getSpecification().getAllowBody())
            throw new BodylessComponentException(_activeComponent);

        _activeComponent.addBody(token);
    }

    private void process(OpenToken token) throws PageLoaderException
    {
        String id = token.getId();
        IComponent component = null;
        String componentType = token.getComponentType();
        
        if (componentType == null)
            component = getEmbeddedComponent(id);
        else
            component = createImplicitComponent(id, componentType);            

        // Make sure the template contains each component only once.

        if (_seenIds.contains(id))
            throw new PageLoaderException(
                Tapestry.getString("BaseComponent.multiple-component-references", _loadComponent.getExtendedId(), id),
                _loadComponent);

        _seenIds.add(id);

        if (_activeComponent == null)
            _loadComponent.addOuter(component);
        else
        {
            // Note: this code may no longer be reachable (because the
            // template parser does this check first).

            if (!_activeComponent.getSpecification().getAllowBody())
                throw new BodylessComponentException(_activeComponent);

            _activeComponent.addBody(component);
        }

        addExpressionBindings(component, token.getExpressionValuesMap());
        addStaticBindings(component, token.getStaticValuesMap());

        _stack[_stackx++] = _activeComponent;

        _activeComponent = component;
    }

    private IComponent createImplicitComponent(String id, String componentType)
    throws PageLoaderException
    {
        return _pageLoader.createImplicitComponent(_requestCycle, _loadComponent, id, componentType);
    }

    private IComponent getEmbeddedComponent(String id) throws PageLoaderException
    {
        try
        {
            return _loadComponent.getComponent(id);
        
        }
        catch (NoSuchComponentException ex)
        {
            throw new PageLoaderException(
                Tapestry.getString("BaseComponent.undefined-embedded-component", _loadComponent.getExtendedId(), id),
                _loadComponent,
                ex);
        }
    }

    private void process(CloseToken token) throws PageLoaderException
    {
        // Again, this is pretty much impossible to reach because
        // the template parser does a great job.

        if (_stackx <= 0)
            throw new PageLoaderException(Tapestry.getString("BaseComponent.unbalanced-close-tags"), _loadComponent);

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
     *  Adds expression bindings for any expressions in the provided map.
     *
     *  <p>It is an error to specify expression 
     *  bindings in both the specification
     *  and the template.
     * 
     **/

    private void addExpressionBindings(IComponent component, Map expressionsMap) throws PageLoaderException
    {
        if (Tapestry.isEmpty(expressionsMap))
            return;

        ComponentSpecification spec = component.getSpecification();

        boolean rejectInformal = !spec.getAllowInformalParameters();

        Iterator i = expressionsMap.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();

            // If matches a formal parameter name, allow it to be set
            // unless there's already a binding.

            boolean isFormal = (spec.getParameter(name) != null);

            if (isFormal)
            {
                if (component.getBinding(name) != null)
                    throw new PageLoaderException(
                        Tapestry.getString(
                            "BaseComponent.dupe-template-expression",
                            name,
                            component.getExtendedId(),
                            _loadComponent.getExtendedId()),
                        component);
            }
            else
            {
                if (rejectInformal)
                    throw new PageLoaderException(
                        Tapestry.getString(
                            "BaseComponent.template-expression-for-informal-parameter",
                            name,
                            component.getExtendedId(),
                            _loadComponent.getExtendedId()),
                        component);

                // If the name is reserved (matches a formal parameter
                // or reserved name, caselessly), then skip it.

                if (spec.isReservedParameterName(name))
                    throw new PageLoaderException(
                        Tapestry.getString(
                            "BaseComponent.template-expression-for-reserved-parameter",
                            name,
                            component.getExtendedId(),
                            _loadComponent.getExtendedId()),
                        component);
            }

            String expression = (String) e.getValue();

            IBinding binding = new ExpressionBinding(_pageSource.getResourceResolver(), _loadComponent, expression);

            component.setBinding(name, binding);
        }

    }

    /**
     *  Adds static bindings for any attrributes specified in the HTML
     *  template, skipping any that are reserved (explicitly, or
     *  because they match a formal parameter name).
     *
     **/

    private void addStaticBindings(IComponent component, Map valuesMap)
    {
        if (Tapestry.isEmpty(valuesMap))
            return;

        ComponentSpecification spec = component.getSpecification();

        boolean rejectInformal = !spec.getAllowInformalParameters();

        Iterator i = valuesMap.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();

            // If matches a formal parameter name, allow it to be set
            // unless there's already a binding.

            boolean isFormal = (spec.getParameter(name) != null);

            if (isFormal)
            {
                if (component.getBinding(name) != null)
                    continue;
            }
            else
            {
                // Skip informal parameters if the component doesn't allow them.

                if (rejectInformal)
                    continue;

                // If the name is reserved (matches a formal parameter
                // or reserved name, caselessly), then skip it.

                if (spec.isReservedParameterName(name))
                    continue;
            }

            String value = (String) e.getValue();

            IBinding binding = _pageSource.getStaticBinding(value);

            component.setBinding(name, binding);
        }
    }

    private void checkAllComponentsReferenced() throws PageLoaderException
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
            (count == 1) ? "BaseComponent.missing-component-spec-single" : "BaseComponent.missing-component-spec-multi";

        StringBuffer buffer = new StringBuffer(Tapestry.getString(key, _loadComponent.getExtendedId()));

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
                    buffer.append(Tapestry.getString("BaseComponent.and"));
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
}
