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

import org.apache.tapestry.engine.IPropertySource;

/**
 * Tapestry infrastructure ... key services required by the
 * {@link org.apache.tapestry.IEngine} instance.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface Infrastructure
{
	/**
	 * Returns an {@link IPropertySource} configured to search
	 * the application specification, etc.  See 
	 * <code>tapestry.ApplicationPropertySource</code>.
	 */
	public IPropertySource getApplicationPropertySource();

}
