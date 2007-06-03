package org.apache.tapestry.util;

import org.apache.hivemind.util.Defense;

import java.util.Iterator;

/**
 * <p>This class implements an {@link Iterator} which can only return a fixed
 * number of items.</p>
 *
 */
public class SizeRestrictingIterator implements Iterator {

    private static final int DEFAULT_MAX_SIZE = 20;

    private final int _maxSize;
    private final Iterator _iterator;
    private int _currentIndex;

    /**
     * Constructs an Iterator which will return at most {@link #DEFAULT_MAX_SIZE} items.
     *
     * @param iterator
     *          The underlying iterator this object will defer to for actual
     *          iteration.
     */
    public SizeRestrictingIterator(Iterator iterator)
    {
        this(iterator, DEFAULT_MAX_SIZE);
    }

    /**
     * Constructs an Iterator which will return at most as many
     * items as defined by the user.
     *
     * @param iterator
     *          The underlying iterator this object will defer to for actual
     *          iteration.
     * @param maxSize
     *          How many items to return / filter the list by.
     */
    public SizeRestrictingIterator(Iterator iterator, int maxSize)
    {
        Defense.notNull(iterator, "Iterator source");
        
        _iterator = iterator;
        _maxSize = maxSize;
        
        _currentIndex = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        return _currentIndex < _maxSize && _iterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public Object next()
    {
        _currentIndex++;
        return _iterator.next();
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        _currentIndex--;
        _iterator.remove();
    }
    
    public String toString()
    {
        return "SizeRestrictingIterator[" +
               "_maxSize=" + _maxSize +
               '\n' +
               ", _current=" + _currentIndex +
               '\n' +
               ", _iterator=" + _iterator +
               '\n' +
               ']';
    }
}
