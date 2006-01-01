// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.asset.DefaultAssetFactory}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDefaultAssetFactory extends HiveMindTestCase
{
    public void testCreateAssetFull()
    {
        Location l = newLocation();

        replayControls();

        AssetFactory factory = new DefaultAssetFactory();

        ExternalAsset asset = (ExternalAsset) factory.createAsset(null, "/foo/bar/baz", null, l);

        assertEquals("ExternalAsset[/foo/bar/baz]", asset.toString());
        assertSame(l, asset.getLocation());
        
        verifyControls();
    }

    public void testCreateAssetFromResource()
    {
        Location l = newLocation();

        replayControls();

        AssetFactory factory = new DefaultAssetFactory();

        ExternalAsset asset = (ExternalAsset) factory.createAsset(null, "/foo/bar/baz", null, l);

        assertEquals("ExternalAsset[/foo/bar/baz]", asset.toString());
        assertSame(l, asset.getLocation());
        
        verifyControls();

    }
}
