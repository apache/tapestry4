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
package net.sf.tapestry.binding;

import net.sf.tapestry.IComponent;

/**
 *  A binding that connects directly to a localized string for
 *  a component.
 *
 *  @see IComponent#getString(String)
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class StringBinding extends AbstractBinding
{
    private IComponent component;
    private String key;

    public StringBinding(IComponent component, String key)
    {
        this.component = component;
        this.key = key;
    }

    public IComponent getComponent()
    {
        return component;
    }

    public String getKey()
    {
        return key;
    }

    /**
     *  Accesses the specified localized string.  Never returns null.
     *
     **/

    public Object getObject()
    {
        return component.getString(key);
    }

    /**
     *  Returns String.class.
     * 
     **/

    public Class getType()
    {
        return String.class;
    }

    /**
     *  Returns true.  Localized component strings are
     *  read-only.
     * 
     **/

    public boolean isInvariant()
    {
        return true;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("StringBinding");
        buffer.append('[');
        buffer.append(component.getExtendedId());
        buffer.append(' ');
        buffer.append(key);
        buffer.append(']');

        return buffer.toString();
    }
}