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

package org.apache.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 *  A component which delegates it's behavior to another object.
 *
 *  [<a href="../../../../../ComponentReference/Delegator.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Delegator extends AbstractComponent
{
    /**
     *  Gets its delegate and invokes {@link IRender#render(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    	IRender delegate = getDelegate();
    	
        if (delegate != null)
        	delegate.render(writer, cycle);
    }
    
    public abstract IRender getDelegate();
}