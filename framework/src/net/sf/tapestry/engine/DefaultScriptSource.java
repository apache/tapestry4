package net.sf.tapestry.engine;

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.script.ScriptParser;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 *  Provides basic access to scripts available on the classpath.  Scripts are cached in
 *  memory once parsed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public class DefaultScriptSource implements IScriptSource
{
    private IResourceResolver _resolver;

    private Map _cache = new HashMap();

    private static final int MAP_SIZE = 17;

    public DefaultScriptSource(IResourceResolver resolver)
    {
        _resolver = resolver;
    }

    public synchronized void reset()
    {
        _cache.clear();
    }

    public synchronized IScript getScript(IResourceLocation scriptLocation)
    {
        IScript result = (IScript) _cache.get(scriptLocation);

        if (result != null)
            return result;

        result = parse(scriptLocation);

        _cache.put(scriptLocation, result);

        return result;
    }

    private IScript parse(IResourceLocation location)
    {
        ScriptParser parser = new ScriptParser(_resolver);

        try
        {
            return parser.parse(location);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultScriptSource.unable-to-parse-script", location),
                ex);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("DefaultScriptSource@");
        buffer.append(Integer.toHexString(hashCode()));

        buffer.append('[');

        if (_cache != null)
        {
            synchronized (_cache)
            {
                buffer.append(_cache.keySet());
            }

            buffer.append(", ");
        }

        buffer.append("]");

        return buffer.toString();
    }

}