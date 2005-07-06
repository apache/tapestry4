// Copyright 2005 The Apache Software Foundation
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
package org.apache.tapestry.record;


import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;

/**
 * Defines the 'page' scope for persisting client properties.
 * Persist the properties only if the current page name is the same as that of the property. 
 * 
 * @author Mindbridge
 * @since 4.0
 * @see org.apache.tapestry.record.ClientPropertyPersistenceScope
 */
public class PageClientPropertyPersistenceScope implements
		ClientPropertyPersistenceScope {

	public boolean addParametersForPersistentProperties(
			ServiceEncoding encoding, IRequestCycle cycle, String pageName,
			PersistentPropertyData data) {
		return pageName.equals(cycle.getPage().getPageName());
	}

}
