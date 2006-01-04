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

package org.apache.tapestry.record;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * A contribution to the <code>tapestry.persist.PersistenceStrategy</code> configuration.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PropertyPersistenceStrategyContribution extends BaseLocatable
{
    private String _name;

    private PropertyPersistenceStrategy _strategy;

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public PropertyPersistenceStrategy getStrategy()
    {
        return _strategy;
    }

    public void setStrategy(PropertyPersistenceStrategy strategy)
    {
        _strategy = strategy;
    }
}