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

package org.apache.tapestry.services.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.schema.Translator;

/**
 * A special-purpose factory for constructing the
 * {@link org.apache.tapestry.services.InjectedValueProvider}service (which needs access to a
 * {@link org.apache.hivemind.internal.Module}which is normally not visible.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InjectedValueProviderFactory implements ServiceImplementationFactory
{
    private Translator _objectTranslator;

    public Object createCoreServiceImplementation(ServiceImplementationFactoryParameters parameters)
    {
        // The invoking module here is the tapestry module

        return new InjectedValueProviderImpl(parameters.getInvokingModule(), _objectTranslator);
    }

    public void setObjectTranslator(Translator objectTranslator)
    {
        _objectTranslator = objectTranslator;
    }
}