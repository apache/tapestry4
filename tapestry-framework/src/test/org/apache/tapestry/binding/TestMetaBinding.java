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

import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ComponentPropertySource;
import org.testng.annotations.Test;

/**
 * Tests functionality of {@link MetaBinding}.
 */
@Test
public class TestMetaBinding extends BindingTestCase
{
    
    public void test_Create()
    {
        IComponent component = newMock(IComponent.class);
        ValueConverter vc = newValueConverter();
        Location l = fabricateLocation(12);
        ComponentPropertySource src = newMock(ComponentPropertySource.class);
        
        expect(src.getComponentProperty(component, "key")).andReturn("wiggle");
        
        replay();

        MetaBinding b = new MetaBinding("param", vc, l, component, src, "key");
        
        assertSame(component, b.getComponent());
        assertEquals(b.getObject(), "wiggle");

        verify();
    }

    public void test_To_String()
    {
        IComponent component = newComponent();
        ValueConverter vc = newValueConverter();
        Location l = fabricateLocation(12);
        ComponentPropertySource src = newMock(ComponentPropertySource.class);
        
        expect(component.getExtendedId()).andReturn("Foo/bar.baz");
        
        replay();

        MetaBinding b = new MetaBinding("param", vc, l, component, src, "key");
        
        assertEquals(b.toString(), "MetaBinding[Foo/bar.baz key]");

        verify();
    }
}
