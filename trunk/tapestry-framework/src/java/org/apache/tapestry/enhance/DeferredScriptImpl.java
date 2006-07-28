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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DeferredScriptImpl implements DeferredScript, Locatable
{
    final Resource _scriptResource;

    final IScriptSource _scriptSource;

    final Location _location;

    public DeferredScriptImpl(Resource resource, IScriptSource source, Location location)
    {
        Defense.notNull(resource, "resource");
        Defense.notNull(source, "source");

        _scriptResource = resource;
        _scriptSource = source;
        _location = location;
    }

    public IScript getScript()
    {
        // No real reason to cache the script here, because a) they are rarely used
        // and b) the IScriptSource caches.

        try
        {
            return _scriptSource.getScript(_scriptResource);
        }
        catch (Exception ex)
        {
            // Decorate the thrown exception with the correct location.

            throw new ApplicationRuntimeException(ex.getMessage(), _location, ex);
        }
    }

    public Location getLocation()
    {
        return _location;
    }

    public String toString()
    {
        return "DeferredScriptImpl[" + _scriptResource + "]";
    }
}
