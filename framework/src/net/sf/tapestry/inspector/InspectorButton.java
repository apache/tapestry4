package net.sf.tapestry.inspector;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.html.Body;

/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 * 
 *  [<a href="../../../../../ComponentReference/InspectorButton.html">Component Reference</a>]
 *
 *  <p>Because the InspectorButton component is implemented using a {@link net.sf.tapestry.html.Rollover},
 *  the containing page must use a {@link Body} component instead of
 *  a &lt;body&gt; tag.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class InspectorButton extends BaseComponent implements IDirect
{
    private boolean _disabled = false;
    
    /**
     *  Gets the listener for the link component.
     *
     *  @since 1.0.5
     **/

    public void trigger(IRequestCycle cycle) throws RequestCycleException
    {
        Inspector inspector = (Inspector) cycle.getPage(INamespace.FRAMEWORK_NAMESPACE + ":Inspector");

        inspector.inspect(getPage().getName(), cycle);
    }

    /**
     *  Renders the script, then invokes the normal implementation.
     *
     *  @since 1.0.5
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (_disabled || cycle.isRewinding())
            return;

        IEngine engine = getPage().getEngine();
        IScriptSource source = engine.getScriptSource();

        IScript script = source.getScript("/net/sf/tapestry/inspector/InspectorButton.script");

        Map symbols = new HashMap();

        IEngineService service = engine.getService(IEngineService.DIRECT_SERVICE);
        Gesture g = service.buildGesture(cycle, this, null);

        symbols.put("URL", g.getURL());

        HttpSession session = cycle.getRequestContext().getSession();
        ScriptSession scriptSession = null;

        try
        {
            scriptSession = script.execute(symbols);
        }
        catch (ScriptException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(Tapestry.getString("InspectorButton.must-be-contained-by-body"), this);

        body.process(scriptSession);

        // Now, go render the rest from the template.
        
        super.renderComponent(writer, cycle);
    }
    
    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }
    
    /**
     *  Always returns false.
     * 
     *  @since 2.3
     * 
     **/
    
    public boolean isStateful()
    {
        return false;
    }
    

}