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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.schema.Translator;
import org.apache.tapestry.services.InjectedValueProvider;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectedValueProviderImpl implements InjectedValueProvider
{
    private Module _module;

    private Translator _objectTranslator;

    /**
     * Deletegates out to
     * {@link Translator#translate(org.apache.hivemind.internal.Module, java.lang.Class, java.lang.String, org.apache.hivemind.Location)
     */
    public Object obtainValue(String locator, Location location)
    {
        return _objectTranslator.translate(_module, Object.class, locator, location);
    }

    public InjectedValueProviderImpl(Module module, Translator objectTranslator)
    {
        _module = module;
        _objectTranslator = objectTranslator;
    }
}