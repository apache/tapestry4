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

package org.apache.tapestry.util.prop;

/**
 *  Used by {@link org.apache.tapestry.util.prop.PropertyFinder}
 *  to identify information about a property. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PropertyInfo
{
    private String _name;
    private Class _type;
    private boolean _read;
    private boolean _write;
    
    PropertyInfo(String name, Class type, boolean read, boolean write)
    {
        _name = name;
        _type = type;
        _read = read;
        _write = write;
    }
    
    public String getName()
    {
        return _name;
    }

    public Class getType()
    {
        return _type;
    }

    public boolean isRead()
    {
        return _read;
    }

    public boolean isWrite()
    {
        return _write;
    }
    
    public boolean isReadWrite()
    {
        return _read && _write;
    }

}
