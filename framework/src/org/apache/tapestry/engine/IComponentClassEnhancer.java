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

import org.apache.tapestry.spec.IComponentSpecification;

/**
 *
 *  A provider of enhanced classes, classes with new methods 
 *  and new attributes, and possibly, implementing new
 *  Java interfaces.  The primary use of class enhancement is to
 *  automate the creation of transient and persistant properties.
 * 
 *  <p>
 *  Implementations of this interface must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public interface IComponentClassEnhancer
{
	/**
	 *  Clears all cached data for the enhancer; this includes references to
	 *  enhanced classes.
	 * 
	 **/
	
	public void reset();
	
	/**
	 *  Used to access the class for a given component (or page).  Returns the
	 *  specified class, or an enhanced version of the class if the
	 *  component requires enhancement.
	 * 
	 *  @param specification the specification for the component
	 *  @param className the name of base class to enhance, as extracted
	 *  from the specification (or possibly, from a default).
	 * 
	 *  @throws org.apache.tapestry.ApplicationRuntimeException if the class does not exist, is invalid,
	 *  or may not be enhanced.
	 * 
	 **/
	
	public Class getEnhancedClass(IComponentSpecification specification, String className);
}
