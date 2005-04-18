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

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * Implementation of {@link org.apache.tapestry.binding.BindingFactory}that creates
 * {@link org.apache.tapestry.binding.ExpressionBinding}instances.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class OGNLBindingFactory extends AbstractBindingFactory
{
    private ExpressionEvaluator _expressionEvaluator;

    private ExpressionCache _expressionCache;

    public IBinding createBinding(IComponent root, String description, String path,
            Location location)
    {
        return new ExpressionBinding(description, location, getValueConverter(), root, path,
                _expressionEvaluator, _expressionCache);
    }

    public void setExpressionCache(ExpressionCache expressionCache)
    {
        _expressionCache = expressionCache;
    }

    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator)
    {
        _expressionEvaluator = expressionEvaluator;
    }

}