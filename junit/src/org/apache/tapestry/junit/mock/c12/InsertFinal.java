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
 *  Used to verify that the enhancer does not enhance custom
 *  parameter binding properties, unless they are abstract. This
 *  class is final so it is not possible to extend/enhance - as
 *  long as the component works, the class can not have been 
 *  enhanced.
 *
 *  @author Richard Lewis-Shell
 *  @version $Id$
 *
 **/

public final class InsertFinal extends AbstractComponent
{
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    	writer.print(getBinding("final").getString());
    }

}
