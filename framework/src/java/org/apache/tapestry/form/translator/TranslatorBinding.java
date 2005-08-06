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

import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Implementation of {@link org.apache.tapestry.IBinding} that wraps around a
 * {@link org.apache.tapestry.form.translator.Translator}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TranslatorBinding extends AbstractBinding
{
    private final Translator _translator;

    public TranslatorBinding(String description, ValueConverter valueConverter, Location location,
            Translator translator)
    {
        super(description, valueConverter, location);

        Defense.notNull(translator, "translator");

        _translator = translator;
    }

    /**
     * Returns the translator.
     */

    public Object getObject()
    {
        return _translator;
    }

}
