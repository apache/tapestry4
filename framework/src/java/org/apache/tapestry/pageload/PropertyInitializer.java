// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * Given a component, a property and a value, this object will reset the property to the value
 * whenever the page (containing the component) is detached. This is related to support for
 * {@link org.apache.tapestry.spec.IPropertySpecification}s.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class PropertyInitializer implements PageDetachListener
{
    private IComponent _component;

    private String _propertyName;

    private String _expression;

    private boolean _invariant;

    private Object _value;

    private Location _location;

    private ExpressionEvaluator _evaluator;

    public PropertyInitializer(IComponent component, String propertyName, String expression,
            Location location, ExpressionEvaluator evaluator)
    {
        _component = component;
        _propertyName = propertyName;
        _expression = expression;
        _location = location;
        _evaluator = evaluator;

        prepareInvariant();
    }

    private void prepareInvariant()
    {
        _invariant = false;

        try
        {
            // If no initial value expression is provided, then read the current
            // property of the expression. This may be null, or may be
            // a value set in finishLoad() (via an abstract accessor).

            if (Tapestry.isBlank(_expression))
            {
                _invariant = true;
                _value = _evaluator.read(_component, _propertyName);
            }
            else if (_evaluator.isConstant(_expression))
            {
                // If the expression is a constant, evaluate it and remember the value
                _invariant = true;
                _value = _evaluator.read(_component, _expression);
            }
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(PageloadMessages.unableToInitializeProperty(
                    _propertyName,
                    _component,
                    ex), _location, ex);
        }
    }

    public void pageDetached(PageEvent event)
    {
        try
        {
            if (_invariant)
                _evaluator.write(_component, _propertyName, _value);
            else
            {
                Object value = _evaluator.read(_component, _expression);
                _evaluator.write(_component, _propertyName, value);
            }
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(PageloadMessages.unableToInitializeProperty(
                    _propertyName,
                    _component,
                    ex), _location, ex);
        }
    }

}