//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

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

    public void write(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        if (buffer != null)
            throw new IllegalArgumentException();

        buffer = new StringBuffer(_bufferLengthHighwater);

        writeChildren(buffer, session);

        session.setInitialization(buffer.toString());

        // Store the buffer length from this run for the next run, since its
        // going to be approximately the right size.

        _bufferLengthHighwater = Math.max(_bufferLengthHighwater, buffer.length());
    }
}