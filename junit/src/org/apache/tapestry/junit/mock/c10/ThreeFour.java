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

package org.apache.tapestry.junit.mock.c10;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.components.LinkEventType;

/**
 *  Used on a page to test links and Body. 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/

public class ThreeFour extends Home
{

	private class LinkContentRender implements IRender
	{
	
        public void render(IMarkupWriter writer, IRequestCycle cycle)
        {
        	renderLinkContent(writer, cycle);
        }

	}

	private void renderLinkContent(IMarkupWriter writer, IRequestCycle cycle)
	{
		ILinkComponent link = (ILinkComponent)getComponent("link");
		
		// These don't exist, but that's ok, we're testing the link's sensitivity
		// to be inside a Body
		
		link.addEventHandler(LinkEventType.CLICK, "eventHandler1");
		link.addEventHandler(LinkEventType.CLICK, "eventHandler2");
		link.addEventHandler(LinkEventType.CLICK, "eventHandler3");
	}

	public IRender getLinkContentRender()
	{
		return new LinkContentRender();
	}
}
