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

package org.apache.tapestry.services;


/**
 * An pool for objects.  Objects may be stored in a Pool for later reuse.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ObjectPool
{
	/**
	 * Returns an object from the pool, previously stored with the given key. May
	 * return null if no such object exists.
	 */
	Object get(Object key);
	
	/**
	 * Stores an object into the pool for later retrieval with the provided key.
	 */
	
	void store(Object key, Object value);
}
