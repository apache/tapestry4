package net.sf.tapestry;

/**
 *  Provides access to an {@link IScript}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 **/

public interface IScriptSource
{
    /**
     *  Retrieves the script identified by the resource path from the source's
     *  cache, reading and parsing the script if necessary.
     * 
     **/

    public IScript getScript(String resourcePath);

    /**
     *  Invoked to clear any cached scripts.
     *
     **/

    public void reset();
}