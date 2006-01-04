// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.components.IPrimaryKeyConverter;

/**
 * Companion to the {@link org.apache.tapestry.components.ForBean For component}, this class is an
 * implementation of {@link org.apache.tapestry.components.IPrimaryKeyConverter} that performs some
 * additional handling, such as tracking value sets..
 * <p>
 * Value sets are sets of value objects maintained by the converter; the converter will provide a
 * synthetic read/write boolean property that indicates if the {@link #getLastValue() last value} is
 * or is not in the set.
 * <p>
 * A single built-in value set, {@link #isDeleted()} has a special purpose; it controls whether or
 * not values are returned from {@link #getValues()}. Subclasses may add additional synthetic
 * boolean properties and additional sets.
 * <p>
 * Why not just store a boolean property in the object itself? Well, deleted is a good example of a
 * property that is meaningful in the context of an operation, but isn't stored ... once an object
 * is deleted (from secondary storage, such as a database) there's no place to store such a flag.
 * The DefaultPrimaryKey converter is used in this context to store transient, operation data ...
 * such as which values are to be deleted.
 * <p>
 * This class can be thought of as a successor to {@link org.apache.tapestry.form.ListEditMap}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultPrimaryKeyConverter implements IPrimaryKeyConverter
{
    private final Map _map = new HashMap();

    private final List _keys = new ArrayList();

    // The values added to the Map, in the order they were added.
    private final List _values = new ArrayList();

    // The last value accessed by getPrimaryKey() or getValue().
    // Other methods may operate upon this value.

    private Object _lastValue;

    private Set _deletedValues;

    /**
     * Clears all properties of the converter, returning it to a pristine state. Subclasses should
     * invoke this implementation in addition to clearing any of their own state.
     */
    public void clear()
    {
        _map.clear();
        _keys.clear();
        _values.clear();
        _lastValue = null;
        _deletedValues = null;
    }

    public final void add(Object key, Object value)
    {
        Defense.notNull(key, "key");
        Defense.notNull(value, "value");

        if (_map.containsKey(key))
            throw new ApplicationRuntimeException(UtilMessages.keyAlreadyExists(key));

        _map.put(key, value);

        _keys.add(key);
        _values.add(value);

        _lastValue = value;
    }

    /**
     * Returns a unmodifiable list of values stored into the converter, in the order in which they
     * were stored.
     * 
     * @return an unmodifiable List
     * @see #add(Object, Object)
     */
    public final List getAllValues()
    {
        return Collections.unmodifiableList(_values);
    }

    /**
     * Returns a list of all values stored into the converter, with deleted values removed.
     */

    public final List getValues()
    {
        if (isDeletedValuesEmpty())
            return getAllValues();

        List result = new ArrayList(_values);

        result.removeAll(_deletedValues);

        return result;
    }

    /**
     * Returns true if the deleted values set is empty (or null).l
     */
    private boolean isDeletedValuesEmpty()
    {
        return _deletedValues == null || _deletedValues.isEmpty();
    }

    /**
     * Checks to see if the {@link #getLastValue() last value} is, or is not, in the set of deleted
     * values.
     */
    public final boolean isDeleted()
    {
        return checkValueSetForLastValue(_deletedValues);
    }

    /**
     * Checks the set to see if it contains the {@link #getLastValue() last value}.
     * 
     * @param valueSet
     *            the set to check, which may be null
     * @return true if the last value is in the set (if non-null)
     */
    protected final boolean checkValueSetForLastValue(Set valueSet)
    {
        return valueSet != null && valueSet.contains(_lastValue);
    }

    /**
     * Adds or removes the {@link #getLastValue() last value} from the
     * {@link #getDeletedValues() deleted values set}.
     * 
     * @param deleted
     */
    public final void setDeleted(boolean deleted)
    {
        _deletedValues = updateValueSetForLastValue(_deletedValues, deleted);
    }

    /**
     * Updates a value set to add or remove the {@link #getLastValue() last value} to the set. The
     * logic here will create and return a new Set instance if necessary (that is, if inSet is true
     * and set is null). The point is to defer the creation of the set until its actually needed.
     * 
     * @param set
     *            the set to update, which may be null
     * @param inSet
     *            if true, the last value will be added to the set (creating the set as necessary);
     *            if false, the last value will be removed
     * @return the set passed in, or a new Set instance
     */
    protected final Set updateValueSetForLastValue(Set set, boolean inSet)
    {
        if (inSet)
        {
            if (set == null)
                set = new HashSet();

            set.add(_lastValue);

            return set;
        }

        if (set != null)
            set.remove(_lastValue);

        return set;
    }

    /**
     * Returns the last active value; this is the value passed to {@link #getPrimaryKey(Object)} or
     * the value for the key passed to {@link #getValue(Object)}.
     * <p>
     * Maintaining <em>value sets</em> involves adding or removing the active value from a set.
     * 
     * @return the last active object
     */
    public final Object getLastValue()
    {
        return _lastValue;
    }

    /**
     * Returns an unmodifiable set of all values marked as deleted.
     */

    public final Set getDeletedValues()
    {
        return createUnmodifiableSet(_deletedValues);
    }

    /**
     * Converts a value set into a returnable value; null is converted to the empty set, and
     * non-null is wrapped as unmodifiable.
     * 
     * @param valueSet
     *            the set to convert and return
     * @return a non-null, non-modifiable Set
     */
    protected final Set createUnmodifiableSet(Set valueSet)
    {
        return valueSet == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(valueSet);
    }

    /**
     * Iterates over the keys and values, removing any values (and corresponding keys) that that are
     * in the deleted set. After invoking this, {@link #getAllValues()} will be the same as
     * {@link #getValues()}.
     */
    public final void removeDeletedValues()
    {
        _lastValue = null;

        if (isDeletedValuesEmpty())
            return;

        int count = _keys.size();

        for (int i = count - 1; i >= 0; i--)
        {
            if (_deletedValues.contains(_values.get(i)))
            {
                _values.remove(i);
                Object key = _keys.remove(i);

                _map.remove(key);
            }
        }
    }

    /**
     * Gets the primary key of an object previously stored in this converter.
     * 
     * @param value
     *            an object previously stored in the converter
     * @return the corresponding key used to store the object
     * @throws ApplicationRuntimeException
     *             if the value was not previously stored
     * @see #add(Object, Object)
     */
    public final Object getPrimaryKey(Object value)
    {
        int index = _values.indexOf(value);

        if (index < 0)
            throw new ApplicationRuntimeException(UtilMessages.valueNotFound(value), value, null,
                    null);

        _lastValue = value;

        return _keys.get(index);
    }

    /**
     * Given a primary key, locates the corresponding object. May invoke
     * {@link #provideMissingValue(Object)} if no such key has been stored into the converter.
     * 
     * @return the object if the key is found, or null otherwise.
     * @see #add(Object, Object)
     */
    public final Object getValue(Object primaryKey)
    {
        Object result = _map.get(primaryKey);

        if (result == null)
            result = provideMissingValue(primaryKey);

        _lastValue = result;

        return result;
    }

    /**
     * Invoked by {@link #getValue(Object)} when the key is not found in the converter's map.
     * Subclasses may override this method to either obtain the corresponding object from secondary
     * storage, to throw an exception, or to provide a new object instance. This implementation
     * returns null.
     * 
     * @param key
     *            the key for which an object was requested
     * @return the object for the key, or null if no object may can be provided
     */
    protected Object provideMissingValue(Object key)
    {
        return null;
    }

}
