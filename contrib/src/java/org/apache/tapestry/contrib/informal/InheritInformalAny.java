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

package org.apache.tapestry.contrib.informal;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * 
 *  A version of the Any component that inherits the informal attributes of its parent.
 *  This component has been deprecated in favour of the 'inherit-informal-parameters' 
 *  tag that indicates that a particular component must inherit the informal parameters
 *  of its parent. This tag is available in the page or component specification file.
 * 
 *  @deprecated
 *  @version $Id$
 *  @author mindbridge
 *  @since 2.2
 * 
 **/

public class InheritInformalAny extends AbstractComponent
{
    // Bindings
    private IBinding m_objElementBinding;

    public IBinding getElementBinding()
    {
        return m_objElementBinding;
    }

    public void setElementBinding(IBinding objElementBinding)
    {
        m_objElementBinding = objElementBinding;
    }

    protected void generateParentAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        String attribute;

        IComponent objParent = getContainer();
        if (objParent == null)
            return;

        IComponentSpecification specification = objParent.getSpecification();
        Map bindings = objParent.getBindings();
        if (bindings == null)
            return;

        Iterator i = bindings.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();

            // Skip over formal parameters stored in the bindings
            // Map.  We're just interested in informal parameters.

            if (specification.getParameter(name) != null)
                continue;

            IBinding binding = (IBinding) entry.getValue();

            Object value = binding.getObject();
            if (value == null)
                continue;

            if (value instanceof IAsset)
            {
                IAsset asset = (IAsset) value;

                // Get the URL of the asset and insert that.
                attribute = asset.buildURL(cycle);
            }
            else
                attribute = value.toString();

            writer.attribute(name, attribute);
        }

    }

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String strElement = m_objElementBinding.getObject().toString();

        writer.begin(strElement);
        generateParentAttributes(writer, cycle);
        renderInformalParameters(writer, cycle);

        renderBody(writer, cycle);

        writer.end();
    }

}
