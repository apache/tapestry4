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