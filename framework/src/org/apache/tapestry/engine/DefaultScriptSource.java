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

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.script.ScriptParser;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 *  Provides basic access to scripts available on the classpath.  Scripts are cached in
 *  memory once parsed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public class DefaultScriptSource implements IScriptSource
{
    private IResourceResolver _resolver;

    private Map _cache = new HashMap();

    public DefaultScriptSource(IResourceResolver resolver)
    {
        _resolver = resolver;
    }

    public synchronized void reset()
    {
        _cache.clear();
    }

    public synchronized IScript getScript(IResourceLocation scriptLocation)
    {
        IScript result = (IScript) _cache.get(scriptLocation);

        if (result != null)
            return result;

        result = parse(scriptLocation);

        _cache.put(scriptLocation, result);

        return result;
    }

    private IScript parse(IResourceLocation location)
    {
        ScriptParser parser = new ScriptParser(_resolver);

        try
        {
            return parser.parse(location);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("DefaultScriptSource.unable-to-parse-script", location),
                ex);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("DefaultScriptSource@");
        buffer.append(Integer.toHexString(hashCode()));

        buffer.append('[');

        if (_cache != null)
        {
            synchronized (_cache)
            {
                buffer.append(_cache.keySet());
            }

            buffer.append(", ");
        }

        buffer.append("]");

        return buffer.toString();
    }

}