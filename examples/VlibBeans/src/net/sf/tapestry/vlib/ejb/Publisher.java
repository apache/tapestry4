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

package net.sf.tapestry.vlib.ejb;

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
    private static final long serialVersionUID = -4137036147085472403L;
    
    private Integer primaryKey;
    private String name;

    public Publisher(Integer primaryKey, String name)
    {
        this.primaryKey = primaryKey;
        this.name = name;
    }

    public Integer getPrimaryKey()
    {
        return primaryKey;
    }

    public String getName()
    {
        return name;
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
        name = value;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("Publisher[");
        buffer.append(primaryKey);
        buffer.append(' ');
        buffer.append(name);
        buffer.append(']');

        return buffer.toString();
    }
}