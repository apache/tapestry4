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
import org.apache.hivemind.impl.BaseLocatable;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.Tapestry;

/**
 * Base class for initializing a property of a JavaBean.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.5
 */

public abstract class AbstractBeanInitializer extends BaseLocatable implements IBeanInitializer
{
    protected String _propertyName;

    public String getPropertyName()
    {
        return _propertyName;
    }

    /** @since 3.0 * */

    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName;
    }

    protected void setBeanProperty(Object bean, Object value)
    {
        try
        {
            PropertyUtils.write(bean, _propertyName, value);
        }
        catch (ApplicationRuntimeException ex)
        {
            String message = Tapestry.format(
                    "AbstractBeanInitializer.unable-to-set-property",
                    _propertyName,
                    bean,
                    value);

            throw new ApplicationRuntimeException(message, getLocation(), ex);
        }

    }
}
