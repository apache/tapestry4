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

package org.apache.tapestry.util;

import java.util.List;

/**
 *  An interface that defines an object that can store named propertys.  The names
 *  and the properties are Strings.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPropertyHolder
{
    /**
     *  Returns a List of Strings, the names of all
     *  properties held by the receiver.  May return an empty list.
     *  The List is sorted alphabetically.  The List may be modified
     *  without affecting this property holder.
     *
     *  <p>Prior to release 2.2, this method returned Collection.
     * 
     **/

    public List getPropertyNames();

    /**
     *  Sets a named property.  The new value replaces the existing value, if any.
     *  Setting a property to null is the same as removing the property.
     *
     **/

    public void setProperty(String name, String value);

    /**
     *  Removes the named property, if present.
     *
     **/

    public void removeProperty(String name);

    /**
     *  Retrieves the named property, or null if the property is not defined.
     *
     **/

    public String getProperty(String name);
}