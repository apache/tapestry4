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

import org.apache.hivemind.Defense;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * Re-initializes a page property from its initial value.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PropertyReinitializer implements PageDetachListener
{
    private IComponent _component;

    private String _propertyName;

    private Object _initialValue;

    private boolean _initialize = true;

    public PropertyReinitializer(IComponent component, String propertyName)
    {
        Defense.notNull(component, "component");
        Defense.notNull(propertyName, "propertyName");

        _component = component;
        _propertyName = propertyName;
    }

    public void pageDetached(PageEvent event)
    {
        // On the first call, which originates with the PageLoader,
        // we read the value (which may have been set inside
        // a finishLoad() method).

        if (_initialize)
        {
            _initialValue = PropertyUtils.read(_component, _propertyName);
            _initialize = false;
            return;
        }

        PropertyUtils.write(_component, _propertyName, _initialValue);
    }

}