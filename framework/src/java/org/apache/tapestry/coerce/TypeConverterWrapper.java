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

package org.apache.tapestry.coerce;

import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.lib.util.StrategyRegistry;
import org.apache.hivemind.lib.util.StrategyRegistryImpl;

/**
 * A service implementation that works around an
 * {@link org.apache.hivemind.lib.util.StrategyRegistry}. The registry is contructed from a
 * configuration that follows the <code>tapestry.coerce.Converters</code> schema (a List of
 * {@link org.apache.tapestry.coerce.TypeConverterContribution}plus an additional converter for
 * nulls.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TypeConverterWrapper implements TypeConverter
{
    private StrategyRegistry _registry = new StrategyRegistryImpl();

    private List _contributions;

    private TypeConverter _nullConverter;

    public void initializeService()
    {
        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            TypeConverterContribution c = (TypeConverterContribution) i.next();

            _registry.register(c.getSubjectClass(), c.getConverter());
        }
    }

    public Object convertValue(Object value)
    {
        if (value == null)
        {
            if (_nullConverter == null)
                return null;

            return _nullConverter.convertValue(null);
        }

        TypeConverter delegate = (TypeConverter) _registry.getStrategy(value.getClass());

        return delegate.convertValue(value);
    }

    /**
     * Sets the List of {@link org.apache.tapestry.coerce.TypeConverterContribution}s.
     */

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    /**
     * Sets the converter used to convert null.
     */

    public void setNullConverter(TypeConverter nullConverter)
    {
        _nullConverter = nullConverter;
    }
}