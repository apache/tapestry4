// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services;

import org.apache.tapestry.IEngine;

/**
 * Service responsible for obtaining instances of {@link org.apache.tapestry.IEngine}
 * to service the current request.  An engine service may be retrieved from a pool, or extracted
 * from the HttpSession. After the request is processed, the engine is re-stored into the
 * HttpSession (if stateful) or back into the pool (if not stateful).
 *
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface EngineManager
{
	/**
	 * Locates or creates an engine instance for the current request.
	 */
	IEngine getEngineInstance();
	
	/**
	 * Store the engine back at the end of the current request.
	 */
	
	void storeEngineInstance(IEngine engine);
}
