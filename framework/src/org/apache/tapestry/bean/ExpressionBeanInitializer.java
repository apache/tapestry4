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

package org.apache.tapestry.bean;

import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 * 
 *  Initializes a helper bean property from an OGNL expression (relative
 *  to the bean's {@link IComponent}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class ExpressionBeanInitializer extends AbstractBeanInitializer
{
    protected String _expression;

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IResourceResolver resolver = provider.getResourceResolver();
        IComponent component = provider.getComponent();
        
        Object value = OgnlUtils.get(_expression, resolver, component);

        setBeanProperty(resolver, bean, value);
    }

	/** @since 3.0 **/
	
    public String getExpression()
    {
        return _expression;
    }

	/** @since 3.0 **/
	
    public void setExpression(String expression)
    {
        _expression = expression;
    }

}