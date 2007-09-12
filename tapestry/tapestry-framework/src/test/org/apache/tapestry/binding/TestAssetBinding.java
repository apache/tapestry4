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

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAssetBinding extends BindingTestCase
{

    public void testGetObject()
    {
        IAsset asset = newMock(IAsset.class);
        IComponent component = newMock(IComponent.class);

        expect(component.getAsset("fred")).andReturn(asset);

        Location l = fabricateLocation(22);

        ValueConverter vc = newValueConverter();

        replay();

        AssetBinding b = new AssetBinding("parameterName", vc, l, component, "fred");

        assertSame(asset, b.getObject());

        assertSame(l, b.getLocation());
        assertEquals("fred", b.getDescription());

        assertSame(component, b.getComponent());

        verify();
    }

    public void testAssetMissing()
    {
        IComponent component = newMock(IComponent.class);

        expect(component.getAsset("fred")).andReturn(null);

        expect(component.getExtendedId()).andReturn("Home/foo");

        Location l = fabricateLocation(22);

        ValueConverter vc = newValueConverter();

        replay();

        IBinding b = new AssetBinding("parameterName", vc, l, component, "fred");

        try
        {
            b.getObject();
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Component Home/foo does not contain an asset named 'fred'.", ex
                    .getMessage());
            assertSame(l, ex.getLocation());
            assertSame(component, ex.getComponent());
            assertSame(b, ex.getBinding());
        }

    }
}