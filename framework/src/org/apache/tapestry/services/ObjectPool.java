package org.apache.tapestry.services;


/**
 * An pool for objects.  Objects may be stored in a Pool for later reuse.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ObjectPool
{
	/**
	 * Returns an object from the pool, previously stored with the given key. May
	 * return null if no such object exists.
	 */
	Object get(Object key);
	
	/**
	 * Stores an object into the pool for later retrieval with the provided key.
	 */
	
	void store(Object key, Object value);
}
