package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  A token for included scripts.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

class IncludeScriptToken extends AbstractToken
{
    private String resourcePath;

    public IncludeScriptToken(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    public void write(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        session.addIncludedScript(resourcePath);
    }

}