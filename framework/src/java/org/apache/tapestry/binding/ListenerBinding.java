// Copyright 2004, 2005 The Apache Software Foundation
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
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.BSFManagerFactory;

/**
 * A very specialized binding that can be used as an {@link org.apache.tapestry.IActionListener},
 * executing a script in a scripting language, via <a href="http://jakarta.apache.org/bsf">Bean
 * Scripting Framework </a>.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class ListenerBinding extends AbstractBinding implements IActionListener
{
    private final String _language;

    private final String _script;

    private final IComponent _component;

    /** @since 3.1 */

    private BSFManagerFactory _managerFactory;

    public ListenerBinding(IComponent component, String description, String language,
            String script, BSFManagerFactory managerFactory, ValueConverter valueConverter,
            Location location)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(language, "language");
        Defense.notNull(script, "script");

        _component = component;
        _language = language;
        _script = script;
        _managerFactory = managerFactory;
    }

    /**
     * Returns this.
     */

    public Object getObject()
    {
        return this;
    }

    /**
     * A ListenerBinding is also a {@link org.apache.tapestry.IActionListener}. It registers a
     * number of beans with the BSF manager and invokes the script.
     * <p>
     * Registers the following bean:
     * <ul>
     * <li>component - the relevant {@link IComponent}, typically the same as the page
     * <li>page - the {@link IPage}trigged by the request (obtained by
     * {@link IRequestCycle#getPage()}
     * <li>cycle - the {@link IRequestCycle}, from which can be found the {@link IEngine}, etc.
     * </ul>
     */

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    {
        BSFManager bsf = _managerFactory.createBSFManager();

        Location location = getLocation();

        try
        {
            IPage page = cycle.getPage();

            bsf.declareBean("component", _component, _component.getClass());
            bsf.declareBean("page", page, page.getClass());
            bsf.declareBean("cycle", cycle, cycle.getClass());

            bsf.exec(
                    _language,
                    location.getResource().toString(),
                    location.getLineNumber(),
                    location.getLineNumber(),
                    _script);
        }
        catch (BSFException ex)
        {
            String message = Tapestry.format("ListenerBinding.bsf-exception", location, ex
                    .getMessage());

            throw new ApplicationRuntimeException(message, _component, getLocation(), ex);
        }
    }

    /** @since 3.1 */

    public Object getComponent()
    {
        return _component;
    }
}