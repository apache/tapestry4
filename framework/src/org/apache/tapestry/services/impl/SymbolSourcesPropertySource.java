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

import org.apache.hivemind.internal.Module;
import org.apache.tapestry.engine.IPropertySource;

/**
 * Property source that bridges to HiveMind Symbol Sources.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class SymbolSourcesPropertySource implements IPropertySource
{
    private Module _module;

    public SymbolSourcesPropertySource(Module module)
    {
        _module = module;
    }

    /**
     * Invokes {@link Module#getSymbolValue(java.lang.String)}.
     */
    public String getPropertyValue(String propertyName)
    {
        return _module.getSymbolValue(propertyName);
    }

}
