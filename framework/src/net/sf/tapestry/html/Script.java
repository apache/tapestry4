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

package net.sf.tapestry.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *  Works with the {@link Body} component to add a script (and perhaps some initialization) 
 *  to the HTML response.
 *
 *  [<a href="../../../../../ComponentReference/Script.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Script extends AbstractComponent
{
    private static final Logger LOG = LogManager.getLogger(Script.class);

    private String _lastScriptPath;
    private String _scriptPath;
    private Map _baseSymbols;

    private IScript _script;

    /**
     *  Constructs the symbols {@link Map}.  This starts with the
     *  contents of the symbols parameter (if specified) to which is added
     *  any informal parameters.  If both a symbols parameter and informal
     *  parameters are bound, then a copy of the symbols parameter's value is made
     *  (that is, the {@link Map} provided by the symbols parameter is read, but not modified).
     *
     **/

    private Map getSymbols()
    {
        Map result = null;
        boolean copy = false;

        if (_baseSymbols != null)
        {
            result = _baseSymbols;

            // Make a writable copy if there are any informal parameters
            copy = true;
        }

        // Now, iterate through all the binding names (which includes both
        // formal and informal parmeters).  Skip the formal ones and
        // access the informal ones.

        Iterator i = getBindingNames().iterator();
        while (i.hasNext())
        {
            String bindingName = (String) i.next();

            // Skip formal parameters

            if (getSpecification().getParameter(bindingName) != null)
                continue;

            IBinding binding = getBinding(bindingName);

            Object value = binding.getObject();

            if (value == null)
                continue;

            if (result == null)
                result = new HashMap();
            else
                if (copy)
                {
                    result = new HashMap(result);
                    copy = false;
                }

            result.put(bindingName, value);
        }

        return result;
    }

    /**
     *  Gets the {@link IScript} for the correct script.
     *
     *
     **/

    private IScript getParsedScript(IRequestCycle cycle) throws RequiredParameterException
    {
        if (_script != null && _scriptPath.equals(_lastScriptPath))
            return _script;

        IEngine engine = cycle.getEngine();
        IScriptSource source = engine.getScriptSource();

        // Cache for later

        IScript script = source.getScript(_scriptPath);
        _lastScriptPath = _scriptPath;

        return script;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        ScriptSession session;

        if (cycle.isRewinding())
            return;

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(Tapestry.getString("Script.must-be-contained-by-body"), this);

        try
        {
            session = getParsedScript(cycle).execute(getSymbols());
        }
        catch (ScriptException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        body.process(session);

        // This component is not allowed to have a body.
    }

    public String getScriptPath()
    {
        return _scriptPath;
    }

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    public Map getBaseSymbols()
    {
        return _baseSymbols;
    }

    public void setBaseSymbols(Map baseSymbols)
    {
        _baseSymbols = baseSymbols;
    }

}