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

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestAssetBinding extends HiveMindTestCase
{
    private ValueConverter newValueConverter()
    {
        return (ValueConverter) newMock(ValueConverter.class);
    }

    public void testGetObject()
    {
        IAsset asset = (IAsset) newMock(IAsset.class);

        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        component.getAsset("fred");
        componentc.setReturnValue(asset);

        Location l = fabricateLocation(22);

        ValueConverter vc = newValueConverter();

        replayControls();

        AssetBinding b = new AssetBinding(component, "fred", "parameterName", vc, l);

        assertSame(asset, b.getObject());

        assertSame(l, b.getLocation());
        assertEquals("parameterName", b.getParameterName());

        assertSame(component, b.getComponent());

        verifyControls();
    }

    public void testAssetMissing()
    {
        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        component.getAsset("fred");
        componentc.setReturnValue(null);

        component.getExtendedId();
        componentc.setReturnValue("Home/foo");

        Location l = fabricateLocation(22);

        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding b = new AssetBinding(component, "fred", "parameterName", vc, l);

        
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