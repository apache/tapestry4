/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.binding;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.pool.Pool;

/**
 *  A very specialized binding that can be used as an {@link net.sf.tapestry.IActionListener},
 *  executing a script in a scripting language, via
 *  <a href="http://jakarta.apache.org/bsf">Bean Scripting Framework</a>.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ListenerBinding extends AbstractBinding implements IActionListener
{
    private static final Log LOG = LogFactory.getLog(ListenerBinding.class);

    private static final String BSF_POOL_KEY = "net.sf.tapestry.BSFManager";

    private String _language;
    private String _script;
    private String _location;
    private IComponent _component;

    public ListenerBinding(IComponent component, String language, String location, String script)
    {
        _component = component;
        _language = language;
        _location = location;
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
        throw new BindingException(Tapestry.getString("ListenerBinding.invalid-access", "getInt()"), this);
    }

    public double getDouble()
    {
        throw new BindingException(Tapestry.getString("ListenerBinding.invalid-access", "getDouble()"), this);

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
     *  A ListenerBinding is also a {@link net.sf.tapestry.IActionListener}.  It
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

    public void actionTriggered(IComponent component, IRequestCycle cycle) throws RequestCycleException
    {
        boolean debug = LOG.isDebugEnabled();

        long startTime = debug ? System.currentTimeMillis() : 0;

        BSFManager bsf = obtainBSFManager(cycle);

        try
        {
            IPage page = cycle.getPage();

            bsf.declareBean("component", _component, _component.getClass());
            bsf.declareBean("page", page, page.getClass());
            bsf.declareBean("cycle", cycle, cycle.getClass());

            bsf.exec(_language, _location, 0, 0, _script);
        }
        catch (BSFException ex)
        {
            String message = Tapestry.getString("ListenerBinding.bsf-exception", _location, ex.getMessage());

            throw new RequestCycleException(message, _component, ex);
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

                LOG.debug("Execution of \"" + _location + "\" took " + (endTime - startTime) + " millis");
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
            LOG.warn(Tapestry.getString("ListenerBinding.unable-to-undeclare-bean", ex));
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
