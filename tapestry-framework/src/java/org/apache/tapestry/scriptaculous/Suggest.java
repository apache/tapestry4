package org.apache.tapestry.scriptaculous;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.TranslatedField;
import org.apache.tapestry.form.TranslatedFieldSupport;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.link.DirectLink;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.SizeRestrictingIterator;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implementation of the <a href="http://wiki.script.aculo.us/scriptaculous/show/Ajax.Autocompleter">Ajax.Autocompleter</a> in
 * the form of a {@link org.apache.tapestry.form.TextField} like component with the additional ability to dynamically suggest
 * values via XHR requests.
 *
 * <p>
 * This component will use the html element tag name defined in your html template to include it to determine whether or not
 * to render a TextArea or TextField style input element. For example, specifying a component definition such as:
 * </p>
 *
 * <pre>&lt;input jwcid="@Suggest" value="literal:A default value" /&gt;</pre>
 *
 * <p>
 * would render something looking like:
 * </p>
 *
 * <pre>&lt;input type="text" name="suggest" id="suggest" autocomplete="off" value="literal:A default value" /&gt;</pre>
 *
 * <p>while a defintion of</p>
 *
 * <pre>&lt;textarea jwcid="@Suggest" value="literal:A default value" /&gt;</pre>
 *
 * <p>would render something like:</p>
 *
 * <pre>
 *  &lt;textarea name="suggest" id="suggest" &gt;A default value&lt;textarea/&gt;
 * </pre>
 *
 */
public abstract class Suggest extends AbstractFormComponent implements TranslatedField, IDirect {
    
    /**
     * Keys that should be treated as javascript literals when contructing the 
     * options json.
     */
    private static final String[] LITERAL_KEYS = new String[]
        {"onFailure", "updateElement", "afterUpdateElement", "callback"};


    /**
     * Injected service used to invoke whatever listeners people have setup to handle
     * changing value from this field.
     *
     * @return The invoker.
     */
    public abstract ListenerInvoker getListenerInvoker();

    /**
     * Injected response builder for doing specific XHR things.
     *
     * @return ResponseBuilder for this request. 
     */
    public abstract ResponseBuilder getResponse();

    /**
     * Associated javascript template.
     *
     * @return The script template.
     */
    public abstract IScript getScript();

    /**
     * Used to convert form input values.
     *
     * @return The value converter to use.
     */
    public abstract ValueConverter getValueConverter();

    /**
     * Injected.
     *
     * @return Service used to validate input.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * Injected.
     *
     * @return Translation service.
     */
    public abstract TranslatedFieldSupport getTranslatedFieldSupport();

    /**
     * Injected.
     *
     * @return The {@link org.apache.tapestry.engine.DirectService} engine.  
     */
    public abstract IEngineService getEngineService();

    ////////////////////////////////////////////////////////
    // Parameters
    ////////////////////////////////////////////////////////

    public abstract Object getValue();
    public abstract void setValue(Object value);

    public abstract ListItemRenderer getListItemRenderer();
    public abstract void setListItemRenderer(ListItemRenderer renderer);

    public abstract IActionListener getListener();

    public abstract Object getListSource();
    public abstract void setListSource(Object value);

    public abstract int getMaxResults();

    public abstract Object getParameters();

    public abstract String getOptions();

    public abstract String getUpdateElementClass();

    /**
     * Used internally to track listener invoked searches versus
     * normal rendering requests.
     *
     * @return True if search was triggered, false otherwise.
     */
    public abstract boolean isSearchTriggered();
    public abstract void setSearchTriggered(boolean value);

    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // render search triggered response instead of normal render if
        // listener was invoked

        IForm form = TapestryUtils.getForm(cycle, this);
        setForm(form);

        if (form.wasPrerendered(writer, this))
            return;

        if (!form.isRewinding() && !cycle.isRewinding()
            && getResponse().isDynamic() && isSearchTriggered())
        {
            setName(form);

            // do nothing if it wasn't for this instance - such as in a loop

            if (cycle.getParameter(getClientId()) == null)
                return;

            renderList(writer, cycle);
            return;
        }

        // defer to super if normal render

        super.renderComponent(writer, cycle);
    }

    /**
     * Invoked only when a search has been triggered to render out the &lt;li&gt; list of
     * dynamic suggestion options.
     *
     * @param writer
     *          The markup writer.
     * @param cycle
     *          The associated request.
     */
    public void renderList(IMarkupWriter writer, IRequestCycle cycle)
    {
        Defense.notNull(getListSource(), "listSource for Suggest component.");

        Iterator values = (Iterator)getValueConverter().coerceValue(getListSource(), Iterator.class);

        if (isParameterBound("maxResults"))
        {
            values = new SizeRestrictingIterator(values, getMaxResults());
        }

        getListItemRenderer().renderList(writer, cycle, values);
    }

    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = getTranslatedFieldSupport().format(this, getValue());
        boolean isTextArea = getTemplateTagName().equalsIgnoreCase("textarea");

        renderDelegatePrefix(writer, cycle);

        if (isTextArea)
            writer.begin(getTemplateTagName());
        else
            writer.beginEmpty(getTemplateTagName());

        // only render input attributes if not a textarea
        if (!isTextArea)
        {
            writer.attribute("type", "text");
            writer.attribute("autocomplete", "off");
        }

        renderIdAttribute(writer, cycle);
        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        renderInformalParameters(writer, cycle);
        renderDelegateAttributes(writer, cycle);

        getTranslatedFieldSupport().renderContributions(this, writer, cycle);
        getValidatableFieldSupport().renderContributions(this, writer, cycle);

        if (value != null)
        {
            if (!isTextArea)
                writer.attribute("value", value);
            else
                writer.print(value);
        }

        if (!isTextArea)
            writer.closeTag();
        else
            writer.end();

        renderDelegateSuffix(writer, cycle);

        // render update element

        writer.begin("div");
        writer.attribute("id", getClientId() + "choices");
        writer.attribute("class", getUpdateElementClass());
        writer.end();

        // render javascript

        JSONObject json = null;
        String options = getOptions();

        try {

            json = options != null ? new JSONObject(options) : new JSONObject();

        } catch (ParseException ex)
        {
            throw new ApplicationRuntimeException(ScriptaculousMessages.invalidOptions(options, ex), this.getBinding("options").getLocation(), ex);
        }

        // bind onFailure client side function if not already defined

        if (!json.has("onFailure"))
        {
            json.put("onFailure", "tapestry.error");
        }

        if (!json.has("encoding"))
        {
            json.put("encoding", cycle.getEngine().getOutputEncoding());
        }
        
        for (int i=0; i<LITERAL_KEYS.length; i++) 
        {
            String key = LITERAL_KEYS[i];
            if (json.has(key))
            {
                json.put(key, new JSONLiteral(json.getString(key)));
            }            
        }

        Map parms = new HashMap();
        parms.put("inputId", getClientId());
        parms.put("updateId", getClientId() + "choices");
        parms.put("options", json.toString());

        Object[] specifiedParams = DirectLink.constructServiceParameters(getParameters());
        Object[] listenerParams = null;
        if (specifiedParams != null)
        {
            listenerParams = new Object[specifiedParams.length + 1];
            System.arraycopy(specifiedParams, 0, listenerParams, 1, specifiedParams.length);
        } else {

            listenerParams = new Object[1];
        }

        listenerParams[0] = getClientId();

        ILink updateLink = getEngineService().getLink(isStateful(), new DirectServiceParameter(this, listenerParams));
        parms.put("updateUrl", updateLink.getURL());

        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(this, cycle, pageRenderSupport, parms);
    }

    /**
     * Rewinds the component, doing translation, validation and binding.
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        try
        {
            Object object = getTranslatedFieldSupport().parse(this, value);
            getValidatableFieldSupport().validate(this, writer, cycle, object);

            setValue(object);
        } catch (ValidatorException e)
        {
            getForm().getDelegate().recordFieldInputValue(value);
            getForm().getDelegate().record(e);
        }
    }

    /**
     * Triggers the listener. The parameters passed are the current text
     * and those specified in the parameters parameter of the component.
     * If the listener parameter is not bound, attempt to locate an implicit
     * listener named by the capitalized component id, prefixed by "do".
     */
    public void trigger(IRequestCycle cycle)
    {
        IActionListener listener = getListener();
        if (listener == null)
            listener = getContainer().getListeners().getImplicitListener(this);

        Object[] params = cycle.getListenerParameters();

        // replace the first param with the correct value
        String inputId = (String)params[0];
        params[0] = cycle.getParameter(inputId);

        cycle.setListenerParameters(params);

        setSearchTriggered(true);

        getListenerInvoker().invokeListener(listener, this, cycle);
    }

    public List getUpdateComponents()
    {
        return Arrays.asList(new Object[] { getClientId() });
    }

    public boolean isAsync()
    {
        return true;
    }

    public boolean isJson()
    {
        return false;
    }

    /**
     * Sets the default {@link ListItemRenderer} for component, to be overriden as
     * necessary by component parameters.
     */
    protected void finishLoad()
    {
        setListItemRenderer(DefaultListItemRenderer.SHARED_INSTANCE);
    }
}
