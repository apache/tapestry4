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
import org.apache.tapestry.IMessages;

/**
 *  Defines an object that can provide a component with its
 *  {@link org.apache.tapestry.IMessages}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public interface IComponentMessagesSource
{
	public IMessages getMessages(IComponent component);
	
	/**
	 *  Clears all cached information for the source.
	 * 
	 **/
	
	public void reset();
}
