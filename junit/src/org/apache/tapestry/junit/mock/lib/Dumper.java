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

package org.apache.tapestry.junit.mock.lib;

import org.apache.tapestry.html.BasePage;

/**
 *  Dumps out an array of objects.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Dumper extends BasePage
{
    private Object[] _objects;
    private Object _currentObject;
    
    public void detach()
    {
        _objects = null;
        _currentObject = null;
        
        super.detach();
    }
    
    public Object[] getObjects()
    {
        return _objects;
    }
    
    public void setObjects(Object[] objects)
    {
        _objects = objects;
        
        fireObservedChange("objects", _objects);
    }
    
    public Object getCurrentObject()
    {
        return _currentObject;
    }

    public void setCurrentObject(Object currentObject)
    {
        _currentObject = currentObject;
    }

    /**
     *  Returns the class name of the current object.  OGNL has trouble
     *  getting properties from Class objects.
     * 
     **/
    
    public String getClassName()
    {
        if (_currentObject == null)
            return "<Null>";
            
        return _currentObject.getClass().getName();
    }
}
