//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.inspector;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResourceUnavailableException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.html.Body;

/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 *
 *  <p>Because the ShowInspector component is implemented using a {@link Rollover},
 *  the containing page must use a {@link Body} component instead of
 *  a &lt;body&gt; tag.
 *
 *  Informal parameters are not allowed.  May not contain a body.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowInspector extends BaseComponent implements IDirect
{
    /**
     *  Gets the listener for the link component.
     *
     *  @since 1.0.5
     **/

    public void trigger(IRequestCycle cycle, String[] context)
        throws RequestCycleException
    {
        Inspector inspector = (Inspector) cycle.getPage("Inspector");

        inspector.inspect(getPage().getName(), cycle);
    }

    /**
     *  Renders the script, then invokes the normal implementation.
     *
     *  @since 1.0.5
     **/

    public void render(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {

        if (cycle.isRewinding())
            return;

        IScript script = null;

        IEngine engine = page.getEngine();
        IScriptSource source = engine.getScriptSource();

        try
        {
            script = source.getScript("/net/sf/tapestry/inspector/ShowInspector.script");
        }
        catch (ResourceUnavailableException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        Map symbols = new HashMap();

        IEngineService service =
            page.getEngine().getService(IEngineService.DIRECT_SERVICE);
        Gesture g = service.buildGesture(cycle, this, null);
        String URL = g.getAbsoluteURL();

        symbols.put("URL", URL);

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
        finally
        {
            symbols.clear();
        }

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(
                Tapestry.getString("ShowInspector.must-be-contained-by-body"),
                this);

        body.process(scriptSession);

        super.render(writer, cycle);

        symbols = null;
    }
}