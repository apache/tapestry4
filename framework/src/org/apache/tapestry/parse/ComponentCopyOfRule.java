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

package org.apache.tapestry.parse;

import java.util.Iterator;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  A rule for processing the copy-of attribute
 *  of the &lt;component&gt; element (in a page
 *  or component specification).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ComponentCopyOfRule extends AbstractSpecificationRule
{
    /**
     *  Validates that the element has either type or copy-of (not both, not neither).
     *  Uses the copy-of attribute to find a previously declared component
     *  and copies its type and bindings into the new component (on top of the stack).
     * 
     **/

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String id = getValue(attributes, "id");
        String copyOf = getValue(attributes, "copy-of");
        String type = getValue(attributes, "type");

        if (Tapestry.isBlank(copyOf))
        {
            if (Tapestry.isBlank(type))
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.missing-type-or-copy-of", id),
                    getResourceLocation());

            return;
        }

        if (Tapestry.isNonBlank(type))
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.both-type-and-copy-of", id),
                getResourceLocation());

        IComponentSpecification spec = (IComponentSpecification) digester.getRoot();

        IContainedComponent source = spec.getComponent(copyOf);
        if (source == null)
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.unable-to-copy", copyOf),
                getResourceLocation());

        IContainedComponent target = (IContainedComponent) digester.peek();

        target.setType(source.getType());
        target.setCopyOf(copyOf);

        Iterator i = source.getBindingNames().iterator();
        while (i.hasNext())
        {
            String bindingName = (String) i.next();
            IBindingSpecification binding = source.getBinding(bindingName);
            target.setBinding(bindingName, binding);
        }
    }

}
