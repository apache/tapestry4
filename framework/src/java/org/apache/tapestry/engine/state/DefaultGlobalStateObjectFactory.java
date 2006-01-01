// Copyright 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.engine.state;

import java.util.Collections;
import java.util.HashMap;

import org.apache.hivemind.HiveMind;

/**
 * The default factory for the (default) global application state object. Creates and returns a
 * synchronized HashMap, if no class name is specified. This duplicates behavior from Tapestry 3.0.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultGlobalStateObjectFactory extends NamedClassStateObjectFactory implements
        StateObjectFactory
{

    public Object createStateObject()
    {
        if (HiveMind.isBlank(getClassName()))
            return Collections.synchronizedMap(new HashMap());

        return super.createStateObject();
    }
}