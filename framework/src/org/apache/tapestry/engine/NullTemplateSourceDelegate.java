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
 *  Null implementation of {@link org.apache.tapestry.engine.ITemplateSourceDelegate}. 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class NullTemplateSourceDelegate implements ITemplateSourceDelegate
{
	private static NullTemplateSourceDelegate _shared;
	
	/**
	 *  Returns a shared instance of NullTemplateSourceDelegate.
	 * 
	 **/
	
	public static NullTemplateSourceDelegate getSharedInstance()
	{
		if (_shared == null)
			_shared = new NullTemplateSourceDelegate();
			
		return _shared;
	}

	/**
	 *  Simply returns null.
	 * 
	 **/
	
    public ComponentTemplate findTemplate(IRequestCycle cycle, IComponent component, Locale locale)
    {
        return null;
    }

}
