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

import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;

/**
 * Used to create an service instance of
 * {@link  org.apache.tapestry.services.impl.SymbolSourcesPropertySource}. This is because SSPS
 * needs an instance of {@link org.apache.hivemind.Module}, and there's no way to do that
 * with hivemind.BuilderFactory.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class SymbolSourcesPropertySourceFactory implements ServiceImplementationFactory
{

    public Object createCoreServiceImplementation(ServiceImplementationFactoryParameters parameters)
    {
        return new SymbolSourcesPropertySource(parameters.getInvokingModule());
    }

}