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

package org.apache.tapestry.spec;
/**
 *  Defines a transient or persistant property of a component or page.  
 *  A {@link org.apache.tapestry.engine.IComponentClassEnhancer} uses this information
 *  to create a subclass with the necessary instance variables and methods.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class PropertySpecification extends BaseLocatable implements IPropertySpecification
{
	private String _name;
	private String _type = "java.lang.Object";
	private boolean _persistent;
	private String _initialValue;
	
    public String getInitialValue()
    {
        return _initialValue;
    }

    public String getName()
    {
        return _name;
    }

    public boolean isPersistent()
    {
        return _persistent;
    }

    public String getType()
    {
        return _type;
    }

    public void setInitialValue(String initialValue)
    {
        _initialValue = initialValue;
    }

	/**
	 *  Sets the name of the property.  This should not be changed
	 *  once this IPropertySpecification is added to
	 *  a {@link org.apache.tapestry.spec.ComponentSpecification}.
	 * 
	 **/
	
    public void setName(String name)
    {
        _name = name;
    }

    public void setPersistent(boolean persistant)
    {
        _persistent = persistant;
    }

    public void setType(String type)
    {
        _type = type;
    }

}
