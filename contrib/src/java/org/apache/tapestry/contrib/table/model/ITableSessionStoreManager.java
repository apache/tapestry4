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

package org.apache.tapestry.contrib.table.model;

import java.io.Serializable;

import org.apache.tapestry.IRequestCycle;

/**
 * An interface responsible for determining <b>where</b> the session state 
 * will be saved between requests.
 *  
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSessionStoreManager
{
	/**
	 * Method saveState saves the session sate
	 * @param objCycle the current request cycle
	 * @param objState the session state to be saved
	 */
	void saveState(IRequestCycle objCycle, Serializable objState);
	/**
	 * Method loadState loads the session state
	 * @param objCycle the current request cycle
	 * @return Object the loaded sessions state
	 */
	Serializable loadState(IRequestCycle objCycle);
}
