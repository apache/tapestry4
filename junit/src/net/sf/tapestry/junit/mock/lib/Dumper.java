//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//


package net.sf.tapestry.junit.mock.lib;

import net.sf.tapestry.html.BasePage;

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
