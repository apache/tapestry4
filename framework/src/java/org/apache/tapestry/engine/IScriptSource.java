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

package org.apache.tapestry.engine;

import org.apache.hivemind.Resource;
import org.apache.tapestry.IScript;

/**
 * Provides access to an {@link IScript}.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.2
 */

public interface IScriptSource
{
    /**
     * Retrieves the script identified by the location from the source's cache,
     * reading and parsing the script if necessary.
     */

    IScript getScript(Resource resource);
}
