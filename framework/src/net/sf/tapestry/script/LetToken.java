package net.sf.tapestry.script;

import java.util.Map;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  Allows for the creation of new symbols that can be used in the script
 *  or returned to the caller.
 *
 *  <p>The &lt;let&gt; tag wraps around static text and &lt;insert&gt;
 *  elements.  The results are trimmed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

class LetToken extends AbstractToken
{
    private String _key;
    private int _bufferLengthHighwater = 20;

    public LetToken(String key)
    {
        _key = key;
    }

    public void write(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        if (buffer != null)
            throw new IllegalArgumentException();

        buffer = new StringBuffer(_bufferLengthHighwater);

        writeChildren(buffer, session);

        // Store the symbol back into the root set of symbols.

        Map symbols = session.getSymbols();
        symbols.put(_key, buffer.toString().trim());

        // Store the buffer length from this run for the next run, since its
        // going to be approximately the right size.

        _bufferLengthHighwater = Math.max(_bufferLengthHighwater, buffer.length());
    }
}