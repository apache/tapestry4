package net.sf.tapestry.pageload;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IMonitor;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.asset.ContextAsset;
import net.sf.tapestry.asset.ExternalAsset;
import net.sf.tapestry.asset.PrivateAsset;
import net.sf.tapestry.binding.FieldBinding;
import net.sf.tapestry.binding.StaticBinding;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.util.MultiKey;
import net.sf.tapestry.util.pool.Pool;

/**
 *  A source for pages for a particular application.  Each application
 *  should have its own <code>PageSource</code>, storing it into the
 *  {@link javax.servlet.ServletContext} using a unique key (usually built from
 *  the application name).
 *
 *  <p>The <code>PageSource</code> acts as a pool for {@link IPage} instances.
 *  Pages are retrieved from the pool using {@link #getPage(IRequestCycle, String, IMonitor)}
 *  and are later returned to the pool using {@link #releasePage(IPage)}.
 *
 *
 *  <p>In addition, this class acts as a cache of serveral common
 *  objects:
 *  <ul>
 *  <li>{@link FieldBinding}
 *  <li>{@link StaticBinding}
 *  <li>{@link ExternalAsset}
 *  <li>{@link ContextAsset}
 *  <li>{@link PrivateAsset}
 *  </ul>
 *
 *  <p>This caching allows common objects to be created once, and
 *  used across all components and pages.  Without pooling, objects would often be duplicated.
 *
 *
 * <p>TBD: Pooled pages stay forever.  Need a strategy for cleaning up the pool,
 * tracking which pages have been in the pool the longest, etc.  A mechanism
 * for reporting pool statistics would be useful.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PageSource implements IPageSource, IRenderDescription
{
    private Map _fieldBindings;
    private Map staticBindings;
    private Map _externalAssets;
    private Map _contextAssets;
    private Map _privateAssets;
    private IResourceResolver _resolver;

    private static class PageSpecificationResolver
    {
        private String _simplePageName;
        private INamespace _namespace;

        private PageSpecificationResolver(ISpecificationSource source, String pageName)
        {
            int colonx = pageName.indexOf(':');

            if (colonx > 0)
            {
                _simplePageName = pageName.substring(colonx + 1);
                String namespaceId = pageName.substring(0, colonx);


                if (namespaceId.equals(INamespace.FRAMEWORK_NAMESPACE))
                    _namespace = source.getFrameworkNamespace();
                else
                    _namespace = source.getApplicationNamespace().getChildNamespace(namespaceId);
            }
            else
            {
                _simplePageName = pageName;

                _namespace = source.getApplicationNamespace();

                if (!_namespace.containsPage(_simplePageName))
                    _namespace = source.getFrameworkNamespace();

            }
        }

        public INamespace getNamespace()
        {
            return _namespace;
        }

        public ComponentSpecification getSpecification()
        {
            return _namespace.getPageSpecification(_simplePageName);
        }
    }

    /**
     *  The pool of {@link PooledPage}s.  The key is a {@link MultiKey},
     *  built from the page name and the page locale.
     *
     **/

    private Pool _pool;

    public PageSource(IResourceResolver resolver)
    {
        this._resolver = resolver;

        _pool = new Pool();
    }

    public IResourceResolver getResourceResolver()
    {
        return _resolver;
    }

    /**
     *  Builds a key for a named page in the application's current locale.
     *
     **/

    protected MultiKey buildKey(IEngine engine, String pageName)
    {
        Object[] keys;

        keys = new Object[] { pageName, engine.getLocale()};

        // Don't make a copy, this array is just for the MultiKey.

        return new MultiKey(keys, false);
    }

    /**
     *  Builds a key from an existing page, using the page's name and locale.  This is
     *  used when storing a page into the pool.
     *
     **/

    protected MultiKey buildKey(IPage page)
    {
        Object[] keys;

        keys = new Object[] { page.getName(), page.getLocale()};

        // Don't make a copy, this array is just for the MultiKey.

        return new MultiKey(keys, false);
    }

    /**
     *  Gets the page from a pool, or otherwise loads the page.  This operation
     *  is threadsafe.
     *
     **/

    public IPage getPage(IRequestCycle cycle, String pageName, IMonitor monitor) throws PageLoaderException
    {
        IEngine engine = cycle.getEngine();
        Object key = buildKey(engine, pageName);
        IPage result = (IPage) _pool.retrieve(key);

        if (result == null)
        {
            if (monitor != null)
                monitor.pageCreateBegin(pageName);

            PageSpecificationResolver specificationResolver =
                new PageSpecificationResolver(engine.getSpecificationSource(), pageName);

            PageLoader loader = new PageLoader(this);

            result =
                loader.loadPage(
                    pageName,
                    specificationResolver.getNamespace(),
                    cycle,
                    specificationResolver.getSpecification());

             if (monitor != null)
                monitor.pageCreateEnd(pageName);
        }
        else
        {
            // The page loader attaches the engine, but a page from
            // the pool needs to be explicitly attached.

            result.attach(engine);
        }

        return result;
    }

    /**
     *  Returns the page to the appropriate pool.  Invokes
     *  {@link IPage#detach()}.
     *
     **/

    public void releasePage(IPage page)
    {
        page.detach();

        _pool.store(buildKey(page), page);
    }

    /**
     *  Invoked (during testing primarily) to release the entire pool
     *  of pages, and the caches of bindings and assets.
     *
     **/

    public synchronized void reset()
    {
        _pool.clear();

        _fieldBindings = null;
        staticBindings = null;
        _externalAssets = null;
        _contextAssets = null;
        _privateAssets = null;

    }

    /**
     *  Gets a field binding for the named field (the name includes the class name
     *  and the field).  If no such binding exists, then one is created, otherwise
     *  the existing binding is returned. 
     *
     **/

    public synchronized IBinding getFieldBinding(String fieldName)
    {
        if (_fieldBindings == null)
            _fieldBindings = new HashMap();

        IBinding result = (IBinding) _fieldBindings.get(fieldName);

        if (result == null)
        {
            result = new FieldBinding(_resolver, fieldName);

            _fieldBindings.put(fieldName, result);
        }

        return result;
    }

    /**
     *  Like {@link #getFieldBinding(String)}, except for {@link StaticBinding}s.
     *
     **/

    public synchronized IBinding getStaticBinding(String value)
    {

        if (staticBindings == null)
            staticBindings = new HashMap();

        IBinding result = (IBinding) staticBindings.get(value);

        if (result == null)
        {
            result = new StaticBinding(value);

            staticBindings.put(value, result);
        }

        return result;
    }

    public synchronized IAsset getExternalAsset(String URL)
    {

        if (_externalAssets == null)
            _externalAssets = new HashMap();

        IAsset result = (IAsset) _externalAssets.get(URL);

        if (result == null)
        {
            result = new ExternalAsset(URL);
            _externalAssets.put(URL, result);
        }

        return result;
    }

    public synchronized IAsset getContextAsset(String assetPath)
    {

        if (_contextAssets == null)
            _contextAssets = new HashMap();

        IAsset result = (IAsset) _contextAssets.get(assetPath);

        if (result == null)
        {
            result = new ContextAsset(assetPath);
            _contextAssets.put(assetPath, result);
        }

        return result;

    }

    public synchronized IAsset getPrivateAsset(String resourcePath)
    {

        if (_privateAssets == null)
            _privateAssets = new HashMap();

        IAsset result = (IAsset) _privateAssets.get(resourcePath);

        if (result == null)
        {
            result = new PrivateAsset(resourcePath);
            _privateAssets.put(resourcePath, result);
        }

        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("PageSource@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');

        if (_pool != null)
        {
            buffer.append("pool=");
            buffer.append(_pool);
        }

        extend(buffer, _fieldBindings, "field bindings");
        extend(buffer, staticBindings, "static bindings");
        extend(buffer, _externalAssets, "external assets");
        extend(buffer, _contextAssets, "context assets");
        extend(buffer, _privateAssets, "private assets");

        buffer.append(']');

        return buffer.toString();
    }

    private void extend(StringBuffer buffer, Map map, String label)
    {
        if (map == null)
            return;

        int count;

        synchronized (map)
        {
            count = map.size();
        }

        if (count == 0)
            return;

        char ch = buffer.charAt(buffer.length() - 1);
        if (ch != ' ' && ch != '[')
            buffer.append(", ");

        buffer.append(count);
        buffer.append(" cached ");
        buffer.append(label);
    }

    /** @since 1.0.6 **/

    public void renderDescription(IMarkupWriter writer)
    {
        writer.print("PageSource");
        writer.begin("ul");

        if (_pool != null)
        {
            writer.begin("li");
            writer.print("pool = ");
            _pool.renderDescription(writer);
            writer.end();
        }

        describe(writer, _fieldBindings, "field bindings");
        describe(writer, staticBindings, "static bindings");
        describe(writer, _externalAssets, "external assets");
        describe(writer, _contextAssets, "context assets");
        describe(writer, _privateAssets, "private assets");

        writer.end(); // <ul>
    }

    /** @since 1.0.6 **/

    private void describe(IMarkupWriter writer, Map map, String label)
    {
        if (map == null)
            return;

        synchronized (map)
        {
            Set entrySet = map.entrySet();
            int count = entrySet.size();

            if (count > 0)
            {
                writer.begin("li");
                writer.print(" ");

                writer.print(count);
                writer.print(" cached ");
                writer.print(label);

                writer.begin("ul");

                Iterator i = map.entrySet().iterator();

                while (i.hasNext())
                {
                    Map.Entry e = (Map.Entry) i.next();

                    writer.begin("li");
                    writer.print(e.getKey().toString());
                    writer.println();
                    writer.end();
                }

                writer.end(); // <ul>
                writer.end(); // <li>

            }
        }
    }
}