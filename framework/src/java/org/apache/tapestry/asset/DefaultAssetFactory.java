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

import java.util.Locale;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;

/**
 * Default asset factory used when the asset path contains a prefix that is not recognized. It is
 * assumed that the prefix is, in fact, the scheme of an external URL. Retur
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DefaultAssetFactory implements AssetFactory
{

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        return new ExternalAsset(path, location);
    }

}