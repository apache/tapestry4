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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.BaseLocatable;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Base class for initializing a property of a JavaBean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

abstract public class AbstractBeanInitializer extends BaseLocatable implements IBeanInitializer
{
    protected String _propertyName;

    public String getPropertyName()
    {
        return _propertyName;
    }

    /** @since 3.0 **/

    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName;
    }

    protected void setBeanProperty(IResourceResolver resolver, Object bean, Object value)
    {
        try
        {
            OgnlUtils.set(_propertyName, resolver, bean, value);
        }
        catch (ApplicationRuntimeException ex)
        {
            String message =
                Tapestry.format(
                    "AbstractBeanInitializer.unable-to-set-property",
                    _propertyName,
                    bean,
                    value);

            throw new ApplicationRuntimeException(message, getLocation(), ex);
        }

    }
}