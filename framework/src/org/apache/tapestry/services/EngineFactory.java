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

import java.util.Locale;

import org.apache.tapestry.IEngine;

/**
 * Responsible for creating new instance of {@link org.apache.tapestry.IEngine}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface EngineFactory
{
	/**
	 * Creates and initializes a new engine instance for the specified locale.
	 */
	public IEngine constructNewEngineInstance(Locale locale);
}
