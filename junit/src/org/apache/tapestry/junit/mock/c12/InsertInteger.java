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

package org.apache.tapestry.junit.mock.c12;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Used to verify that the enhancer creates a property 
 *  with the correct name (not necessarily the parameter
 *  name).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class InsertInteger extends AbstractComponent
{
	public abstract int getIntValue();
	
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    	writer.print(getIntValue());
    }

}
