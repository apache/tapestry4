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

package net.sf.tapestry.spec;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  A specification of a helper bean for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanSpecification extends BasePropertyHolder
{
    protected String className;
    protected BeanLifecycle lifecycle;

    /** @since 1.0.9 **/
    private String description;

    /**
     *  A List of {@link IBeanInitializer}.
     *
     **/

    protected List initializers;

    public BeanSpecification(String className, BeanLifecycle lifecycle)
    {
        this.className = className;
        this.lifecycle = lifecycle;
    }

    public String getClassName()
    {
        return className;
    }

    public BeanLifecycle getLifecycle()
    {
        return lifecycle;
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void addInitializer(IBeanInitializer initializer)
    {
        if (initializers == null)
            initializers = new ArrayList();

        initializers.add(initializer);
    }

    /**
     *  Returns the {@link List} of {@link IBeanInitializer}s.  The caller
     *  should not modify this value!.  May return null if there
     *  are no initializers.
     *
     *  @since 1.0.5
     *
     **/

    public List getInitializers()
    {
        return initializers;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("BeanSpecification[");

        buffer.append(className);
        buffer.append(", lifecycle ");
        buffer.append(lifecycle.getEnumerationId());

        if (initializers != null && initializers.size() > 0)
        {
            buffer.append(", ");
            buffer.append(initializers.size());
            buffer.append(" initializers");
        }

        buffer.append(']');

        return buffer.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }
}