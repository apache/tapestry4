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

package net.sf.tapestry.bean;

import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Initializes a helper bean property from a property path (relative
 *  to the bean's {@link IComponent}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class PropertyBeanInitializer extends AbstractBeanInitializer
{
    protected String[] propertyPath;

    public PropertyBeanInitializer(String propertyName, String propertyPath)
    {
        super(propertyName);

        this.propertyPath = PropertyHelper.splitPropertyPath(propertyPath);
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IComponent component = provider.getComponent();
        PropertyHelper componentHelper = PropertyHelper.forInstance(component);
        PropertyHelper beanHelper = PropertyHelper.forInstance(bean);

        Object value = componentHelper.getPath(component, propertyPath);

        beanHelper.set(bean, propertyName, value);
    }

}