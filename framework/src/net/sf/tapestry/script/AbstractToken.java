package net.sf.tapestry.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  Base class for creating tokens which may contain other tokens.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

abstract class AbstractToken implements IScriptToken
{
    private List tokens;

    public void addToken(IScriptToken token)
    {
        if (tokens == null)
            tokens = new ArrayList();

        tokens.add(token);
    }

    /**
     *  Invokes {@link IScriptToken#write(StringBuffer,ScriptSession)}
     *  on each child token (if there are any).
     *
     **/

    protected void writeChildren(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        if (tokens == null)
            return;

        Iterator i = tokens.iterator();

        while (i.hasNext())
        {
            IScriptToken token = (IScriptToken) i.next();

            token.write(buffer, session);
        }
    }
}