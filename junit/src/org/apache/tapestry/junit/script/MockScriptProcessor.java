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

package org.apache.tapestry.junit.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.util.IdAllocator;

/**
 * Used by {@link org.apache.tapestry.junit.script.TestScript}.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 **/
public class MockScriptProcessor implements IScriptProcessor
{
    private StringBuffer _body;
    private StringBuffer _initialization;
    private List _externalScripts;
    private IdAllocator _idAllocator = new IdAllocator();

    public void addBodyScript(String script)
    {
        if (_body == null)
            _body = new StringBuffer();

        _body.append(script);
    }

	public String getBody()
	{
		if (_body == null)
			return null;
			
			return _body.toString();
	}

    public void addInitializationScript(String script)
    {
        if (_initialization == null)
            _initialization = new StringBuffer();

        _initialization.append(script);
    }

	public String getInitialization()
	{
		if (_initialization == null)return null;
		
		return _initialization.toString();
	}

    public void addExternalScript(IResourceLocation scriptLocation)
    {
        if (_externalScripts == null)
            _externalScripts = new ArrayList();

        _externalScripts.add(scriptLocation);
    }
    
    public IResourceLocation[] getExternalScripts()
    {
    	if (_externalScripts == null)return null;
    	
    	int count = _externalScripts.size();
    	
    	return (IResourceLocation[])_externalScripts.toArray(new IResourceLocation[count]);
    }

    public String getUniqueString(String baseValue)
    {
    	return _idAllocator.allocateId(baseValue);
    }

}
