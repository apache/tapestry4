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

package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;

/**
 *  A light-weight, read-only version of the {@link IPublisher} bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Publisher implements Serializable
{
    private static final long serialVersionUID = -2843992788128325821L;
    
    private Integer _id;
    private String _name;

    public Publisher(Integer id, String name)
    {
        _id = id;
        _name = name;
    }

    public Integer getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    /**
     *  Name is a writable property of this bean, to support the
     *  applications' EditPublisher's page.
     *
     *  @see IOperations#updatePublishers(Publisher[],Integer[])
     *
     **/

    public void setName(String value)
    {
        _name = value;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("Publisher[");
        buffer.append(_id);
        buffer.append(' ');
        buffer.append(_name);
        buffer.append(']');

        return buffer.toString();
    }
}