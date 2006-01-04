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

package org.apache.tapestry.form;

import org.apache.hivemind.util.PropertyUtils;

/**
 * Tests for {@link org.apache.tapestry.form.AbstractFormComponent}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AbstractFormComponentTest extends BaseFormComponentTestCase
{
    public void testCanTakeFocus()
    {
        AbstractFormComponent component = (AbstractFormComponent) newInstance(AbstractFormComponent.class);

        assertEquals(true, component.getCanTakeFocus());

        PropertyUtils.write(component, "disabled", true);

        assertEquals(false, component.getCanTakeFocus());
    }
}
