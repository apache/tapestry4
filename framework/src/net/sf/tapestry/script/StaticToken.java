package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  A token for static portions of the template.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class StaticToken implements IScriptToken
{
    private String text;

    StaticToken(String text)
    {
        this.text = text;
    }

    /**
     *  Writes the text to the writer.
     *
     **/

    public void write(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        buffer.append(text);
    }

    public void addToken(IScriptToken token)
    {
        // Should never be invoked.
    }
}