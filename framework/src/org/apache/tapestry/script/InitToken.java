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
 *  Generates a String from its child tokens, then applies it
 *  to {@link ScriptSession#setInitialization(String)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 *
 **/

class InitToken extends AbstractToken
{
    private int _bufferLengthHighwater = 100;

    public InitToken(ILocation location)
    {
        super(location);
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        if (buffer != null)
            throw new IllegalArgumentException();

        buffer = new StringBuffer(_bufferLengthHighwater);

        writeChildren(buffer, session);

        session.getProcessor().addInitializationScript(buffer.toString());

        // Store the buffer length from this run for the next run, since its
        // going to be approximately the right size.

        _bufferLengthHighwater = Math.max(_bufferLengthHighwater, buffer.length());
    }
}