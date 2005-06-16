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
import org.apache.tapestry.binding.BindingFactory;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Uses the tapestry.form.translator.TranslatorBeanFactory service to obtain configured
 * {@link org.apache.tapestry.form.translator.Translator} instances that are then wrapped as
 * {@link org.apache.tapestry.form.translator.TranslatorBinding}s.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TranslatorBindingFactory implements BindingFactory
{
    private BeanFactory _translatorBeanFactory;

    private ValueConverter _valueConverter;

    public IBinding createBinding(IComponent root, String bindingDescription, String path,
            Location location)
    {
        try
        {
            Translator translator = (Translator) _translatorBeanFactory.get(path);

            return new TranslatorBinding(bindingDescription, _valueConverter, location, translator);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), location, ex);
        }
    }

    public void setTranslatorBeanFactory(BeanFactory translatorBeanFactory)
    {
        _translatorBeanFactory = translatorBeanFactory;
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }
}
