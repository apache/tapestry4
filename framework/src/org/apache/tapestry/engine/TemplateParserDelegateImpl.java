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

package org.apache.tapestry.engine;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.parse.ITemplateParserDelegate;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Basic implementation of the {@link org.apache.tapestry.parse.ITemplateParserDelegate} interface.
 *
 * @author Howard Lewis Ship
 */
public class TemplateParserDelegateImpl implements ITemplateParserDelegate
{
    private IComponent _component;
    private ComponentSpecificationResolver _resolver;
    private IRequestCycle _cycle;

    public TemplateParserDelegateImpl(IComponent component, IRequestCycle cycle)
    {
        _component = component;
        _resolver = new ComponentSpecificationResolver(cycle);
        _cycle = cycle;
    }

    public boolean getKnownComponent(String componentId)
    {
        return _component.getSpecification().getComponent(componentId) != null;
    }

    public boolean getAllowBody(String componentId, ILocation location)
    {
        IComponent embedded = _component.getComponent(componentId);

        if (embedded == null)
            throw Tapestry.createNoSuchComponentException(_component, componentId, location);

        return embedded.getSpecification().getAllowBody();
    }

    public boolean getAllowBody(String libraryId, String type, ILocation location)
    {
        INamespace namespace = _component.getNamespace();

        _resolver.resolve(_cycle, namespace, libraryId, type, location);

        IComponentSpecification spec = _resolver.getSpecification();

        return spec.getAllowBody();
    }

}