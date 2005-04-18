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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.schema.Translator;

/**
 * Service <code>tapestry.DeferredObjectTranslator</code>, which is used to encapsulate the
 * ObjectTranslator and return {@link org.apache.tapestry.services.impl.DeferredObjectImpl}instances.
 * These allow the translator to <em>not be executed until (and unless)
 * the value is needed</em>,
 * with the added expense that you have to de-referrence the
 * {@link org.apache.tapestry.services.impl.DeferredObjectImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DeferredObjectTranslator implements Translator
{
    private Translator _objectTranslator;

    public Object translate(Module module, Class propertyType, String inputValue, Location location)
    {
        return new DeferredObjectImpl(_objectTranslator, module, inputValue, location);
    }

    public void setObjectTranslator(Translator objectTranslator)
    {
        _objectTranslator = objectTranslator;
    }
}