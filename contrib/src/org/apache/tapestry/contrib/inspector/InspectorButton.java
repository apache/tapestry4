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

package org.apache.tapestry.contrib.inspector;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;

/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 * 
 *  [<a href="../../../../../../ComponentReference/InspectorButton.html">Component Reference</a>]
 *
 *  <p>Because the InspectorButton component is implemented using a {@link org.apache.tapestry.html.Rollover},
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

    public void trigger(IRequestCycle cycle)
    {
        String name = getNamespace().constructQualifiedName("Inspector");

        Inspector inspector = (Inspector) cycle.getPage(name);

        inspector.inspect(getPage().getPageName(), cycle);
    }

    /**
     *  Renders the script, then invokes the normal implementation.
     *
     *  @since 1.0.5
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_disabled || cycle.isRewinding())
            return;

        IEngine engine = getPage().getEngine();
        IScriptSource source = engine.getScriptSource();

        IResourceLocation scriptLocation =
            getSpecification().getSpecificationLocation().getRelativeLocation(
                "InspectorButton.script");

        IScript script = source.getScript(scriptLocation);

        Map symbols = new HashMap();

        IEngineService service = engine.getService(Tapestry.DIRECT_SERVICE);
        ILink link = service.getLink(cycle, this, null);

        symbols.put("URL", link.getURL());

        Body body = Body.get(cycle);

        if (body == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("InspectorButton.must-be-contained-by-body"),
                this,
                null,
                null);

        script.execute(cycle, body, symbols);

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