package net.sf.tapestry.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.IResourceLocation;
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
    private IResourceLocation _scriptLocation;
    private List _tokens = new ArrayList();

    public ParsedScript(IResourceLocation scriptLocation)
    {
        _scriptLocation = scriptLocation;
    }

    public IResourceLocation getScriptLocation()
    {
        return _scriptLocation;
    }

    public void addToken(IScriptToken token)
    {
        _tokens.add(token);
    }

    public ScriptSession execute(Map symbols) throws ScriptException
    {
        ScriptSession result = new ScriptSession(_scriptLocation, symbols);
        Iterator i = _tokens.iterator();

        while (i.hasNext())
        {
            IScriptToken token = (IScriptToken) i.next();

            token.write(null, result);
        }

        return result;
    }
}