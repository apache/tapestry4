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

package org.apache.tapestry.contrib.components;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Conditional;

/**
 *  This component is a container for {@link When} or Otherwise components;
 *  it provides the context for mutually exclusive conditional evaluation.
 *
 *  [<a href="../../../../../../ComponentReference/contrib.Choose.html">Component Reference</a>]
 *
 *  @author David Solis
 *  @version $Id$
 * 
 **/
public abstract class Choose extends Conditional {


	public void addBody(IRender element)
	{
		super.addBody(element);
		if (element instanceof When)
			((When) element).setChoose(this);	
	}
	
	protected void cleanupAfterRender(IRequestCycle cycle)
	{
		setConditionMet(false);
		super.cleanupAfterRender(cycle);
	}
	
	protected boolean evaluateCondition()
	{
		return getCondition();
	}

	public boolean getInvert()
	{
		// This component doesn't require invert parameter.
		return false;
	}

	public abstract boolean getCondition();
	
	public abstract boolean isConditionMet();
	public abstract void setConditionMet(boolean value);
}
