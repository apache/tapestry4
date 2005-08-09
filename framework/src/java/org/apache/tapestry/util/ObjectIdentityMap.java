package org.apache.tapestry.util;

/**
 * A simple map-like collection, similar to (but more more limited than) JDK 1.4's IdentityHashMap.
 * It is designed for <em>small</em> collections of objects.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ObjectIdentityMap
{
    private int _pairCount = 0;

    // Alternates between keys and values

    private Object[] _pairs;

    /**
     * Adds or updates a key in the bag.
     * 
     * @param key
     *            the key to store a value under; an existing value with the key is discarded
     * @param value
     *            the value to store
     */
    public void put(Object key, Object value)
    {
        for (int i = 0; i < _pairCount; i++)
        {
            int index = 2 * i;

            if (_pairs[index] == key)
            {
                _pairs[index + 1] = value;
                return;
            }
        }

        expandPairsIfNeeded();

        int index = 2 * _pairCount;

        _pairs[index] = key;
        _pairs[index + 1] = value;

        _pairCount++;
    }

    /**
     * Returns the object stored for the given key.
     * 
     * @return the value, or null if the key is not found
     */

    public Object get(Object key)
    {
        for (int i = 0; i < _pairCount; i++)
        {
            int index = 2 * i;

            if (_pairs[index] == key)
            {
                return _pairs[index + 1];
            }
        }

        return null;
    }

    private void expandPairsIfNeeded()
    {
        int currentSize = _pairs == null ? 0 : _pairs.length;

        int newLength = 2 * (_pairCount + 1);

        if (newLength >= currentSize)
        {
            // Expand to dobule current size. Allocate room for 5 keys and 5 values
            // initially.

            int newSize = Math.max(10, 2 * currentSize);

            Object[] newPairsArray = new Object[newSize];

            if (currentSize > 0)
                System.arraycopy(_pairs, 0, newPairsArray, 0, currentSize);

            _pairs = newPairsArray;
        }
    }
}