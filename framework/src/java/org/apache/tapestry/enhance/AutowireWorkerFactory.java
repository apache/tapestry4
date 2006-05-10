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
package org.apache.tapestry.enhance;

import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;

/**
 * Constructs {@link AutowireWorker}s.  This is necessary because the
 * {@link AutowireWorker} must have its HiveMind module injected into it.
 * 
 * @author James Carman
 * @version 1.0
 */
public class AutowireWorkerFactory implements ServiceImplementationFactory
{
    public Object createCoreServiceImplementation(
            ServiceImplementationFactoryParameters params )
    {
        return new AutowireWorker( params.getInvokingModule(), params.getLog() );
    }
}
