package net.sf.tapestry.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.IScript;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  A top level container for a number of {@link IScriptToken script tokens}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

public class ParsedScript implements IScript
{
    private String scriptPath;

    public ParsedScript(String scriptPath)
    {
        this.scriptPath = scriptPath;
    }

    public String getScriptPath()
    {
        return scriptPath;
    }

    private List tokens = new ArrayList();

    public void addToken(IScriptToken token)
    {
        tokens.add(token);
    }

    public ScriptSession execute(Map symbols) throws ScriptException
    {
        ScriptSession result = new ScriptSession(scriptPath, symbols);
        Iterator i = tokens.iterator();

        while (i.hasNext())
        {
            IScriptToken token = (IScriptToken) i.next();

            token.write(null, result);
        }

        return result;
    }
}