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

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.OgnlUtils;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Initializes a helper bean property from an OGNL expression (relative
 *  to the bean's {@link IComponent}).
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *  @deprecated To be removed in 2.3.  Use {@link ExpressionBeanInitializer}.
 * 
 **/

public class PropertyBeanInitializer extends AbstractBeanInitializer
{
    private String _expression;

    public PropertyBeanInitializer(String propertyName, String expression)
    {
        super(propertyName);

        _expression = expression;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IResourceResolver resolver = provider.getResourceResolver();
        IComponent component = provider.getComponent();
        
        Object value = OgnlUtils.get(_expression, resolver, component);

        setBeanProperty(resolver, bean, value);
    }

}