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

package org.apache.tapestry.junit.spec;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;


/**
 *  Test cases for page and component specifications.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestComponentSpecification extends TapestryTestCase
{

    public void testBeanProperty() throws Exception
    {
        IComponentSpecification s = parseComponent("BeanProperty.jwc");
        IBeanSpecification fred = s.getBeanSpecification("fred");

        checkList("propertyNames", new String[] { "bruce", "nicole", "zeta" }, fred.getPropertyNames());

        checkProperty(fred, "bruce", "wayne");
        checkProperty(fred, "nicole", "kidman");
        checkProperty(fred, "zeta", "jones");

    }

    public void testComponentProperty() throws Exception
    {
        IComponentSpecification s = parseComponent("ComponentProperty.jwc");
        IContainedComponent c = s.getComponent("barney");

        checkList("propertyNames", new String[] { "apple", "chocolate", "frozen" }, c.getPropertyNames());

        checkProperty(c, "apple", "pie");
        checkProperty(c, "chocolate", "cake");
        checkProperty(c, "frozen", "yogurt");

    }
    
    public void testAssetProperty() throws Exception
    {
        IComponentSpecification s = parseComponent("AssetProperty.jwc");
        
        checkAsset(s, "private", "hugh", "grant");
        checkAsset(s, "external", "joan", "rivers");
        checkAsset(s, "context", "john", "cusak");
    }
    
    private void checkAsset(IComponentSpecification s, String assetName, String propertyName,
    String expectedValue)
    {
        IAssetSpecification a = s.getAsset(assetName);
        
        assertEquals("Property " + propertyName + ".",
        expectedValue,
        a.getProperty(propertyName));
    }
}
