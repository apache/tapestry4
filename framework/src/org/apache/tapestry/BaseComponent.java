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