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

package org.apache.tapestry.script;

import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScriptProcessor;

/**
 *  The result of executing a script, the session is used during the parsing
 *  process as well.  Following {@link org.apache.tapestry.IScript#execute(org.apache.tapestry.IRequestCycle, org.apache.tapestry.IScriptProcessor, java.util.Map)}, the session
 *  provides access to output symbols as well as the body and initialization
 *  blocks created by the script tokens.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

public class ScriptSession
{
    private IRequestCycle _cycle;
    private IScriptProcessor _processor;
    private IResourceLocation _scriptLocation;
    private Map _symbols;

    public ScriptSession(
        IResourceLocation scriptLocation,
        IRequestCycle cycle,
        IScriptProcessor processor,
        Map symbols)
    {
        _scriptLocation = scriptLocation;
        _cycle = cycle;
        _processor = processor;
        _symbols = symbols;
    }

    public IResourceLocation getScriptPath()
    {
        return _scriptLocation;
    }

    public Map getSymbols()
    {
        return _symbols;
    }

	public IRequestCycle getRequestCycle()
	{
		return _cycle;
	}

    public IScriptProcessor getProcessor()
    {
        return _processor;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ScriptSession[");
        buffer.append(_scriptLocation);
        buffer.append(']');

        return buffer.toString();
    }
}