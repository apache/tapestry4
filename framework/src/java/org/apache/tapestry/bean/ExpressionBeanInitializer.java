// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * Initializes a helper bean property from an OGNL expression (relative to the bean's
 * {@link IComponent}).
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class ExpressionBeanInitializer extends AbstractBeanInitializer
{
    protected String _expression;

    private final ExpressionEvaluator _evaluator;

    public ExpressionBeanInitializer(ExpressionEvaluator evaluator)
    {
        _evaluator = evaluator;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IComponent component = provider.getComponent();

        try
        {
            Object value = _evaluator.read(component, _expression);

            setBeanProperty(bean, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }
    }

    /** @since 3.0 * */

    public String getExpression()
    {
        return _expression;
    }

    /** @since 3.0 * */

    public void setExpression(String expression)
    {
        _expression = expression;
    }

}