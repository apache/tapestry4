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

import org.apache.tapestry.ILocation;


/**
 *  A token for static portions of the template.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class StaticToken extends AbstractToken
{
    private String _text;

    StaticToken(String text, ILocation location	)
    {
    	super(location);
    	
        _text = text;
    }

    /**
     *  Writes the text to the writer.
     *
     **/

    public void write(StringBuffer buffer, ScriptSession session)
    {
        buffer.append(_text);
    }

    public void addToken(IScriptToken token)
    {
        // Should never be invoked.
    }
}