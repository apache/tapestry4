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

import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.parse.ComponentTemplate;

/**
 *  Acts as a delegate to the {@link ITemplateSource}, providing access to
 *  page and component templates after the normal search mechanisms have failed.
 * 
 *  <p>
 *  The delegate must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *  @see org.apache.tapestry.engine.DefaultTemplateSource
 * 
 **/

public interface ITemplateSourceDelegate
{
	/**
	 *  Invoked by the {@link ITemplateSource} when a template can't be found
	 *  by normal means (i.e., in the normal locations).  This method
	 *  should find the template.  The result may be null.  The delegate
	 *  is responsible for caching the result.
	 * 
	 *  @param cycle for access to Tapestry and Servlet API objects
	 *  @param component component (or page) for which a template is needed
	 *  @param locale the desired locale for the template
	 * 
	 **/
	
	public ComponentTemplate findTemplate(IRequestCycle cycle,
	IComponent component,
	Locale locale);
}
