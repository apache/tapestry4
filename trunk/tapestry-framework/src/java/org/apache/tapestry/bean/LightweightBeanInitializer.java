// Copyright 2005 The Apache Software Foundation
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
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IBeanProvider;

/**
 * Performs "lightweight" initialization of a bean; this is based on a term (and code) from
 * HiveMind, where the settings for a bean's properties are expressed as a single string.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class LightweightBeanInitializer extends BaseLocatable implements IBeanInitializer
{
    private final String _initializer;

    public LightweightBeanInitializer(String initializer)
    {
        Defense.notNull(initializer, "initializer");

        _initializer = initializer;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        try
        {
            PropertyUtils.configureProperties(bean, _initializer);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }
    }

    /**
     * Returns the initializer itself, rather than any single property name.
     */
    public String getPropertyName()
    {
        return _initializer;
    }

}
