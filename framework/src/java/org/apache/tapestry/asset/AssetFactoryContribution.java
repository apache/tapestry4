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

package org.apache.tapestry.asset;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * Contribution to the <code>tapestry.asset.AssetFactories</code> configuration point.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AssetFactoryContribution extends BaseLocatable
{
    private String _prefix;

    private AssetFactory _factory;

    public AssetFactory getFactory()
    {
        return _factory;
    }

    public void setFactory(AssetFactory factory)
    {
        _factory = factory;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void setPrefix(String prefix)
    {
        _prefix = prefix;
    }
}