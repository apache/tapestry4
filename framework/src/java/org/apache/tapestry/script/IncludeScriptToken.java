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

package org.apache.tapestry.script;

import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;

/**
 *  A token for included scripts.
 *
 *  @author Howard Lewis Ship
 *  @since 1.0.5
 * 
 **/

class IncludeScriptToken extends AbstractToken
{
    private String _resourcePath;

    public IncludeScriptToken(String resourcePath, Location location)
    {
        super(location);

        _resourcePath = resourcePath;
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        Resource includeLocation = null;

        if (_resourcePath.startsWith("/"))
        {
            includeLocation =
                new ClasspathResource(
                    session.getRequestCycle().getEngine().getClassResolver(),
                    _resourcePath);
        }
        else
        {
            Resource baseLocation = session.getScriptTemplateResource();
            includeLocation = baseLocation.getRelativeResource(_resourcePath);
        }

        // TODO: Allow for scripts relative to context resources!

        session.addExternalScript(includeLocation);
    }

}