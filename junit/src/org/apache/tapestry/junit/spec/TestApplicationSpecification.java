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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IExtensionSpecification;

/**
 *  Tests related to {@link org.apache.tapestry.spec.ApplicationSpecification}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestApplicationSpecification extends TapestryTestCase
{

    public void testBasicExtension() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        PropertyBean extension = (PropertyBean) spec.getExtension("testBean");

        assertEquals("booleanProperty", true, extension.getBooleanProperty());
        assertEquals("intProperty", 18, extension.getIntProperty());
        assertEquals("longProperty", 383838L, extension.getLongProperty());
        assertEquals("doubleProperty", -3.14, extension.getDoubleProperty(), 0.0);
        assertEquals(
            "stringProperty",
            "Tapestry: Java Web Components",
            extension.getStringProperty());
    }

    public void testExtensionType() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        PropertyBean extension = (PropertyBean) spec.getExtension("testBean", Object.class);

        assertNotNull(extension);
    }

    public void testExtensionTypeFailClass() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", Number.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "does not inherit from class java.lang.Number");
        }

    }

    public void testExtensionTypeFailInterface() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", IMonitor.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "does not implement interface org.apache.tapestry.engine.IMonitor");
        }

    }

    public void testExtensionProperty() throws Exception
    {
        IApplicationSpecification a = parseApp("ExtensionProperty.application");

        IExtensionSpecification e = a.getExtensionSpecification("testBean");

        assertEquals("Property fred.", "flintstone", e.getProperty("fred"));
    }

    public void testImmediateExtension() throws Exception
    {
        assertEquals("instanceCount", 0, ImmediateExtension.getInstanceCount());

        parseApp("ImmediateExtension.application");

        assertEquals("instanceCount", 1, ImmediateExtension.getInstanceCount());
    }
}
