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

package org.apache.tapestry.binding;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.pool.Pool;

/**
 *  A very specialized binding that can be used as an {@link org.apache.tapestry.IActionListener},
 *  executing a script in a scripting language, via
 *  <a href="http://jakarta.apache.org/bsf">Bean Scripting Framework</a>.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ListenerBinding extends AbstractBinding implements IActionListener
{
    private static final Log LOG = LogFactory.getLog(ListenerBinding.class);

    private static final String BSF_POOL_KEY = "org.apache.tapestry.BSFManager";

    private String _language;
    private String _script;
    private IComponent _component;

    public ListenerBinding(IComponent component, String language, String script, ILocation location)
    {
        super(location);

        _component = component;
        _language = language;
        _script = script;
    }

    /**
     *  Always returns true.
     * 
     **/

    public boolean getBoolean()
    {
        return true;
    }

    public int getInt()
    {
        throw new BindingException(
            Tapestry.format("ListenerBinding.invalid-access", "getInt()"),
            this);
    }

    public double getDouble()
    {
        throw new BindingException(
            Tapestry.format("ListenerBinding.invalid-access", "getDouble()"),
            this);

    }

    /**
     *  Returns the underlying script.
     * 
     **/

    public String getString()
    {
        return _script;
    }

    /**
     *  Returns this.
     * 
     **/

    public Object getObject()
    {
        return this;
    }

    /**
     *  A ListenerBinding is also a {@link org.apache.tapestry.IActionListener}.  It
     *  registers a number of beans with the BSF manager and invokes the
     *  script.
     * 
     *  <p>
     *  Registers the following bean:
     *  <ul>
     *  <li>component - the relevant {@link IComponent}, typically the same as the page 
     *  <li>page - the {@link IPage} trigged by the request (obtained by {@link IRequestCycle#getPage()}
     *  <li>cycle - the {@link IRequestCycle}, from which can be found
     *  the {@link IEngine}, etc.
     *  </ul>
     * 
     **/

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    {
        boolean debug = LOG.isDebugEnabled();

        long startTime = debug ? System.currentTimeMillis() : 0;

        BSFManager bsf = obtainBSFManager(cycle);

        ILocation location = getLocation();

        try
        {
            IPage page = cycle.getPage();

            bsf.declareBean("component", _component, _component.getClass());
            bsf.declareBean("page", page, page.getClass());
            bsf.declareBean("cycle", cycle, cycle.getClass());

            bsf.exec(
                _language,
                location.getResourceLocation().toString(),
                location.getLineNumber(),
                location.getLineNumber(),
                _script);
        }
        catch (BSFException ex)
        {
            String message =
                Tapestry.format("ListenerBinding.bsf-exception", location, ex.getMessage());

            throw new ApplicationRuntimeException(message, _component, getLocation(), ex);
        }
        finally
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Cleaning up " + bsf);

            undeclare(bsf, "component");
            undeclare(bsf, "page");
            undeclare(bsf, "cycle");

            cycle.getEngine().getPool().store(BSF_POOL_KEY, bsf);

            if (debug)
            {
                long endTime = System.currentTimeMillis();

                LOG.debug(
                    "Execution of \"" + location + "\" took " + (endTime - startTime) + " millis");
            }
        }
    }

    private void undeclare(BSFManager bsf, String name)
    {
        try
        {
            bsf.undeclareBean(name);
        }
        catch (BSFException ex)
        {
            LOG.warn(Tapestry.format("ListenerBinding.unable-to-undeclare-bean", ex));
        }
    }

    private BSFManager obtainBSFManager(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();
        Pool pool = engine.getPool();

        BSFManager result = (BSFManager) pool.retrieve(BSF_POOL_KEY);

        if (result == null)
        {
            LOG.debug("Creating new BSFManager instance.");

            result = new BSFManager();

            result.setClassLoader(engine.getResourceResolver().getClassLoader());
        }

        return result;
    }
}
