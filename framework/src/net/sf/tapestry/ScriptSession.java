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
    private IResourceLocation _scriptLocation;
    private Map _symbols;
    private String _body;
    private String _initialization;

    /**
     *  List of included scripts.
     *
     *  @since 1.0.5
     *
     **/

    private List _includes;

    public ScriptSession(IResourceLocation scriptPath, Map symbols)
    {
        _scriptLocation = scriptPath;
        _symbols = symbols;
    }

    public IResourceLocation getScriptPath()
    {
        return _scriptLocation;
    }

    public Map getSymbols()
    {
        return _symbols;
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
        return _includes;
    }

    public void addIncludedScript(String resourcePath)
    {
        if (_includes == null)
            _includes = new ArrayList();

        _includes.add(resourcePath);
    }

    public void setBody(String value)
    {
        _body = value;
    }

    public String getBody()
    {
        return _body;
    }

    public String getInitialization()
    {
        return _initialization;
    }

    public void setInitialization(String value)
    {
        _initialization = value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ScriptSession[");
        buffer.append(_scriptLocation);
        buffer.append(']');

        return buffer.toString();
    }
}