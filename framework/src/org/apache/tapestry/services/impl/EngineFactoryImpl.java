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

package org.apache.tapestry.services.impl;

import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.services.EngineFactory;

/**
 * Standard implementation of {@link org.apache.tapestry.services.EngineFactory} service.
 * This should do for most purposes, since a major focus of Tapestry 3.1 is to no longer
 * require subclassing of {@link org.apache.tapestry.engine.BaseEngine}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class EngineFactoryImpl implements EngineFactory
{
    interface EngineConstructor
    {
        IEngine construct();
    }

    static class BaseEngineConstructor implements EngineConstructor
    {
        public IEngine construct()
        {
            return new BaseEngine();
        }
    }

    static class ReflectiveEngineConstructor implements EngineConstructor
    {
        private Class _engineClass;

        ReflectiveEngineConstructor(Class engineClass)
        {
            _engineClass = engineClass;
        }

        public IEngine construct()
        {
            try
            {
                return (IEngine) _engineClass.newInstance();
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    ImplMessages.errorInstantiatingEngine(_engineClass, ex),
                    ex);
            }
        }
    }

    public IEngine constructNewEngineInstance(Locale locale)
    {
        return null;
    }

}
