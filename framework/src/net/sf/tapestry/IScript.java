package net.sf.tapestry;

import java.util.Map;

/**
 *  An object that can convert a set of symbols into a collection of JavaScript statements.
 *
 *  <p>IScript implementation must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public interface IScript
{
    /**
     *  Returns a string representing where the script was loaded from.
     *
     **/

    public String getScriptPath();

    /**
     *  Executes the script, which will read and modify the symbols {@link Map}, and return
     *  a {@link ScriptSession} that can be used to obtain results.
     *
     **/

    public ScriptSession execute(Map symbols) throws ScriptException;

}