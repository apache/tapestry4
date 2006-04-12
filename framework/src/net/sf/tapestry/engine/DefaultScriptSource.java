package net.sf.tapestry.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
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

    public void reset()
    {
        _cache.clear();
    }

    public IScript getScript(String resourcePath)
    {
        IScript result;

        synchronized (_cache)
        {
            result = (IScript) _cache.get(resourcePath);
        }

        if (result != null)
            return result;

        result = parse(resourcePath);

        // There's a small window if reset() is invoked on a very busy system where
        // cache could be null here.

        synchronized (_cache)
        {
            _cache.put(resourcePath, result);
        }

        return result;
    }

    private IScript parse(String resourcePath)
    {
        ScriptParser parser = new ScriptParser(_resolver);
        InputStream stream = null;

        try
        {
            URL url = _resolver.getResource(resourcePath);

            if (url == null)
                throw new ApplicationRuntimeException(
                    Tapestry.getString("DefaultScriptSource.unable-to-find-script", resourcePath));

            stream = url.openStream();

            IScript result = parser.parse(stream, resourcePath);

            stream.close();

            return result;
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultScriptSource.unable-to-parse-script", resourcePath),
                ex);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultScriptSource.unable-to-read-script", resourcePath),
                ex);
        }
        finally
        {
            Tapestry.close(stream);
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