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

package org.apache.tapestry.pageload;

import java.util.Collection;
import java.util.Iterator;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;

/**
 *  Walks through the tree of components and invokes the visitors on each of 
 *  of the components in the tree.
 * 
 *  @author mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class ComponentTreeWalker
{
    private IComponentVisitor[] _visitors;
    
    public ComponentTreeWalker(IComponentVisitor[] visitors)
    {
        _visitors = visitors;
    }

    public void walkComponentTree(IComponent component)
    {
        // Invoke visitors
        for (int i = 0; i < _visitors.length; i++)
        {
            IComponentVisitor visitor = _visitors[i];
            visitor.visitComponent(component);
        }

        // Recurse into the embedded components
        Collection components = component.getComponents().values();

        if (Tapestry.size(components) == 0)
            return;

        for (Iterator it = components.iterator(); it.hasNext();)
        {
            IComponent embedded = (IComponent) it.next();
            walkComponentTree(embedded);
        }
    }
}
