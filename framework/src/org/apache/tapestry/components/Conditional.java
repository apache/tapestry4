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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  A conditional element on a page which will render its wrapped elements
 *  zero or one times.
 *
 *  [<a href="../../../../../ComponentReference/Conditional.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship, David Solis
 *  @version $Id$
 * 
 **/

public abstract class Conditional extends AbstractComponent 
{
	/**
	 *  Renders its wrapped components only if the condition is true (technically,
	 *  if condition matches invert). 
	 *  Additionally, if element is specified, can emulate that HTML element if condition is met
	 *
	 **/

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) 
	{
		if (evaluateCondition()) 
		{
			String element = getElement();
			
			boolean render = !cycle.isRewinding() && Tapestry.isNonBlank(element);
			
			if (render)
			{
				writer.begin(element);
				renderInformalParameters(writer, cycle);
			}

			renderBody(writer, cycle);
			
			if (render)
				writer.end(element);
		}
	}
	
	protected boolean evaluateCondition()
	{
		return getCondition() != getInvert();
	}

	public abstract boolean getCondition();
	public abstract boolean getInvert();

	public abstract String getElement();
}