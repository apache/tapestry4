/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Base implementation for most components that use an HTML template.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class BaseComponent extends AbstractComponent
{
    private static final Log LOG = LogFactory.getLog(BaseComponent.class);

    private static final int OUTER_INIT_SIZE = 5;

    private IRender[] _outer;
    private int _outerCount = 0;

    /**
     *  Adds an element as an outer element for the receiver.  Outer
     *  elements are elements that should be directly rendered by the
     *  receiver's <code>render()</code> method.  That is, they are
     *  top-level elements on the HTML template.
     *
     * 
     **/

    protected void addOuter(IRender element)
    {
        if (_outer == null)
        {
            _outer = new IRender[OUTER_INIT_SIZE];
            _outer[0] = element;

            _outerCount = 1;
            return;
        }

        // No more room?  Make the array bigger.

        if (_outerCount == _outer.length)
        {
            IRender[] newOuter;

            newOuter = new IRender[_outer.length * 2];

            System.arraycopy(_outer, 0, newOuter, 0, _outerCount);

            _outer = newOuter;
        }

        _outer[_outerCount++] = element;
    }

    /**
     *
     *  Reads the receiver's template and figures out which elements wrap which
     *  other elements.
     *
     *  <P>This is coded as a single, big, ugly method for efficiency.
     * 
     **/

    private void readTemplate(IRequestCycle cycle, IPageLoader loader)
    {
        IPageSource pageSource = loader.getEngine().getPageSource();

        if (LOG.isDebugEnabled())
            LOG.debug(this +" reading template");

        ITemplateSource source = loader.getTemplateSource();
        ComponentTemplate componentTemplate = source.getTemplate(cycle, this);

        // Most of the work is done inside the loader class. 
        // We instantiate it just to invoke process() on it.
        
        new BaseComponentTemplateLoader(cycle, loader, this, componentTemplate, pageSource).process();

        if (LOG.isDebugEnabled())
            LOG.debug(this +" finished reading template");
    }

    /**
     *   Renders the top level components contained by the receiver.
     *
     *   @since 2.0.3
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Begin render " + getExtendedId());

        for (int i = 0; i < _outerCount; i++)
            _outer[i].render(writer, cycle);

        if (LOG.isDebugEnabled())
            LOG.debug("End render " + getExtendedId());
    }

    /**
     *  Loads the template for the component, then invokes
     *  {@link AbstractComponent#finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}.
     *  Subclasses must invoke this method first,
     *  before adding any additional behavior, though its usually
     *  simpler to override {@link #finishLoad()} instead.
     *
     **/

    public void finishLoad(IRequestCycle cycle, IPageLoader loader, IComponentSpecification specification)
    {
        readTemplate(cycle, loader);

        super.finishLoad(cycle, loader, specification);
    }
}