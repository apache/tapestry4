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

package org.apache.tapestry.param;

import org.apache.tapestry.IRequestCycle;

/**
 *  Define a type of connector between a binding of a component and a JavaBeans
 *  property of the component (with the same name).  Allows
 *  for the parameter to be set before the component is rendered,
 *  then cleared after the component is rendered.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 * 
 **/

public interface IParameterConnector
{
    /**
     *  Sets the parameter from the binding.
     *  
     *  @throws RequiredParameterException if the parameter is
     *  required, but the {@link org.apache.tapestry.IBinding}
     *  supplies a null value.
     * 
     **/
    
	public void setParameter(IRequestCycle cycle);
	
	/**
	 *  Clears the parameters to a null, 0 or false value
	 *  (depending on type).
	 * 
	 **/
	
	public void resetParameter(IRequestCycle cycle);
}
