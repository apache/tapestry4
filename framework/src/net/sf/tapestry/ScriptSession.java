package net.sf.tapestry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  The result of executing a script, the session is used during the parsing
 *  process as well.  Following {@link IScript#execute(Map)}, the session
 *  provides access to output symbols as well as the body and initialization
 *  blocks created by the script tokens.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

public class ScriptSession
{
    private String scriptPath;
    private Map symbols;
    private String body;
    private String initialization;

    /**
     *  List of included scripts.
     *
     *  @since 1.0.5
     *
     **/

    private List includes;

    public ScriptSession(String scriptPath, Map symbols)
    {
        this.scriptPath = scriptPath;
        this.symbols = symbols;
    }

    public String getScriptPath()
    {
        return scriptPath;
    }

    public Map getSymbols()
    {
        return symbols;
    }

    /**
     *  Returns a list of scripts included by the
     *  the executed script.  These are not URLs, they
     *  are resource paths (i.e., in the classpath).
     *
     *  @since 1.0.5
     *
     **/

    public List getIncludedScripts()
    {
        return includes;
    }

    public void addIncludedScript(String resourcePath)
    {
        if (includes == null)
            includes = new ArrayList();

        includes.add(resourcePath);
    }

    public void setBody(String value)
    {
        body = value;
    }

    public String getBody()
    {
        return body;
    }

    public String getInitialization()
    {
        return initialization;
    }

    public void setInitialization(String value)
    {
        initialization = value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ScriptSession[");
        buffer.append(scriptPath);
        buffer.append(']');

        return buffer.toString();
    }
}