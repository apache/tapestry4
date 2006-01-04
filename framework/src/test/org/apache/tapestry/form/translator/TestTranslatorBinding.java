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

package org.apache.tapestry.form.translator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.lib.BeanFactory;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingTestCase;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.translator.TranslatorBinding} and
 * {@link org.apache.tapestry.form.translator.TranslatorBindingFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestTranslatorBinding extends BindingTestCase
{
    public void testCreate()
    {
        Location l = newLocation();
        ValueConverter vc = newValueConverter();
        IComponent component = newComponent();

        MockControl bfc = newControl(BeanFactory.class);
        BeanFactory bf = (BeanFactory) bfc.getMock();

        Translator translator = (Translator) newMock(Translator.class);

        bf.get("string");
        bfc.setReturnValue(translator);

        replayControls();

        TranslatorBindingFactory f = new TranslatorBindingFactory();
        f.setValueConverter(vc);
        f.setTranslatorBeanFactory(bf);

        IBinding binding = f.createBinding(component, "description", "string", l);

        assertSame(translator, binding.getObject());
        assertSame(l, binding.getLocation());
        assertTrue(binding.isInvariant());
        assertEquals("description", binding.getDescription());

        verifyControls();
    }

    public void testFailure()
    {
        Location l = newLocation();
        IComponent component = newComponent();

        MockControl bfc = newControl(BeanFactory.class);
        BeanFactory bf = (BeanFactory) bfc.getMock();

        Throwable t = new RuntimeException("Boom!");

        bf.get("string");
        bfc.setThrowable(t);

        replayControls();

        TranslatorBindingFactory f = new TranslatorBindingFactory();
        f.setTranslatorBeanFactory(bf);

        try
        {
            f.createBinding(component, "description", "string", l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Boom!", ex.getMessage());
            assertSame(t, ex.getRootCause());
            assertSame(l, ex.getLocation());
        }

        verifyControls();

    }
}
