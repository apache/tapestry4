package net.sf.tapestry.util.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.ICleanable;
import net.sf.tapestry.util.JanitorThread;

/**
 *  A Pool is used to pool instances of a useful class.  It uses
 *  keys, much like a {@link Map}, to identify a list of pooled objects.
 *  Retrieving an object from the Pool atomically removes it from the
 *  pool.  It can then be stored again later.
 *
 *  <p>The implementation of Pool is threadsafe.
 *
 *  <p>Pool implements {@link ICleanable}, with a goal of
 *  only keeping pooled objects that have been needed within
 *  a recent time frame.  A generational system is used, where each
 *  pooled object is assigned a generation count.  {@link #executeCleanup}
 *  culls objects whose generation count is too old (outside of a
 *  {@link #getWindow() window}).
 * 
 *  <p>
 *  Objects in the pool can receive two notifications: one notification
 *  when they are {@link #store(Object, Object) stored} into the pool,
 *  and one when they are discarded form the pool.
 * 
 *  <p>
 *  Classes that implement {@link net.sf.tapestry.util.pool.IPoolable}
 *  receive notifications directly, as per the two methods
 *  of that interface.
 * 
 *  <p>
 *  Alternately, an adaptor for the other classes can be
 *  registerered (using {@link #registerAdaptor(Class, IPoolableAdaptor)}.
 *  The adaptor will be invoked to perform the desred cleanup
 *  of the object instead.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Pool implements ICleanable, IRenderDescription
{
    private static final Log LOG = LogFactory.getLog(Pool.class);

    private AdaptorRegistry _adaptors = new AdaptorRegistry();

    /**
     *  The generation, used to cull unused pooled items.
     *
     *  @since 1.0.5
     **/

    private int _generation;

    /**
     *  The generation window, used to identify which
     *  items should be culled.
     *
     *  @since 1.0.5
     **/

    private int _window = 10;

    /**
     *  The number of objects pooled.
     *
     **/

    private int _pooledCount;

    /**
     *  A map of PoolLists, keyed on an arbitrary object.
     *
     **/

    private Map _map;

    /**
     *  Creates a new Pool using the default map size.  Creation of the map is deferred.
     *
     *
     **/

    public Pool()
    {
        this(true);
    }

    /**
     *  Creates a new Pool using the specified map size.  The map is created immediately.
     *
     **/

    public Pool(int mapSize)
    {
        this(mapSize, true);
    }

    /**
     *  @param useSharedJanitor if true, then the Pool is added to
     *  the {@link JanitorThread#getSharedJanitorThread() shared janitor}.
     *
     *  @since 1.0.5
     *
     **/

    public Pool(boolean useSharedJanitor)
    {
        if (useSharedJanitor)
            JanitorThread.getSharedJanitorThread().add(this);

        registerAdaptors();
    }

    /**
     *  Standard constructor.
     *
     *  @param mapSize initial size of the map.
     *  @param useSharedJanitor if true, then the Pool is added to
     *  the {@link JanitorThread#getSharedJanitorThread() shared janitor}.
     *
     *  @since 1.0.5
     **/

    public Pool(int mapSize, boolean useSharedJanitor)
    {
        this(useSharedJanitor);

        _map = new HashMap(mapSize);
    }

    /**
     *  Returns the window used to cull pooled objects during a cleanup.
     *  The default is 10, which works out to about five minutes with
     *  a standard janitor (on a 30 second cycle).
     *
     *  @since 1.0.5
     *
     **/

    public int getWindow()
    {
        return _window;
    }

    /**
     *  Sets the window, or number of generations that an object may stay
     *  in the pool before being culled.
     *
     *  @throws IllegalArgumentException if value is less than 1.
     *
     *  @since 1.0.5
     **/

    public void setWindow(int value)
    {
        if (value < 1)
            throw new IllegalArgumentException("Pool window may not be less than 1.");

        _window = value;
    }

    /**
     *  Returns a previously pooled object with the given key, or null if no
     *  such object exists.  Getting an object from a Pool removes it from the Pool,
     *  but it can later be re-added with {@link #store(Object,Object)}.
     *
     **/

    public synchronized Object retrieve(Object key)
    {
        PoolList list;
        Object result = null;

        if (_map == null)
            _map = new HashMap();

        list = (PoolList) _map.get(key);

        if (list != null)
            result = list.retrieve();

        if (result != null)
            _pooledCount--;

        if (LOG.isDebugEnabled())
            LOG.debug("Retrieved " + result + " from " + key);

        return result;
    }

    /**
     *  Retrieves an instance of the named class.  If no pooled
     *  instance is available, a new instance is created
     *  (using the no arguments constructor).  Objects are
     *  pooled using their actual class as a key.
     * 
     **/

    public Object retrieve(Class objectClass)
    {
        Object result = retrieve((Object) objectClass);

        if (result == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("No instance of " + objectClass.getName() + " is available, instantiating one.");

            try
            {
                result = objectClass.newInstance();
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.getString("Pool.unable-to-instantiate-instance", objectClass.getName()),
                    ex);
            }
        }

        return result;
    }

    /**
     *  Stores an object using its class as a key.
     * 
     *  @see #retrieve(Class)
     * 
     **/

    public void store(Object object)
    {
        store(object.getClass(), object);
    }

    /**
     *  Stores an object in the pool for later retrieval, resetting
     *  the object for storage within the pool.
     *
     **/

    public synchronized void store(Object key, Object object)
    {
        getAdaptor(object).resetForPool(object);

        if (_map == null)
            _map = new HashMap();

        PoolList list = (PoolList) _map.get(key);

        if (list == null)
        {
            list = new PoolList(this);
            _map.put(key, list);
        }

        int count = list.store(_generation, object);

        _pooledCount++;

        if (LOG.isDebugEnabled())
            LOG.debug("Stored " + object + " into " + key + " (" + count + " pooled)");
    }

    /**
     *  Removes all previously pooled objects from this Pool.
     *
     **/

    public synchronized void clear()
    {
        if (_map != null)
        {
            Iterator i = _map.values().iterator();

            while (i.hasNext())
            {
                PoolList list = (PoolList) i.next();

                list.discardAll();
            }

            _map.clear();
        }

        _pooledCount = 0;

        if (LOG.isDebugEnabled())
            LOG.debug("Cleared");
    }

    /**
     *  Returns the number of object pooled, the sum of the number
     *  of objects in pooled under each key.  This number should be treated
     *  as approximate, since there are a few minor windows where, under load,
     *  it may not be properly synchronized.
     *
     *  @since 1.0.2
     **/

    public int getPooledCount()
    {
        return _pooledCount;
    }

    /**
     *  Returns the number of keys within the pool.
     *
     *  @since 1.0.2
     **/

    public synchronized int getKeyCount()
    {
        if (_map == null)
            return 0;

        return _map.size();
    }

    /**
     *  Peforms culling of unneeded pooled objects.
     *
     *  @since 1.0.5
     *
     **/

    public synchronized void executeCleanup()
    {
        if (_map == null)
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Executing cleanup of " + this);

        _generation++;

        int oldestGeneration = _generation - _window;

        if (oldestGeneration < 0)
            return;

        int oldCount = _pooledCount;
        int culledKeys = 0;

        // During the cleanup, we keep the entire instance synchronized
        // (meaning other threads will block when trying to store
        // or retrieved pooled objects).  Fortunately, this
        // should be pretty darn quick!

        int newCount = 0;

        Iterator i = _map.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            PoolList list = (PoolList) e.getValue();

            int count = list.cleanup(oldestGeneration);

            if (count == 0)
            {
                i.remove();
                culledKeys++;
            }
            else
                newCount += count;
        }

        _pooledCount = newCount;

        if (LOG.isDebugEnabled())
            LOG.debug("Culled " + (oldCount - _pooledCount) + " pooled objects and " + culledKeys + " keys.");
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("generation", _generation);
        builder.append("pooledCount", _pooledCount);

        return builder.toString();
    }

    /** @since 1.0.6 **/

    public synchronized void renderDescription(IMarkupWriter writer)
    {
        writer.begin("table");
        writer.attribute("border", "1");
        writer.println();

        writer.begin("tr");
        writer.begin("th");
        writer.attribute("colspan", "2");
        writer.print(toString());
        writer.end();
        writer.end();
        writer.println();

        if (_map != null)
        {
            Iterator i = _map.entrySet().iterator();

            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry) i.next();
                PoolList list = (PoolList) entry.getValue();

                writer.begin("tr");
                writer.begin("td");
                writer.print(entry.getKey().toString());
                writer.end();
                writer.begin("td");
                writer.print(list.getPooledCount());
                writer.end();
                writer.end();

                writer.println();
            }
        }
    }

    /**
     *  Invoked from the constructor to register the default set of
     *  {@link net.sf.tapestry.util.pool.IPoolableAdaptor}.  Subclasses
     *  may override this to register a different set.
     * 
     *  <p>
     *  Registers:
     *  <ul>
     *  <li>{@link NullPoolableAdaptor} for class Object
     *  <li>{@link DefaultPoolableAdaptor} for interface {@link IPoolable}
     *  <li>{@link StringBufferAdaptor} for {@link StringBuffer}
     *  </ul>
     * 
     *  @since 2.4
     * 
     **/

    protected void registerAdaptors()
    {
        registerAdaptor(Object.class, new NullPoolableAdaptor());
        registerAdaptor(IPoolable.class, new DefaultPoolableAdaptor());
        registerAdaptor(StringBuffer.class, new StringBufferAdaptor());
    }

    /**
     *  Registers an adaptor for a particular class (or interface).
     * 
     *  @since 2.4
     * 
     **/

    public void registerAdaptor(Class registrationClass, IPoolableAdaptor adaptor)
    {
        _adaptors.register(registrationClass, adaptor);
    }

    /**
     *  Returns an adaptor appropriate to the object.
     * 
     **/

    public IPoolableAdaptor getAdaptor(Object object)
    {
        return (IPoolableAdaptor) _adaptors.getAdaptor(object.getClass());
    }
}